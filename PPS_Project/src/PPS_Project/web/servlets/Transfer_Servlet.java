package PPS_Project.web.servlets;

import java.io.IOException;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import PPS_Project.DAO.Transactions_DAO;
import PPS_Project.DAO.User_DAO;
import PPS_Project.bean.Transactions;
import PPS_Project.bean.User;

/**
 * Servlet implementation class transfer_servlet
 */
@WebServlet("/Transfer_Servlet")
public class Transfer_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       //private User user;
       private User_DAO userDAO;
   	   private Transactions_DAO transactionDAO;
  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Transfer_Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		userDAO = new User_DAO();
		transactionDAO = new Transactions_DAO();
	
		
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
		
		String action = request.getServletPath();
				System.out.println(action);
				
				try {
					switch (action) {
					case "/transfer":
						transferPPS(request, response);
					break;
					default:
					    System.out.println(" tranfer requested");
					           	
					  break;
			        }   
		        }
				 catch (SQLException ex) {
			            throw new ServletException(ex);
			        }
    
	}
	
    
    
    private void transferPPS (HttpServletRequest request, HttpServletResponse response) 
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
		
		int PPS_amount = Integer.parseInt(request.getParameter("PPS-amount"));
    	String recieverEmail = request.getParameter("receiver-email");
    	
    	
    	User latestUpdatedSenderInfo = userDAO.selectUser(user.getUser_email());
    	User receiverInfo = userDAO.selectUser(recieverEmail);
    	
    	//String senderEmail = latestUpdatedSenderInfo.getUser_email();
    	
    	
    	if (receiverInfo != null && !receiverInfo.getUser_email().equalsIgnoreCase(latestUpdatedSenderInfo.getUser_email())) {
    		// Check if the sender PPS balance is enough
    		if(latestUpdatedSenderInfo.getPPS_balance() >= PPS_amount) {
    			// Update the sender balance: 
    			latestUpdatedSenderInfo.setPPS_balance(latestUpdatedSenderInfo.getPPS_balance() - PPS_amount);
    			boolean isSenderPPSUpdated = userDAO.updateUserPPSBalance(latestUpdatedSenderInfo);
    			System.out.println("isSenderPPSUpdated: "+ isSenderPPSUpdated);
    			
    			// Update the receiver balance: 
    			receiverInfo.setPPS_balance(receiverInfo.getPPS_balance() + PPS_amount);
    			boolean isReceiverPPSUpdated = userDAO.updateUserPPSBalance(receiverInfo);
    			System.out.println("isReceiverPPSUpdated: "+ isReceiverPPSUpdated);
    			
    			// Insert the transfer transaction to the transactions table
    			Transactions newTransaferTransaction = new Transactions ();
    			newTransaferTransaction.setTransaction_date(LocalDate.now().toString());
    			newTransaferTransaction.setTransaction_time(LocalTime.now().toString());
    			newTransaferTransaction.setDollar_amount(0.0);
    			newTransaferTransaction.setPPS_amount(PPS_amount);
    			newTransaferTransaction.setTransaction_name("TRANSFER");
    			newTransaferTransaction.setTransaction_from_email(latestUpdatedSenderInfo.getUser_email());
    			newTransaferTransaction.setTransaction_to_email(receiverInfo.getUser_email());
    			boolean isTransferTransactionInserted = transactionDAO.insertTransaction(newTransaferTransaction);
    			System.out.println("isTrnsaferTransactionInserted: "+ isTransferTransactionInserted);
    			
    			request.setAttribute("message", "Transfer is Successful.");
				request.getRequestDispatcher("user-transferPage.jsp").forward(request, response);
				return;
    			
    		} else {
    			request.setAttribute("errorMessage", "No enough PPS");
    			request.getRequestDispatcher("user-transferPage.jsp").forward(request, response);
    			return;
    		}
    		
    		
    	} else {
    		request.setAttribute("errorMessage", "The receiver email is not correct");
			request.getRequestDispatcher("user-transferPage.jsp").forward(request, response);
			return;
    	}
    	
    	
    	
    	
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
