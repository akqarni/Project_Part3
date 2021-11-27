package PPS_Project.web.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import PPS_Project.DAO.PPS_Price_DAO;
import PPS_Project.DAO.Transactions_DAO;
import PPS_Project.DAO.User_DAO;
import PPS_Project.bean.PPS_Price;
import PPS_Project.bean.Transactions;
import PPS_Project.bean.User;

/**
 * Servlet implementation class Buy_and_Sell_Servlet
 */
@WebServlet("/Buy_and_Sell_Servlet")
public class Buy_and_Sell_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private User_DAO userDAO;
	private Transactions_DAO transactionDAO;
	private PPS_Price_DAO PPS_PriceDAO;
	private PPS_Price latestPPSprice;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Buy_and_Sell_Servlet() {
        super();
        // TODO Auto-generated constructor stub
        userDAO = new User_DAO();
		transactionDAO = new Transactions_DAO();
		PPS_PriceDAO = new PPS_Price_DAO();
		
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
            case "/buy":
                System.out.println("The action is: login");
                buyPPS(request, response);
                break;
            case "/sell":
                System.out.println("The action is: withdraw");
                sellPPS(request, response);
                break;
            default:
                System.out.println("Not sure which action, we will treat it as the list action");
                break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
	}
	
	private void buyPPS(HttpServletRequest request, HttpServletResponse response) 
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
			int pps_buy_amount = Integer.parseInt(request.getParameter("PPS-to-buy-amount"));
			System.out.println("requested pps amount: "+ pps_buy_amount);
			
			user = userDAO.selectUser(user.getUser_email());
			User root = userDAO.selectUser("root");
			
			double user_dollar_balance = user.getDollar_balance();
			System.out.println("requested user dollar balance: "+ user_dollar_balance);
			long user_pps_balance = user.getPPS_balance();
			
			
			long root_PPS_balance = root.getPPS_balance();
			double root_dollar_amount=root.getDollar_balance();
			
			System.out.println("root pps amount: "+ root_PPS_balance);
			
			PPS_Price PPS_price = PPS_PriceDAO.getLastest_PPS_price();
			double currentPPS_price = PPS_price.getPrice();
			System.out.println("current pps price: "+ currentPPS_price);
			 
			double required_dollar_amount = (double) pps_buy_amount * currentPPS_price;
			System.out.println("required dollar to buy pps: "+ required_dollar_amount);
			
			
			double updated_pps_price = 1/((1/currentPPS_price)-1);
			System.out.println("updated pps price: "+ updated_pps_price);
			
			long updated_root_pps_balance = root_PPS_balance - pps_buy_amount;
			System.out.println("root updated pps balance: "+ updated_root_pps_balance);
			
			double updated_root_dollar_balance = root_dollar_amount + required_dollar_amount;
			System.out.println("root updated dollar balance: "+ updated_root_dollar_balance);
			
			
			long updated_user_pps_balance = user_pps_balance + pps_buy_amount;
			System.out.println("buyer updated pps balance: "+ updated_user_pps_balance);
			
			double updated_user_dollar_balance = user_dollar_balance - required_dollar_amount;
			System.out.println("buyer updated dollar balance: "+ updated_user_dollar_balance);
			
			User latestUpdatedrootinfo = userDAO.selectUser("root");
			User latestUpdatedbuyerinfo = userDAO.selectUser(user.getUser_email());
		
			
			latestPPSprice = new PPS_Price(updated_pps_price);
			
			
			if (root_PPS_balance >= pps_buy_amount)
			{
				if(user_dollar_balance >= required_dollar_amount) {
					//update pps price
					boolean isupdatedppsprice = PPS_PriceDAO.update_PPS_price(latestPPSprice);
					System.out.println("isupdatedppsprice "+ isupdatedppsprice);
						
					//update root info
					
					latestUpdatedrootinfo.setPPS_balance(updated_root_pps_balance);
					latestUpdatedrootinfo.setDollar_balance(updated_root_dollar_balance);
					boolean isrootPPSUpdated = userDAO.updateUserPPSBalance(latestUpdatedrootinfo);
	    			System.out.println("isrootPPSUpdated: "+ isrootPPSUpdated);
	    			boolean isrootdollarUpdated = userDAO.updateUserDollarBalance(latestUpdatedrootinfo);
	    			System.out.println("isrootdollarUpdated : "+ isrootdollarUpdated );
	    			
	    			
	    			//update buyer info
					latestUpdatedbuyerinfo .setPPS_balance(updated_user_pps_balance);
					latestUpdatedbuyerinfo.setDollar_balance(updated_user_dollar_balance);
					boolean isbuyerPPSUpdated = userDAO.updateUserPPSBalance(latestUpdatedbuyerinfo);
	    			System.out.println("isbuyerPPSUpdated: "+ isbuyerPPSUpdated);
	    			boolean isbuyerdollarUpdated = userDAO.updateUserDollarBalance(latestUpdatedbuyerinfo);
	    			System.out.println("isbuyerdollarUpdated : "+ isbuyerdollarUpdated );
	    			
	    			
	    			//update transaction table
	    			
	    			Transactions newBuyTransaction = new Transactions ();
	    			newBuyTransaction.setTransaction_date(LocalDate.now().toString());
	    			newBuyTransaction.setTransaction_time(LocalTime.now().toString());
	    			newBuyTransaction.setDollar_amount(required_dollar_amount);
	    			newBuyTransaction.setPPS_amount(pps_buy_amount);
	    			newBuyTransaction.setTransaction_name("BUY");
	    			newBuyTransaction.setTransaction_from_email("root");
	    			newBuyTransaction.setTransaction_to_email(user.getUser_email());
	    			newBuyTransaction.setPPS_price(currentPPS_price);
	    			boolean isTransferTransactionInserted = transactionDAO.insertTransaction(newBuyTransaction);
	    			System.out.println("isTrnsaferTransactionInserted: "+ isTransferTransactionInserted);
	    			request.setAttribute("message", "Successful.");
	    			request.getRequestDispatcher("user-buyPage.jsp").forward(request, response);
					
				} else {
					request.setAttribute("errorMessage", "No enough dollars");
					request.getRequestDispatcher("user-buyPage.jsp").forward(request, response);
					return;
				}
				
			}
			
			return;
		} catch (Exception e) {
			System.out.println(e);
			request.setAttribute("errorMessage", "Error Occurred. Could NOT update the balance.");
			request.getRequestDispatcher("user-buyPage.jsp").forward(request, response);
			return;
		}
		
	}
	
	private void sellPPS(HttpServletRequest request, HttpServletResponse response) 
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
			User root = userDAO.selectUser("root");
			
			long root_PPS_balance = root.getPPS_balance();
			
			int PPS_Amount = Integer.parseInt(request.getParameter("PPS-to-sell-amount"));
			long seller_pps_amount = user.getPPS_balance();
			double seller_dollar_balance=user.getDollar_balance();
			
			double root_current_dollar_balance=root.getDollar_balance();
			long root_current_pps_amount = root.getPPS_balance();		
			
			PPS_Price PPS_price = PPS_PriceDAO.getLastest_PPS_price();
			
			double currentPPS_price = PPS_price.getPrice();
						
			double required_dollar_balance = (double)PPS_Amount * currentPPS_price;
			
			double updated_pps_price=1/((1/currentPPS_price)+1);
			long seller_updated_pps_amount = seller_pps_amount-PPS_Amount ;
			double seller_updated_dollar_balance=seller_dollar_balance+required_dollar_balance;
						
			long root_updated_pps_amount= root_current_pps_amount + PPS_Amount;
			double root_updated_dollar_balance=root_current_dollar_balance-required_dollar_balance;
				
			User latestUpdatedrootinfo = userDAO.selectUser("root");
			User latestUpdatedsellerinfo = userDAO.selectUser(user.getUser_email());
		
			
			latestPPSprice=new PPS_Price(updated_pps_price);
			
			
			if(PPS_Amount<=seller_pps_amount)
			{
				if(root_current_dollar_balance>= required_dollar_balance)
				{
					
					//update pps price
					boolean isupdatedppsprice=PPS_PriceDAO.update_PPS_price(latestPPSprice);
					System.out.println("isupdatedppsprice "+ isupdatedppsprice);
					
					//update root info
					latestUpdatedrootinfo.setPPS_balance(root_updated_pps_amount);
					latestUpdatedrootinfo.setDollar_balance(root_updated_dollar_balance);
					boolean isrootPPSUpdated = userDAO.updateUserPPSBalance(latestUpdatedrootinfo);
	    			System.out.println("isrootPPSUpdated: "+ isrootPPSUpdated);
	    			boolean isrootdollarUpdated = userDAO.updateUserDollarBalance(latestUpdatedrootinfo);
	    			System.out.println("isrootdollarUpdated : "+isrootdollarUpdated );
					
	    			
	    			//update seller info
	    			latestUpdatedsellerinfo.setPPS_balance(seller_updated_pps_amount);
	    			latestUpdatedsellerinfo.setDollar_balance(seller_updated_dollar_balance);
					boolean issellerPPSUpdated = userDAO.updateUserPPSBalance(latestUpdatedsellerinfo );
	    			System.out.println("issellerPPSUpdated: "+ issellerPPSUpdated);
	    			boolean issellerdollarUpdated = userDAO.updateUserDollarBalance(latestUpdatedsellerinfo );
	    			System.out.println("issellerdollarUpdated : "+issellerdollarUpdated );
	    			
	    			
	    			
	    			//update transaction table
	    			Transactions newsellTransaction = new Transactions ();
	    			newsellTransaction.setTransaction_date(LocalDate.now().toString());
	    			newsellTransaction.setTransaction_time(LocalTime.now().toString());
	    			newsellTransaction.setDollar_amount(required_dollar_balance);
	    			newsellTransaction.setPPS_amount(PPS_Amount);
	    			newsellTransaction.setTransaction_name("sell");
	    			newsellTransaction.setTransaction_from_email(user.getUser_email());
	    			newsellTransaction.setTransaction_to_email("root");
	    			newsellTransaction.setPPS_price(currentPPS_price);
	    			boolean isTransferTransactionInserted = transactionDAO.insertTransaction(newsellTransaction);
	    			System.out.println("isTrnsaferTransactionInserted: "+ isTransferTransactionInserted);
	    			
	    			request.setAttribute("message", "Successful.");
	    			request.getRequestDispatcher("user-sellPage.jsp").forward(request, response);
					
					
					
				}	
				
			}
			
			
		
			
			return;
		} catch (Exception e) {
			System.out.println(e);
			request.setAttribute("errorMessage", "Error Occurred. Could NOT update the balance.");
			request.getRequestDispatcher("user-sellPage.jsp").forward(request, response);
			return;
		}
		
	}

	

}
