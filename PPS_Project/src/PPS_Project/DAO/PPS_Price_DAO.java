package PPS_Project.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import PPS_Project.bean.PPS_Price;
import PPS_Project.bean.User;

public class PPS_Price_DAO {
	
	private static final long serialVersionUID = 1L;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	
	// ALL SQL QUERIRES
    private static final String SELECT_PPS_PRICE = "select price from PPS_price";
	private static final String UPDATE_PPS_PRICE  = "update PPS_price set price = ?"
			+ "";
	

	public PPS_Price_DAO() {
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
    
    // update the pps price
    public boolean update_PPS_price(PPS_Price pps_price) throws SQLException {
        connect_func();
        
        preparedStatement = (PreparedStatement) connect.prepareStatement(UPDATE_PPS_PRICE);
        preparedStatement.setDouble(1, pps_price.getPrice());

         
        boolean rowUpdated = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        disconnect();
        return rowUpdated;     
    }
    
    // Get the latest pps price
    public PPS_Price getLastest_PPS_price() throws SQLException {
    	PPS_Price pps_price = null;
        connect_func();      
        statement =  (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_PPS_PRICE);
         
        if (resultSet.next()) {
            double price = resultSet.getDouble("price");
            pps_price = new PPS_Price(price);
        }        
        
        resultSet.close();
        statement.close();         
        disconnect();        
        return pps_price;
        
    
    }
    
 


}
