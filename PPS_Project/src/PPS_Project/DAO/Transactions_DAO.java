package PPS_Project.DAO;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import java.sql.Date;
import java.sql.Time;
import PPS_Project.bean.Transactions;


public class Transactions_DAO {
	
	private static final long serialVersionUID = 1L;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	
	// ALL SQL QUERIRES
	private static final String INSERT_TRANSCATIONS_SQL = "INSERT INTO transactions" + "  (transaction_name, transaction_date, transaction_time, dollar_amount, PPS_amount, transaction_from_email, transaction_to_email, PPS_Price) VALUES "
			+ " (?, ?, ?, ?, ?, ?, ?, ?);";
	// Select all the deposits and withdraw transactions for a specific user
	private static final String SELECT_ALL_DEPOSITS_AND_WITHDRAW = "select transaction_name, transaction_date, transaction_time, dollar_amount, PPS_amount from transactions where transaction_from_email = ? and transaction_from_email = transaction_to_email";
	private static final String SELECT_ALL_TRANSACTIONS  = "select * from transactions;";
	private static final String DELETE_ALL_TRANSACTIONS_SQL  = "delete * from transactions;";
	private static final String SELECT_ALL_TRANSACTIONS_BY_USER_SQL = "select transaction_name, transaction_date, transaction_time, dollar_amount, PPS_amount, transaction_from_email, transaction_to_email, PPS_Price from transactions where transaction_from_email = ? or transaction_to_email = ?";
	

	public Transactions_DAO() {
    }
	
    protected void connect_func() throws SQLException {
        if (connect == null || connect.isClosed()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            connect = (Connection) DriverManager
    			      .getConnection("jdbc:mysql://127.0.0.1:3306/PPS_DB?serverTimezone=UTC"
    			          + "&useSSL=false&user=john&password=john1234");

            System.out.println(connect);
        }
    }
    
    protected void disconnect() throws SQLException {
        if (connect != null && !connect.isClosed()) {
        	connect.close();
        }
    }
    
 // Insert transaction
    public boolean insertTransaction(Transactions transaction) throws SQLException {
		System.out.println(INSERT_TRANSCATIONS_SQL);
		// Step 1: Establishing a Connection
		connect_func();
		
		// Step 2:Create a statement using connection object
		preparedStatement = connect.prepareStatement(INSERT_TRANSCATIONS_SQL);
		preparedStatement.setString(1, transaction.getTransaction_name());
		preparedStatement.setString(2, transaction.getTransaction_date());
		preparedStatement.setString(3, transaction.getTransaction_time());
		preparedStatement.setDouble(4, transaction.getDollar_amount());
		preparedStatement.setDouble(5, transaction.getPPS_amount());
		preparedStatement.setString(6, transaction.getTransaction_from_email());
		preparedStatement.setString(7, transaction.getTransaction_to_email());
		preparedStatement.setDouble(8, transaction.getPPS_price());
		System.out.println(preparedStatement);
		
		// Step 3: Execute the query or update query		
		boolean rowInserted = preparedStatement.executeUpdate() > 0;
	    //preparedStatement.close();
        disconnect();
	    return rowInserted;		 	
	}
    
    // Get all deposit and withdraw activities for a certain user
    public List<Transactions> getDepositWithdrawTransactions(String user_email) throws SQLException {
    	
    	List<Transactions> TransactionsList = new ArrayList<Transactions>(); 
         
        connect_func();
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_ALL_DEPOSITS_AND_WITHDRAW);
        preparedStatement.setString(1, user_email);
         
        ResultSet resultSet = preparedStatement.executeQuery();
         
        while (resultSet.next()) {
            String transaction_name = resultSet.getString("transaction_name");
            String transaction_date = resultSet.getString("transaction_date");
            String transaction_time = resultSet.getString("transaction_time");
            double dollar_amount = resultSet.getDouble("dollar_amount");
            int PPS_amount = resultSet.getInt("PPS_amount");
            
            Transactions transaction = new Transactions(transaction_name,transaction_date,transaction_time,dollar_amount, PPS_amount);
            TransactionsList.add(transaction);
        }
         
        resultSet.close();
        statement.close();
         
        return TransactionsList;
    }
    
 // Get all user activities
    public List <Transactions> getAllTransactionsOfUser(String user_email) throws SQLException {
    	
    	List<Transactions> TransactionsList = new ArrayList <Transactions>(); 
         
        connect_func();
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_ALL_TRANSACTIONS_BY_USER_SQL);
        preparedStatement.setString(1, user_email);
        preparedStatement.setString(2, user_email);
         
        ResultSet resultSet = preparedStatement.executeQuery();
         
        while (resultSet.next()) {
            String transaction_name = resultSet.getString("transaction_name");
            String transaction_date = resultSet.getString("transaction_date");
            String transaction_time = resultSet.getString("transaction_time");
            double dollar_amount = resultSet.getDouble("dollar_amount");
            int PPS_amount = resultSet.getInt("PPS_amount");
            String from_email = resultSet.getString("transaction_from_email");
            String to_email = resultSet.getString("transaction_to_email");
            double PPS_price = resultSet.getDouble("PPS_Price");
            
            System.out.println("transaction_name: " + transaction_name);
            Transactions transaction = new Transactions(transaction_name,transaction_date,transaction_time,dollar_amount, PPS_amount, from_email, to_email, PPS_price);
            TransactionsList.add(transaction);
        }
         
        resultSet.close();
        //statement.close();
         
        return TransactionsList;
    }
}
