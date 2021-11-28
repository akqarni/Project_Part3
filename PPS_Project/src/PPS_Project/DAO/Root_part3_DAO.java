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
    
    

}
