package PPS_Project.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import PPS_Project.bean.Follows;
import PPS_Project.bean.Transactions;
import PPS_Project.bean.User;

public class Root_part3_DAO {
	
	private static final long serialVersionUID = 1L;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	
	// ALL SQL QUERIRES
	private static final String SELECT_ALL_USERS_EMAILS  = "select email from users;";
	private static final String SELECT_COMMON_FOLLOWERS  = "Select f1.follower_email \n"
			+ "from follows f1, follows f2\n"
			+ "where f1.follower_email = f2.follower_email and f1.following_email = ? and f2.following_email = ?;";
private static final  String select_biggest_buy="select * from transactions where transaction_name='buy'and  PPS_amount>=all(select PPS_amount from transactions)";
	
	private static final  String select_biggest_buyer="select  transaction_to_email,sum(PPS_amount)from transactions where transaction_name='buy' group by transaction_to_email having sum(PPS_amount)>=all(select sum(PPS_amount) from transactions where transaction_name='buy' group by transaction_to_email)";
	private static final  String  select_neverbuy_user="select * from transactions where transaction_name='transfer' and transaction_to_email not in(select transaction_to_email from transactions where transaction_name='buy') group by transaction_to_email";
	
	private static final String select_neversell_user="select * from transactions where transaction_name='buy' and transaction_to_email not in(select transaction_from_email from transactions where transaction_name='sell') group by transaction_to_email";
	
	private static final String select_inactive_user="select * from users where email not in ((select transaction_from_email from transactions)union (select transaction_to_email from transactions))";
	public Root_part3_DAO() {
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
    
 // Retrieve all users' emails
    public List<User> allUsersEmails () throws SQLException {
		List<User> userEmailsList = new ArrayList<User>(); 
        
        connect_func();
         
        // Step 2:Create a statement using connection object
        statement =  (Statement) connect.createStatement();
        
        // Step 3: execute
        resultSet = statement.executeQuery(SELECT_ALL_USERS_EMAILS); 
         
        while (resultSet.next()) {
        	// Don't show the root in the select list 
        	if(!resultSet.getString("email").equalsIgnoreCase("root")) {
        		String email = resultSet.getString("email");
            	User user = new User(email);
            	userEmailsList.add(user);
        	}	
        }
         
        resultSet.close();
        return userEmailsList;
	}
    
    // Retrieve all users emails
    public List <String> commonFollowersEmails (String firstUserEmail, String secondUserEmail) throws SQLException {
        List<String> commonFollowersList = new ArrayList<String>(); 
        
        connect_func();
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_COMMON_FOLLOWERS);
        preparedStatement.setString(1, firstUserEmail);
        preparedStatement.setString(2, secondUserEmail);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
         
        while (resultSet.next()) {
            String follower_email = resultSet.getString("follower_email");
            commonFollowersList.add(follower_email);
        }
         
        resultSet.close();
        //statement.close();
         
        return commonFollowersList;
	}
    
    
    
    
public List <Transactions> get_Biggest_Buy() throws SQLException {
    	
    	List<Transactions> biggestbuyList = new ArrayList <Transactions>(); 
         
        connect_func();
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(select_biggest_buy);
       
         
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
           biggestbuyList.add(transaction);
        }
         
        resultSet.close();
        //statement.close();
         
        return biggestbuyList;
    }
    
    
    
    public List <Transactions> get_Biggest_Buyer() throws SQLException {
    	
    	List<Transactions> biggestbuyList = new ArrayList <Transactions>(); 
         
        connect_func();
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(select_biggest_buyer);
       
         
        ResultSet resultSet = preparedStatement.executeQuery();
         
        while (resultSet.next()) {
      
            String to_email = resultSet.getString("transaction_to_email");
           int sum_pps=resultSet.getInt("sum(PPS_amount)");
            
            System.out.println("get biggest buyer");
            Transactions transaction = new Transactions(to_email,sum_pps);
           biggestbuyList.add(transaction);
        }
         
        resultSet.close();
        //statement.close();
         
        return biggestbuyList;
    }
    
 public List <Transactions> get_neverbuy_user() throws SQLException {
    	
    	List<Transactions> biggestbuyList = new ArrayList <Transactions>(); 
         
        connect_func();
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(select_neverbuy_user);
       
         
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
           biggestbuyList.add(transaction);
        }
        
        
        
         
        resultSet.close();
        //statement.close();
         
        return biggestbuyList;
    }
     
 public List <Transactions> get_neversell_user() throws SQLException {
 	
 	List<Transactions> biggestbuyList = new ArrayList <Transactions>(); 
      
     connect_func();
      
     preparedStatement = (PreparedStatement) connect.prepareStatement(select_neversell_user);
    
      
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
        biggestbuyList.add(transaction);
     }
     
     
     
      
     resultSet.close();
     //statement.close();
      
     return biggestbuyList;
 }
    
    
    
    
    
 
 public List <User> getAllInactiveUser( ) throws SQLException {
 	
 	List<User> UserList = new ArrayList <User>(); 
      
     connect_func();
      
     preparedStatement = (PreparedStatement) connect.prepareStatement(select_inactive_user);
    
      
     ResultSet rs = preparedStatement.executeQuery();
      
     while (rs.next()) {
         String email= rs.getString("email");
         String password = rs.getString("pass");
         String fname = rs.getString("fname");
			String lname = rs.getString("lname");
			String address = rs.getString("address");
			String dob = rs.getString("dob");
			long PPS_balance = rs.getLong("PPS_balance");
			double dollar_balance = rs.getDouble("dollar_balance");
			
			
			User user = new User(email, password, fname, lname, address, dob, PPS_balance,dollar_balance);
			
         
        UserList.add(user);
     }
      
rs.close();
     //statement.close();
      
     return UserList;
 }
    
    
    

}
