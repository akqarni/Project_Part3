package PPS_Project.web.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.x.protobuf.MysqlxCrud.Projection;

import PPS_Project.DAO.Follows_DAO;
import PPS_Project.DAO.User_DAO;
import PPS_Project.bean.Follows;
import PPS_Project.bean.User;

/**
 * Servlet implementation class User_Servlet
 */
@WebServlet("/User_Servlet")
public class User_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private User_DAO userDAO;
	private project1 project;
     private Follows_DAO followdao; 
     private User user;
    /** 
     * @see HttpServlet#HttpServlet()
     */
    public User_Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		userDAO = new User_DAO();
		project=new project1();
		followdao=new Follows_DAO();
		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String action = request.getServletPath();
        System.out.println(action);
       
        try {
            switch (action) {
            case "/login":
                System.out.println("The action is: login");
                loginUser(request, response);
                break;
            case "/log-out":
                System.out.println("The action is: logout");
                logoutUser(request, response);
                break;
            case "/new":
                System.out.println("The action is: new");
                showNewForm(request, response);
                break;
            case "/insert":
                System.out.println("The action is: insert");
            	insertUser(request, response);
                break;
            case "/delete":
                System.out.println("The action is: delete");
            	deleteUser(request, response);
                break;
            case "/edit":
                System.out.println("The action is: edit");
                showEditForm(request, response);
                break;
            case "/showFollowingInfo":
                System.out.println("The action is: showFollowingInfo");
                showFollowingInfo(request, response);
                break;
            case "/initialize": 
    		    System.out.println("The action is: initialize");
    				intializeDatabase(request, response);   	
    		    break;
            case "/follow": 
    		    System.out.println("The action is: follow");
    				follow(request, response);   	
    		    break;
            case "/selectinfo": 
    		    System.out.println("The action is:selectinfo");
    				selectinfo(request, response);   	
    		    break;
            default:
                System.out.println("Not sure which action, we will treat it as the list action");
                showDashboardPage(request, response);           	
                break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    
	}
	
	// login user
    private void loginUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		User user = userDAO.validateUser(email, password);
		
		System.out.println("is user null? " + userDAO == null );
		
		// validate input data.
		StringBuilder errorBuf = new StringBuilder();

		if ((email == null) || email.trim().equals("")) {
			email = "";
			errorBuf.append("Required:  Email. <br>");
		}

		if ((password == null) || password.trim().equals("")) {
			password = "";
			errorBuf.append("Required:  password. <br>");
		}

		String errorMessage = errorBuf.toString();
		if (!errorMessage.isEmpty()) {
			request.setAttribute("errorMessage", errorBuf.toString());
			request.getRequestDispatcher("user-loginForm.jsp").forward(request, response);
			return;

		}
		
		
		if (user != null) {
			// Init session for Authenticated user.
			HttpSession session = request.getSession(true);
			if(user.getUser_email().equalsIgnoreCase("root")) {
				//response.sendRedirect("show");
				// Init session for Authenticated user
				session.setAttribute("user", user);
				
				RequestDispatcher dispatcher = request.getRequestDispatcher("root-welcomePage.jsp");
		        dispatcher.forward(request, response);
				System.out.println("login success");	
			} else {
				//response.sendRedirect("show");
				// Init session for Authenticated user.
				session.setAttribute("user", user);
				session.setAttribute("transferfromemail",user.getUser_email());
				RequestDispatcher dispatcher = request.getRequestDispatcher("user-activityPage.jsp");
		        dispatcher.forward(request, response);
				System.out.println("login success");
				
			}
		}
		else {
			//response.sendRedirect("login");
			request.setAttribute("errorMessage", "Invalid User account / password. <br>");
			RequestDispatcher dispatcher = request.getRequestDispatcher("user-loginForm.jsp");
	        dispatcher.forward(request, response);
	        System.out.println("login failed");
			}
		
	}
    
    // Log out user
    private void logoutUser (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

		try {
			// Init session for Authenticated user.
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}

			// Reset message
			request.setAttribute("message", "Logout Successful.");
			request.setAttribute("errorMessage", "");

			// Forward to index page.
			request.getRequestDispatcher("user-loginForm.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "UNEXPECTED ERROR occurred.");
			request.getRequestDispatcher("user-loginForm.jsp").forward(request, response);
			return;
		}
	}
    	
    
	
	// Show the sign-up form
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("showNewForm started: 000000000000000000000000000");
     
        RequestDispatcher dispatcher = request.getRequestDispatcher("user-signupForm.jsp");
        dispatcher.forward(request, response);
        System.out.println("The user sees the InsertPeopleForm page now.");
     
        System.out.println("showNewForm finished: 1111111111111111111111111111111");
    }
    
    // Insert new user
    private void insertUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
    	String errorMessage = "";
    	String email = request.getParameter("email");
    	User user = userDAO.selectUser(email);
    	
    	
    	if (user != null) {
			//response.sendRedirect("show");
    		errorMessage += "Account already exists for User email: <br>" + email
					+ "<br><br> Please Login with your email and password." + "<br>";

			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher dispatcher = request.getRequestDispatcher("user-loginForm.jsp");
	        dispatcher.forward(request, response);
			System.out.println("signup failed");
			}
		else {
			if (request.getParameter("password1").equals(request.getParameter("password2"))) {
				//response.sendRedirect("login");
				String password = request.getParameter("password1");
				String fname = request.getParameter("fname");
				String lname = request.getParameter("lname");
				String address = request.getParameter("address");
				String dob = request.getParameter("dob");
				long PPS_balance = 0;
				String default_settings = "10111111";
				double dollar_balance = 0.0;
				
				User newUser = new User(email, password,  fname, lname, address, dob, PPS_balance, dollar_balance, default_settings);
				
				userDAO.insertUser(newUser);
				RequestDispatcher dispatcher = request.getRequestDispatcher("user-loginForm.jsp");
				String message = "Signup Successful! <br>";
				request.setAttribute("message", message);
		        dispatcher.forward(request, response);
		        System.out.println("signup success");
				
			} else {
				//response.sendRedirect("show");
	    		errorMessage += "Password is not matched <br>";

				request.setAttribute("errorMessage", errorMessage);
				RequestDispatcher dispatcher = request.getRequestDispatcher("user-signupForm.jsp");
		        dispatcher.forward(request, response);
				System.out.println("signup failed");
			}
			
			}
    	
		//response.sendRedirect("login");
	}
    
    // Insert follower
    private void follow(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
    	String errorMessage = "";
      	HttpSession session = request.getSession(false);
    		if (session == null) {
    			// User not signed in. Or session expired.
    			// Forward to user login page.
    			request.getRequestDispatcher("user-loginForm.jsp").forward(request, response);
    			return;
    		}
    		
    		User user = (User) session.getAttribute("user");
    		if (user == null) {
    			// User not signed in. Or session expired.
    			// Forward to user login page.
    			request.getRequestDispatcher("user-loginForm.jsp").forward(request, response);
    			return;
    		}
           String follower_email=user.getUser_email();
    

    	String  following_email =request.getParameter("receiver-email");
    	System.out.println(following_email);
    	
    	User user1 = userDAO.selectUser(following_email);
  
    	if(user1!=null)
    	{
    		Follows follow=new Follows(user.getUser_email(),following_email);
    		followdao.insertFollow(follow);
    		request.setAttribute("message", "Successful");
			request.getRequestDispatcher("user-followersPage.jsp").forward(request, response);
    	}
    	else {
    		request.setAttribute("errorMessage", "Invalid Email");
			request.getRequestDispatcher("user-followersPage.jsp").forward(request, response);
    	}
		//response.sendRedirect("login");
	}
    

    // Delete a user
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		String email = request.getParameter("email");
		userDAO.deleteUser(email);
		response.sendRedirect("show");

	}
    
    // Show edit from to update
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
    	String email = request.getParameter("email");
		User existingUser = userDAO.selectUser(email);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-updateForm.jsp");
		request.setAttribute("user", existingUser);
		dispatcher.forward(request, response);
	}
    
    /*
    // Update the user info
    private void updateUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
    	String email = request.getParameter("email");
    	String password = request.getParameter("password");
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String address = request.getParameter("address");
		String dob = request.getParameter("dob");
		
		User user = new User(email, password ,fname, lname, address, dob);
		userDAO.updateUser(user);
		
		response.sendRedirect("show");
	}*/
    private void intializeDatabase(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException { // Note, the execption is not working 
    	try {
    		project.part1();
        	RequestDispatcher dispatcher = request.getRequestDispatcher("root-welcomePage.jsp");
        	String message = "Intialization completed! <br>";
    		request.setAttribute("message", message);
        	dispatcher.forward(request, response);	
    	} catch (Exception e) {
    		RequestDispatcher dispatcher = request.getRequestDispatcher("root-welcomePage.jsp");
    		request.setAttribute("errorMessage", "Can not intialize the database");
        	dispatcher.forward(request, response);
    		
    	}
    	
	}
    
    // Default: show dashboard page
    private void showDashboardPage (HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		//request.setAttribute("listUser", listUser);
		//dispatcher.forward(request, response);
	}
    
 // Default: show dashboard page
    private void showFollowingInfo (HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
    	
    	HttpSession session = request.getSession(false);
    	User user = (User) session.getAttribute("user");
		if (user == null) {
			// User not signed in. Or session expired.
			// Forward to user login page.
			request.getRequestDispatcher("user-loginForm.jsp").forward(request, response);
			return;
		}
    	try {
    		String followingEmail = request.getParameter("followingEmail");
    		User FollowingUser = userDAO.selectUser(followingEmail);
    		//System.out.println("FollowingUser is null ? "+ FollowingUser == null);
    		//"10111111"
    		//"01234567"
    		String setting = FollowingUser.getUser_settings();
    		System.out.println("FE: "+ followingEmail + " Setting: "+ setting);
    		List <String> userInfo = new ArrayList <String>();
    		if (setting.charAt(0) == '1') {
    			userInfo.add("User email: "+ FollowingUser.getUser_email());
    		}
    		if (setting.charAt(2) == '1') {
    			userInfo.add("First Name: "+ FollowingUser.getUser_fname());
    		}
    		if (setting.charAt(3) == '1') {
    			userInfo.add("Last Name: "+ FollowingUser.getUser_lname());
    		}
    		if (setting.charAt(4) == '1') {
    			userInfo.add("Address: "+ FollowingUser.getUser_address());
    		}
    		if (setting.charAt(5) == '1') {
    			userInfo.add("Birthday: "+ FollowingUser.getUser_dob());
    		}
    		if (setting.charAt(6) == '1') {
    			userInfo.add("PPS balance: "+ FollowingUser.getPPS_balance());
    		}
    		if (setting.charAt(7) == '1') {
    			userInfo.add("Dollar balance: "+ FollowingUser.getDollar_balance());
    		}
    		
    		//System.out.println("userInfo.isEmpty()"+ userInfo.isEmpty());
    		
    		
    		request.setAttribute("followingInfo", userInfo);
        	RequestDispatcher dispatcher = request.getRequestDispatcher("user-followingInfo.jsp");
        	//String message = "??<br>";
    		//request.setAttribute("message", message);
        	dispatcher.forward(request, response);	
    	} catch (Exception e) {
    		System.out.println(e);
    		RequestDispatcher dispatcher = request.getRequestDispatcher("user-followersPage.jsp");
    		request.setAttribute("errorMessage", "Can not show the following information");
        	dispatcher.forward(request, response);
    		
    	}
	}
    
    
    
    private void selectinfo (HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
      	
    	HttpSession session = request.getSession(false);
    	User user = (User) session.getAttribute("user");
		if (user == null) {
			// User not signed in. Or session expired.
			// Forward to user login page.
			request.getRequestDispatcher("user-loginForm.jsp").forward(request, response);
			return;
		}
    	System.out.println("reached");
    	try { StringBuilder settinginfo = new StringBuilder(" ");
    		
    	
    	if(request.getParameter("check1") != null) {
            settinginfo.insert(0,'1');
        }
        else {settinginfo.insert(0,'0');
           
        }
    	settinginfo.insert(1,'0');
        if(request.getParameter("check2") != null) {
        	settinginfo.insert(2,'1');
        }
        else {
        	settinginfo.insert(2,'0');
        }

        if(request.getParameter("check3") != null) {
        	settinginfo.insert(3,'1');
        }
        else {
        	settinginfo.insert(3,'0');
        }
        if(request.getParameter("check4") != null) {
        	settinginfo.insert(4,'1');
        }
        else {
        	settinginfo.insert(4,'0');
        }
        
        if(request.getParameter("check5") != null) {
        	settinginfo.insert(5,'1');
        }
        else {
        	settinginfo.insert(5,'0');
        }
        if(request.getParameter("check6") != null) {
        	settinginfo.insert(6,'1');
        }
        else {
        	settinginfo.insert(6,'0');
        }
        if(request.getParameter("check7") != null) {
        	settinginfo.insert(7,'1');
        }
        else {
        	settinginfo.insert(7,'0');
        }
    	System.out.println("the value of settinginfo   "+ settinginfo);
    	String settings = settinginfo.toString();
    	
    	user.setUser_settings(settings);
    	userDAO.updateUserSetting(user);
    	
    	RequestDispatcher dispatcher = request.getRequestDispatcher("select_information.jsp");
		request.setAttribute("message", "Settings updates are saved! <br>");
    	dispatcher.forward(request, response);
    	
    	} catch (Exception e) {
    		System.out.println(e);
    		RequestDispatcher dispatcher = request.getRequestDispatcher("select_information.jsp");
    		request.setAttribute("errorMessage", "Couldn't update the settings");
        	dispatcher.forward(request, response);
    	}
    
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    


	

}
