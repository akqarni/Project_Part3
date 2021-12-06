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
	private static final String SELECT_FREQUENT_USERS_EMAILS = "select transaction_to_email, count(transaction_name) from transactions \n"
			+ "where transaction_name = 'buy'\n"
			+ "group by transaction_to_email \n"
			+ "having count(transaction_name) >= all (\n"
			+ "select count(transaction_name) from transactions\n"
			+ "where transaction_name='buy' \n"
			+ "group by transaction_to_email\n"
			+ ");";
	private static final String SELECT_ALL_USERS_EMAILS  = "select email from users;";
	
	private static final String SELECT_COMMON_FOLLOWERS  = "Select f1.follower_email \n"
			+ "from follows f1, follows f2\n"
			+ "where f1.follower_email = f2.follower_email and f1.following_email = ? and f2.following_email = ?;";
	
    private static final String SELECT_BIGGEST_BUY   ="select * from transactions "
    		+ "where transaction_name='buy' and  PPS_amount >= all(select PPS_amount from transactions)";
    
	private static final String SELECT_BIGGEST_BUYERS = "select transaction_to_email, sum(PPS_amount) from transactions\n"
			+ "where transaction_name='BUY' \n"
			+ "group by transaction_to_email \n"
			+ "having sum(PPS_amount) >= \n"
			+ "all(select sum(PPS_amount) from transactions where transaction_name='BUY' group by transaction_to_email);";
	
	private static final String SELECT_NEVERBUY_USERS = "select * from transactions\n"
			+ "where transaction_name='transfer' and transaction_to_email not in \n"
			+ "(select transaction_to_email from transactions where transaction_name='BUY') \n"
			+ "group by transaction_to_email;";
	
	private static final String SELECT_NEVERSELL_USERS = "select * from transactions\n"
			+ "where transaction_name = 'BUY' and transaction_to_email not in\n"
			+ "(select transaction_from_email from transactions where transaction_name='SELL') \n"
			+ "group by transaction_to_email";
	
	private static final String SELECT_INACTIVE_USERS = "select * from users "
			+ "where email not in "
			+ "((select transaction_from_email from transactions) union (select transaction_to_email from transactions))";
	
	private static final String SELECT_LUCKY_USERS_EMAILS = "SELECT DISTINCT transaction_to_email from transactions t, follows f  \n"
			+ "WHERE t.transaction_name = \"TRANSFER\" and NOT EXISTS  (\n"
			+ "select follower_email from follows where t.transaction_name = \"TRANSFER\" and follower_email not in \n"
			+ "           (select follower_email from transactions t1, follows f1\n"
			+ "            where t.transaction_to_email = t1.transaction_to_email)\n"
			+ ");";
	
	private static final String SELECT_POPULAR_USERS_EMAILS = "select following_email from follows\n"
			+ "group by following_email \n"
			+ "having count(follower_email) >= 5;";
	
	// Statistics 
	private static final String SELECT_BUYS_COUNT = "Select transaction_name, count(transaction_ID) as numberOfBuys from transactions\n"
			+ "where transaction_name = \"BUY\"\n"
			+ "group by transaction_name;";
	private static final String SELECT_SELLS_COUNT = "Select transaction_name, count(transaction_ID) as numberOfSells from transactions\n"
			+ "where transaction_name = \"SELL\"\n"
			+ "group by transaction_name;";
	private static final String SELECT_TRANSFERS_COUNT = "Select transaction_name, count(transaction_ID) as numberOfTransfers from transactions\n"
			+ "where transaction_name = \"TRANSFER\"\n"
			+ "group by transaction_name;";
	private static final String SELECT_DEPOSITS_COUNT = "Select transaction_name, count(transaction_ID) as numberOfDeposits from transactions\n"
			+ "where transaction_name = \"DEPOSIT\"\n"
			+ "group by transaction_name;";
	private static final String SELECT_WITHDRAWS_COUNT = "Select transaction_name, count(transaction_ID) as numberOfWithdraws from transactions\n"
			+ "where transaction_name = \"WITHDRAW\"\n"
			+ "group by transaction_name;";
	
	
	
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
    
  // List the users who performed the most number of buyers (not amount but most frequent).
    public List<User> allFrequentUsersEmails () throws SQLException {
    	List<User> frequentUsersEmails = new ArrayList <User>(); 
       
       connect_func();
        
       // Step 2:Create a statement using connection object
       statement =  (Statement) connect.createStatement();
       
       // Step 3: execute
       resultSet = statement.executeQuery(SELECT_LUCKY_USERS_EMAILS); 
        
       while (resultSet.next()) {
      	String email = resultSet.getString("transaction_to_email");
      	User user = new User(email);
        frequentUsersEmails.add(user);
       }
        
       resultSet.close();
       return frequentUsersEmails;
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
    
  //List all the users such that every follower has sent him/her some money
    public List<User> allLuckyUsers () throws SQLException {
    		List<User> luckyUsersEmailsList = new ArrayList<User>(); 
       
       connect_func();
        
       // Step 2:Create a statement using connection object
       statement =  (Statement) connect.createStatement();
       
       // Step 3: execute
       resultSet = statement.executeQuery(SELECT_LUCKY_USERS_EMAILS); 
        
       while (resultSet.next()) {
      	String email = resultSet.getString("transaction_to_email");
        	User user = new User(email);
        	luckyUsersEmailsList.add(user);
       }
        
       resultSet.close();
       return luckyUsersEmailsList;
    }
    
  // List those users that are followed by at least 5 followers. 
    public List<User> allPopularUsers () throws SQLException {
    		List<User> popularUsersEmailsList = new ArrayList<User>(); 
       
       connect_func();
        
       // Step 2:Create a statement using connection object
       statement =  (Statement) connect.createStatement();
       
       // Step 3: execute
       resultSet = statement.executeQuery(SELECT_POPULAR_USERS_EMAILS); 
        
       while (resultSet.next()) {
      	String email = resultSet.getString("following_email");
        	User user = new User(email);
        	popularUsersEmailsList.add(user);
       }
        
       resultSet.close();
       return popularUsersEmailsList;
    }
    
    
    public List <Transactions> get_Biggest_Buy() throws SQLException {
    	
    	List<Transactions> biggestbuyList = new ArrayList <Transactions>(); 
         
        connect_func();
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_BIGGEST_BUY);
       
         
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
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_BIGGEST_BUYERS);
       
         
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
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_NEVERBUY_USERS);
       
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
          
         preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_NEVERSELL_USERS);
        
          
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
        preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_INACTIVE_USERS);    
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
   			
   			if(!email.equalsIgnoreCase("root")) {
   				User user = new User(email, password, fname, lname, address, dob, PPS_balance,dollar_balance);
   		        UserList.add(user);
   		        }
   			}
         rs.close();
        //statement.close(); 
        return UserList;
        }
    
    public String [][] systemStatistics( ) throws SQLException {
      	 String [][] statistics = new String [5][2]; 
            
           connect_func();  
           
           // BUY count
           preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_BUYS_COUNT);    
           ResultSet rs = preparedStatement.executeQuery();
           
           if (rs.next()) {
               String transaction_name = rs.getString("transaction_name");
               int transaction_count = rs.getInt("numberOfBuys");
               
               statistics [0][0] = transaction_name;
               statistics [0][1] = Integer.toString(transaction_count);
           } else {
        	   statistics [0][0] = "BUY";
               statistics [0][1] = "0"; 
           }
           rs.close();
           
           // SELL count
           preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_SELLS_COUNT);    
           ResultSet rs1 = preparedStatement.executeQuery();
           
           if (rs1.next()) {
               String transaction_name = rs1.getString("transaction_name");
               int transaction_count = rs1.getInt("numberOfSells");
               
               statistics [1][0] = transaction_name;
               statistics [1][1] = Integer.toString(transaction_count);
           } else {
        	   statistics [1][0] = "SELL";
               statistics [1][1] = "0"; 
           }
           rs1.close();
           
           // TRANSFER count
           preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_TRANSFERS_COUNT);    
           ResultSet rs2 = preparedStatement.executeQuery();
           
           if (rs2.next()) {
               String transaction_name = rs2.getString("transaction_name");
               int transaction_count = rs2.getInt("numberOfTransfers");
               
               statistics [2][0] = transaction_name;
               statistics [2][1] = Integer.toString(transaction_count);
           } else {
        	   statistics [2][0] = "TRANSFER";
               statistics [2][1] = "0"; 
           }
           rs2.close();
           
           // DEPOSIT count
           preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_DEPOSITS_COUNT);    
           ResultSet rs3 = preparedStatement.executeQuery();
           
           if (rs3.next()) {
               String transaction_name = rs3.getString("transaction_name");
               int transaction_count = rs3.getInt("numberOfDeposits");
               
               statistics [3][0] = transaction_name;
               statistics [3][1] = Integer.toString(transaction_count);
           } else {
        	   statistics [3][0] = "DEPOSIT";
               statistics [3][1] = "0"; 
           }
           rs3.close();
           
           // WITHDRAW count
           preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_WITHDRAWS_COUNT);    
           ResultSet rs4 = preparedStatement.executeQuery();
           
           if (rs4.next()) {
               String transaction_name = rs4.getString("transaction_name");
               int transaction_count = rs4.getInt("numberOfWithdraws");
               
               statistics [4][0] = transaction_name;
               statistics [4][1] = Integer.toString(transaction_count);
           } else {
        	   statistics [4][0] = "WITHDRAW";
               statistics [4][1] = "0"; 
           }
           rs4.close();
           
           //statement.close(); 
           return statistics;
           }
    
    
 
    
 
 
 
 
 
 
}
