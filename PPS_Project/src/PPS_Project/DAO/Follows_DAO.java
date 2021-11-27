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

public class Follows_DAO {
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	// ALL SQL QUERIRES
		private static final String INSERT_FOLLOWS_SQL = "INSERT INTO follows" + "  (follower_email, following_email) VALUES "
				+ " (?, ?);";
		private static final String SELECT_FOLLOWINGS_BY_FOLLOWER_ID = "select following_email from follows where follower_email = ?";
		private static final String SELECT_FOLLOWERS_BY_FOLLOWING_ID = "select follower_email from follows where following_email = ?";
		private static final String SELECT_ALL_FOLLOWS  = "select * from follows;";
		private static final String DELETE_FOLLOWER_SQL  = "delete from follows where email = follower_email;";
		//private static final String DELETE_ALL_USERS_SQL  = "delete * from users;";
		
	
	public Follows_DAO(){
		
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
    
 // Insert follow
    public boolean insertFollow(Follows follow) throws SQLException {
		System.out.println(INSERT_FOLLOWS_SQL);
		// Step 1: Establishing a Connection
		connect_func();
		
		// Step 2:Create a statement using connection object
		preparedStatement = connect.prepareStatement(INSERT_FOLLOWS_SQL);
		preparedStatement.setString(1, follow.getFollower_email());
		preparedStatement.setString(2, follow.getFollowing_email());
	
		System.out.println(preparedStatement);
		
		// Step 3: Execute the query or update query		
		boolean rowInserted = preparedStatement.executeUpdate() > 0;
	    //preparedStatement.close();
        disconnect();
	    return rowInserted;		 	
	}
    
 // Select all followings by follower id
    public List<Follows> selectALLFollowingsByFollowerID (String follower_email) throws SQLException {
    	List<Follows> FollowingsList = new ArrayList<Follows>(); 
        
        connect_func();
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_FOLLOWINGS_BY_FOLLOWER_ID);
        preparedStatement.setString(1, follower_email);
         
        ResultSet resultSet = preparedStatement.executeQuery();
         
        while (resultSet.next()) {
            String following_email = resultSet.getString("following_email");
           
            
            Follows follow = new Follows(follower_email, following_email);
            FollowingsList.add(follow);
        }
         
        resultSet.close();
        statement.close();
         
        return FollowingsList;
	}
    
 // Select all followers by following id
    public List<Follows> selectALLFollowersByFollowingID (String following_email) throws SQLException {
    	List<Follows> FollowingsList = new ArrayList<Follows>(); 
        
        connect_func();
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_FOLLOWERS_BY_FOLLOWING_ID);
        preparedStatement.setString(1, following_email);
         
        ResultSet resultSet = preparedStatement.executeQuery();
         
        while (resultSet.next()) {
            String follower_email = resultSet.getString("follower_email");
           
            
            Follows follow = new Follows(follower_email, following_email);
            FollowingsList.add(follow);
        }
         
        resultSet.close();
        statement.close();
         
        return FollowingsList;
	}

}
