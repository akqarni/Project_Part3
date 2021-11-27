package PPS_Project.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Root_part3_DAO {
	
	private static final long serialVersionUID = 1L;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	
	// ALL SQL QUERIRES
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (email, pass, fname, lname, address, dob, PPS_balance, dollar_balance) VALUES "
			+ " (?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String SELECT_USER_BY_ID = "select email, pass, fname, lname, address, dob, PPS_balance, dollar_balance from users where email = ?";
	private static final String SELECT_ALL_USERS  = "select * from users;";
	private static final String DELETE_USERS_SQL  = "delete from users where email = ?;";
	private static final String DELETE_ALL_USERS_SQL  = "delete * from users;";
	private static final String UPDATE_USER_DOLLAR_BALANCE_SQL  = "update users set dollar_balance = ? where email = ?;";
	private static final String UPDATE_USER_PPS_BALANCE_BY_ID = "update users set PPS_balance = ? where email = ?";
	private static final String VALIDATE_USER_SQL = "select fname, lName, address, dob, PPS_balance, dollar_balance from users where email = ? and pass = ?";
	
	
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

}
