package PPS_Project.web.servlets;

import java.sql.SQLException;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class project1 {
	
	
	
	

	/**
	 * 
	 * @throws SQLException
	 */
	
	private static final long serialVersionUID = 1L;
	private Connection connect = null;
	private Connection connect1 = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	

    
	
	
    public void part1() throws SQLException {
    	

    	try {
    		
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
        
    
    	

    	statement = connect.createStatement();
    	
    	// Create all the tables statements
    	String createUsersTable_SQL = "CREATE TABLE IF NOT EXISTS users (\r\n"
    			+ "    email varchar(50),\r\n"
    			+ "    pass varchar(50),\r\n"
    			+ "    fname varchar(50),\r\n"
    			+ "    lname varchar(50),\r\n"
    			+ "    address varchar(100),\r\n"
    			+ "    dob date,\r\n"
    			+ "    PPS_balance long,\r\n"
    			+ "    dollar_balance DOUBLE,\r\n"
    			+ "    setting varchar(10),\r\n"
    			+ "    primary key (email)\r\n"
    			+ ")";
    	
    	
    	String createFollowTable_SQL = "CREATE TABLE IF NOT EXISTS follows (\r\n"
    			+ "    follower_email varchar(50),\r\n"
    			+ "    following_email varchar(50),\r\n"
    			+ "    primary key (follower_email, following_email),\r\n"
    			+ "    foreign key (follower_email) references users(email),\r\n"
    			+ "    foreign key (following_email) references users(email)\r\n"
    			+ ");";
    	
    	String createTransactionsTable_SQL = "CREATE TABLE IF NOT EXISTS transactions (\r\n"
    			+ "    transaction_ID Integer auto_increment,\r\n"
    			+ "    transaction_name varchar(20),\r\n"
    			+ "    transaction_date date,\r\n"
    			+ "    transaction_time time,\r\n"
    			+ "    dollar_amount Double,\r\n"
    			+ "    PPS_amount int,\r\n"
    			+ "    transaction_from_email varchar(50) not null,\r\n"
    			+ "    transaction_to_email varchar(50) not null,\r\n"
    			+ "    PPS_Price double,\r\n"
    			+ "    primary key (transaction_ID),\r\n"
    			+ "    foreign key (transaction_from_email) references users(email),\r\n"
    			+ "    foreign key (transaction_to_email) references users (email)\r\n"
    			+ ")";
    	
    	String createPPS_priceTable_SQL ="CREATE TABLE IF NOT EXISTS PPS_price " +
                "(price double)" ;
    	
    	
    	// Drop all tables
    	String dropUserTable_SQL="delete from users where email <> 'root'";
    	String dropTransactionsTable_SQL = "drop table if exists transactions";
    	String dropFollowTable_SQL = "drop table if exists follows";
    	String dropPPS_priceTable_SQL = "drop table if exists PPS_price";
    	
    	// Intialize all tables
    	String intializeUserTable = "insert into users (email,pass,fname,lname,address,dob,PPS_balance,dollar_balance, setting) values"
    			+ "('jbjb@yahoo.com','1234','Sahr','Ahmed','Saudi Arabia, Mecca 24250','2012-11-12',0,0,10111111),"
    			+ "('effat@gmail.com','1234','Effat','Fazel','Dearborn, Michigan 48183','2012-11-12',0,0, 10111111),"
    			+ "('jbxsjb@gmail.com','1234','Reem','Alqarni','Canton, Michigan 48183','2012-11-12',0,0, 10111111),"
    			+ "('gdfgd@gmail.com','1234','Rami','Saad','Livonea, Michigan 48183','2012-11-12',0,0, 10111111),"
    			+ "('rofdgfgot@gmail.com','1234','Saad','AlAli','Dearborn, Michigan 48183','2012-11-12',0,0, 10111111),"
    			+ "('bit@gmail.com','1234','Afnan','Alqarni','Taylor, Michigan 48183','2012-11-12',0,0, 10111111),"
    			+ "('tre@gmail.com','1234','Sara','Alix','Athens, Ohio 44341','2012-11-12',0,0, 10111111),"
    			+ "('ret@gmail.com','1234','Ali','Alzh','Athens, Ohio 44341','2012-11-12',0,0, 10111111),"
    			+ "('roofwert@gmail.com','1234','Ahmed','Saleh','Athens, Ohio 44341','2012-11-12',0,0, 10111111),"
    			+ "('ef@gmail.com','1234','Roaa','Ali','Jeddah, Saudi Arabia 44341','2012-01-12',0,0, 10111111);";
    	
    	String intializeFollowTable = "insert into follows(follower_email,following_email) values"
    			+ "('ef@gmail.com','ret@gmail.com'), "
    			+ "('bit@gmail.com','ret@gmail.com'),"
    			+ "('gdfgd@gmail.com','ret@gmail.com'),"
    			+ "('bit@gmail.com','tre@gmail.com'),"
    			+ "('rofdgfgot@gmail.com','ef@gmail.com'),"
    			+ "('effat@gmail.com','bit@gmail.com'),"
    			+ "('effat@gmail.com','tre@gmail.com')";
    	
    	String intializeTransactionsTable = "INSERT INTO transactions (transaction_name,transaction_date,transaction_time,dollar_amount,PPS_amount,transaction_from_email,transaction_to_email) VALUES "
    			+ "('effat',  '2018-11-12','13:30',1200,1234,'gdfgd@gmail.com','ret@gmail.com')"
    			+ ",('dnn',  '2018-11-12','13:30',1200,1234,'ef@gmail.com','ret@gmail.com')"
    			+ ",('uiopeffat',  '2018-11-12','13:30',1200,1234,'ef@gmail.com','ret@gmail.com')"
    			+ ",('eerteffat',  '2018-11-12','13:30',1200,1234,'ef@gmail.com','ret@gmail.com')"
    			+ ",('wqereffat',  '2018-11-12','13:30',1200,1234,'ef@gmail.com','ret@gmail.com')"
    			+ ",('iuyeffat',  '2018-11-12','13:30',1200,1234,'ef@gmail.com','ret@gmail.com')"
    			+ ",('treeffat',  '2018-11-12','13:30',1200,1234,'ef@gmail.com','ret@gmail.com')"
    			+ ",('fwegeffat',  '2018-11-12','13:30',1200,1234,'ef@gmail.com','ret@gmail.com')"
    			+ ",('effat',  '2018-11-12','13:30',1200,1234,'ef@gmail.com','ret@gmail.com')"
    			+ ",('ijijeffat',  '2018-11-12','13:30',1200,1234,'ef@gmail.com','ret@gmail.com')";
    	
    	String intializePPS_priceTable = "insert into PPS_price (price) values (0.000001);";
    	
    
    	// Execute all drop statements
      	statement.executeUpdate(dropPPS_priceTable_SQL);
   	    statement.executeUpdate(dropTransactionsTable_SQL);
   	    statement.executeUpdate(dropFollowTable_SQL);
   	    statement.executeUpdate(dropUserTable_SQL);
       	
   	    // Execute all create statements
       	statement.executeUpdate(createUsersTable_SQL);
       	statement.executeUpdate(createTransactionsTable_SQL);
        statement.executeUpdate(createFollowTable_SQL);
        statement.executeUpdate(createPPS_priceTable_SQL);
        
        // Execute all intialization statements
        statement.executeUpdate(intializeUserTable);
      	statement.executeUpdate(intializeFollowTable);
      	//statement.executeUpdate(intializeTransactionsTable);
      	statement.executeUpdate(intializePPS_priceTable);
    

    	
    	
    }
    	catch (Exception e) {
            System.out.println(e);
       } 
    }
    

}
