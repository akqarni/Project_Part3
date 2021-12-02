package PPS_Project.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;

import PPS_Project.bean.User;

public class User_DAO {
	
	private static final long serialVersionUID = 1L;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	
	// ALL SQL QUERIRES
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (email, pass, fname, lname, address, dob, PPS_balance, dollar_balance) VALUES "
			+ " (?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String SELECT_USER_BY_ID = "select email, pass, fname, lname, address, dob, PPS_balance, dollar_balance, setting from users where email = ?";
	private static final String SELECT_ALL_USERS  = "select * from users;";
	private static final String DELETE_USERS_SQL  = "delete from users where email = ?;";
	private static final String DELETE_ALL_USERS_SQL  = "delete * from users;";
	private static final String UPDATE_USER_DOLLAR_BALANCE_SQL  = "update users set dollar_balance = ? where email = ?;";
	private static final String UPDATE_USER_PPS_BALANCE_BY_ID = "update users set PPS_balance = ? where email = ?";
	private static final String UPDATE_USER_SETTINGS_BY_ID = "update users set setting = ? where email = ?";
	private static final String VALIDATE_USER_SQL = "select fname, lName, address, dob, PPS_balance, dollar_balance, setting from users where email = ? and pass = ?";
	
	
	public User_DAO() {
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
    
    // Insert user
    public boolean insertUser(User user) throws SQLException {
		System.out.println(INSERT_USERS_SQL);
		// Step 1: Establishing a Connection
		connect_func();
		
		// Step 2:Create a statement using connection object
		preparedStatement = connect.prepareStatement(INSERT_USERS_SQL);
		preparedStatement.setString(1, user.getUser_email());
		preparedStatement.setString(2, user.getUser_password());
		preparedStatement.setString(3, user.getUser_fname());
		preparedStatement.setString(4, user.getUser_lname());
		preparedStatement.setString(5, user.getUser_address());
		preparedStatement.setString(6, user.getUser_dob());
		preparedStatement.setLong(7, user.getPPS_balance());
		preparedStatement.setDouble(8, user.getDollar_balance());
		System.out.println(preparedStatement);
		
		// Step 3: Execute the query or update query		
		boolean rowInserted = preparedStatement.executeUpdate() > 0;
	    //preparedStatement.close();
        disconnect();
	    return rowInserted;		 	
	}
    
    // Select user by id
    public User selectUser(String email) throws SQLException {
		User user = null;
		
		// Step 1: Establishing a Connection
		connect_func();
		
		// Step 2:Create a statement using connection object
		preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_USER_BY_ID);
		preparedStatement.setString(1, email);
		System.out.println(preparedStatement);
		
		// Step 3: Execute the query or update query
		ResultSet rs = preparedStatement.executeQuery();
		
		// Step 4: Process the ResultSet object.
		while (rs.next()) {
			String password = rs.getString("pass");
			String fname = rs.getString("fname");
			String lname = rs.getString("lname");
			String address = rs.getString("address");
			String dob = rs.getString("dob");
			long PPS_balance = rs.getLong("PPS_balance");
			double dollar_balance = rs.getDouble("dollar_balance");
			String setting = rs.getString("setting");
			
			System.out.println("email: "+ email+ "password: "+ password);
			
			user = new User(email, password, fname, lname, address, dob, PPS_balance,dollar_balance, setting);
			
		}
		
		
		
		rs.close();
        //statement.close();
		return user;
	}
    
 // validate user
    public User validateUser(String user_email, String user_password) throws SQLException {
		
		// Step 1: Establishing a Connection
		connect_func();
				
		// Step 2:Create a statement using connection object
		preparedStatement = (PreparedStatement) connect.prepareStatement(VALIDATE_USER_SQL);
		preparedStatement.setString(1, user_email);
		preparedStatement.setString(2, user_password);
		System.out.println(preparedStatement);
		
		// Step 3: Execute the query or update query
		ResultSet rs = preparedStatement.executeQuery();
		
		// Step 4: Process the ResultSet object.
		if (rs.next()) {
			String fname = rs.getString("fname");
			String lname = rs.getString("lname");
			String address = rs.getString("address");
			String dob = rs.getString("dob");
			long PPS_balance = rs.getLong("PPS_balance");
			double dollar_balance = rs.getDouble("dollar_balance");
			String setting = rs.getString("setting");
			System.out.println("email: "+ user_email+ "password: "+ user_password);
			
			User user = new User(user_email, user_password, fname, lname, address, dob, PPS_balance,dollar_balance, setting);
			return user;
			
		}
		else {
			return null;
		}
	}
        
    // Delete user
    public boolean deleteUser (String email) throws SQLException {
    	// Step 1: Establishing a Connection
    	connect_func();
        
        // Step 2:Create a statement using connection object
        preparedStatement = (PreparedStatement) connect.prepareStatement(DELETE_USERS_SQL);
        preparedStatement.setString(1, email);
         
        boolean rowDeleted = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        
        return rowDeleted;     
    }
    
    // Delete all users
    public void deleteAllUsers () throws SQLException {
    	// Step 1: Establishing a Connection
    	connect_func();
        
        // Step 2:Create a statement using connection object
        statement =  (Statement) connect.createStatement();
        
        // Step 3: execute
        statement.executeQuery(DELETE_ALL_USERS_SQL); 
        
    }
    
    // Update user dollar balance
    public boolean updateUserDollarBalance(User user) throws SQLException {
    	// Step 1: Establishing a Connection
    	connect_func();
        System.out.println("inside User_DAO: user.getDollar_balance(): "+ user.getDollar_balance());
        //System.out.println("inside User_DAO: dollar_amount from form: "+ dollar_amount);
    	//double newBalance = user.getDollar_balance() + dollar_amount;
    	// Step 2: Create a statement using connection object
        preparedStatement = (PreparedStatement) connect.prepareStatement(UPDATE_USER_DOLLAR_BALANCE_SQL);
        preparedStatement.setDouble(1, user.getDollar_balance());
        preparedStatement.setString(2, user.getUser_email());
        System.out.println(preparedStatement);
         
        boolean rowUpdated = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        return rowUpdated;     
    }
    
    public boolean updateUserPPSBalance(User user) throws SQLException {
    	// Step 1: Establishing a Connection
    	connect_func();
        System.out.println("inside User_DAO: user.getPPS_balance(): "+ user.getPPS_balance());
        
        // System.out.println("inside User_DAO: dollar_amount from form: "+ dollar_amount);
    	// double newBalance = user.getDollar_balance() + dollar_amount;
        
    	// Step 2: Create a statement using connection object
        preparedStatement = (PreparedStatement) connect.prepareStatement(UPDATE_USER_PPS_BALANCE_BY_ID);
        preparedStatement.setLong(1, user.getPPS_balance());
        preparedStatement.setString(2, user.getUser_email());
        System.out.println(preparedStatement);
         
        boolean rowUpdated = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        return rowUpdated;		
    }
    
    public boolean updateUserSetting(User user) throws SQLException {
    	// Step 1: Establishing a Connection
    	connect_func();
        System.out.println("inside User_DAO: user.getUser_settings(): "+ user.getUser_settings());
        
        
    	// Step 2: Create a statement using connection object
        preparedStatement = (PreparedStatement) connect.prepareStatement(UPDATE_USER_SETTINGS_BY_ID);
        preparedStatement.setString(1, user.getUser_settings());
        preparedStatement.setString(2, user.getUser_email());
        System.out.println(preparedStatement);
         
        boolean rowUpdated = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        return rowUpdated;		
    }

    
}
