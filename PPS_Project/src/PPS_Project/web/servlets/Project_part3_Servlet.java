package PPS_Project.web.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import PPS_Project.DAO.Root_part3_DAO;
import PPS_Project.DAO.Transactions_DAO;
import PPS_Project.DAO.User_DAO;
import PPS_Project.bean.Transactions;
import PPS_Project.bean.User;

/**
 * Servlet implementation class Project_part3_Servlet
 */
@WebServlet("/Project_part3_Servlet")
public class Project_part3_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Root_part3_DAO root_DAO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Project_part3_Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		root_DAO = new Root_part3_DAO();
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
            case "/findCommonFollowers":
                System.out.println("The action is: findCommonFollowers");
                findCommonFollowers(request, response);
                break;
           
            default:
                System.out.println("Not sure which action, we will treat it as the list action");
                break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
	}
	
	private void findCommonFollowers(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		
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
		
		try {
			
			String firstUserEmail = request.getParameter("firstUserEmail");
			String secondUserEmail = request.getParameter("secondUserEmail");
			request.setAttribute("firstUserEmail", firstUserEmail);
			request.setAttribute("secondUserEmail", secondUserEmail);
			
			List <String> commonFollowers =  root_DAO.commonFollowersEmails(firstUserEmail,secondUserEmail);
			System.out.println("commonFollowers.size() " + commonFollowers.size());
			if(commonFollowers.size() != 0) {
				/*
				for (String follower_email : commonFollowers) {
					System.out.println("follower email: ---" + follower_email);
					} // DEBUG
					*/
					
				request.setAttribute("commonFollowers", commonFollowers);
				//request.getRequestDispatcher("root-commonUsers.jsp").forward(request, response);
				RequestDispatcher dispatcher = request.getRequestDispatcher("root-commonUsers.jsp");       
			    dispatcher.forward(request, response);
			    return;
				
			}
			else {
				// Show an error message if there are no common followers between the two users
				request.setAttribute("errorMessage", "There are no common followers between the users");
				request.getRequestDispatcher("root-commonUsers.jsp").forward(request, response);
				return;
			}
			
		} catch (Exception e) {
			System.out.println(e);
			request.setAttribute("errorMessage", "Error Occurred. Could NOT find the common followers.");
			request.getRequestDispatcher("root-commonUsers.jsp").forward(request, response);
			return;
		}
		
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
