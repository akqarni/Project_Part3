package PPS_Project.web.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.x.protobuf.MysqlxCrud.Projection;

import PPS_Project.DAO.User_DAO;
import PPS_Project.bean.User;

/**
 * Servlet implementation class User_Servlet
 */
@WebServlet("/User_Servlet")
public class User_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private User_DAO userDAO;
	private project1 project;
       
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
                /*
            case "/update":
                System.out.println("The action is: update");
                updateUser(request, response);
                break;*/
            case "/initialize": 
    		    System.out.println("The action is: initialize");
    				intializeDatabase(request, response);   	
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
				double dollar_balance = 0.0;
				
				User newUser = new User(email, password,  fname, lname, address, dob, PPS_balance, dollar_balance);
				
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
        	String message = "Intialization completed! <br>";
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
    


	

}
