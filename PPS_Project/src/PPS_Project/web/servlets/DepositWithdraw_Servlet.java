package PPS_Project.web.servlets;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

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
 * Servlet implementation class DepositWithdraw_Servlet
 */
@WebServlet("/DepositWithdraw_Servlet")
public class DepositWithdraw_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private User_DAO userDAO;
	private Transactions_DAO transactionDAO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DepositWithdraw_Servlet() {
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
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String action = request.getServletPath();
        System.out.println(action);
        try {
            switch (action) {
            case "/deposit":
                System.out.println("The action is: login");
                depositDollars(request, response);
                break;
            case "/withdraw":
                System.out.println("The action is: withdraw");
                withdrawDollars(request, response);
                break;
            default:
                System.out.println("Not sure which action, we will treat it as the list action");
                break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
	}
	
	private void depositDollars(HttpServletRequest request, HttpServletResponse response) 
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
			user = userDAO.selectUser(user.getUser_email());
			double dollarAmount = Double.parseDouble(request.getParameter("dollar-amount-deposit"));
			
			//double newDollarBalance = dollarAmount + user.getDollar_balance();
			user.setDollar_balance(user.getDollar_balance() + dollarAmount); 
			boolean isDollarUpdated = userDAO.updateUserDollarBalance(user);
			
			// Insert deposit transaction to transactions table
			Transactions newDepositTransaction = new Transactions();
			//long millis = System.currentTimeMillis(); 
			newDepositTransaction.setTransaction_date(LocalDate.now().toString());
			newDepositTransaction.setTransaction_time(LocalTime.now().toString());
			newDepositTransaction.setDollar_amount(dollarAmount);
			newDepositTransaction.setPPS_amount(0);
			newDepositTransaction.setTransaction_name("DEPOSIT");
			newDepositTransaction.setTransaction_from_email(user.getUser_email());
			newDepositTransaction.setTransaction_to_email(user.getUser_email());
			newDepositTransaction.setPPS_price(0);
			
			boolean isInserted = transactionDAO.insertTransaction(newDepositTransaction);
			
			//System.out.println("isDollarUpdated: "+ isDollarUpdated);
			System.out.println("isTransactionInserted: "+ isInserted);
			request.setAttribute("message", "Deposit Successful.");
			request.getRequestDispatcher("user-balancePage.jsp").forward(request, response);
			
			return;
		} catch (Exception e) {
			System.out.println(e);
			request.setAttribute("errorMessage", "Error Occurred. Could NOT update the balance.");
			request.getRequestDispatcher("user-balancePage.jsp").forward(request, response);
			return;
		}
		
		
	}
	
	private void withdrawDollars(HttpServletRequest request, HttpServletResponse response) 
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
			double dollarAmount = Double.parseDouble(request.getParameter("dollar-amount-withdraw"));
			user = userDAO.selectUser(user.getUser_email());
			if (user.getDollar_balance() > dollarAmount) {
				user.setDollar_balance(user.getDollar_balance() - dollarAmount); 
				boolean isDollarUpdated = userDAO.updateUserDollarBalance(user);
				
				// Insert deposit transaction to transactions table
				Transactions newWithdrawTransaction = new Transactions();
				//long millis = System.currentTimeMillis(); 
				newWithdrawTransaction.setTransaction_date(LocalDate.now().toString());
				newWithdrawTransaction.setTransaction_time(LocalTime.now().toString());
				newWithdrawTransaction.setDollar_amount(dollarAmount);
				newWithdrawTransaction.setPPS_amount(0);
				newWithdrawTransaction.setTransaction_name("WITHDRAW");
				newWithdrawTransaction.setTransaction_from_email(user.getUser_email());
				newWithdrawTransaction.setTransaction_to_email(user.getUser_email());
				newWithdrawTransaction.setPPS_price(0);
				
				boolean isInserted = transactionDAO.insertTransaction(newWithdrawTransaction);
				
				//System.out.println("isDollarUpdated: "+ isDollarUpdated);
				System.out.println("isTransactionInserted: "+ isInserted);
				request.setAttribute("message", "Withdraw Successful.");
				request.getRequestDispatcher("user-balancePage.jsp").forward(request, response);
				return;
			} else {
				request.setAttribute("errorMessage", "No enough balance");
				request.getRequestDispatcher("user-balancePage.jsp").forward(request, response);
				return;
			}
			
		
			
		} catch (Exception e) {
			System.out.println(e);
			request.setAttribute("errorMessage", "Error Occurred. Could NOT update the balance.");
			request.getRequestDispatcher("user-balancePage.jsp").forward(request, response);
			return;
		}
		
		
	}

}
