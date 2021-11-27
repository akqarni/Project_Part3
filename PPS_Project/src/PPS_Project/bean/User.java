package PPS_Project.bean;


public class User {

	private String user_email;
	private String user_password;
	private String user_fname;
	private String user_lname;
	private String user_address;
	private String user_dob;
	private long PPS_balance;
	private double dollar_balance;

	public User(String user_fname, String user_lname, String user_address, String user_dob) {
		super();
		this.user_fname = user_fname;
		this.user_lname = user_lname;
		this.user_address = user_address;
		this.user_dob = user_dob;
	}


	public User(String user_email, String user_password ,String user_fname, String user_lname, String user_address, String user_dob) {
		super();
		this.user_email = user_email;
		this.user_password = user_password;
		this.user_fname = user_fname;
		this.user_lname = user_lname;
		this.user_address = user_address;
		this.user_dob = user_dob;
	}
	
	
	
	
	public User(String user_email, String user_password, String user_fname, String user_lname, String user_address,
			String user_dob, long pPS_balance, double dollar_balance) {
		super();
		this.user_email = user_email;
		this.user_password = user_password;
		this.user_fname = user_fname;
		this.user_lname = user_lname;
		this.user_address = user_address;
		this.user_dob = user_dob;
		this.PPS_balance = pPS_balance;
		this.dollar_balance = dollar_balance;
	}


	public String getUser_email() {
		return user_email;
	}
	
	public void setUser_email(String user_id) {
		this.user_email = user_id;
	}
	
	public String getUser_password() {
		return user_password;
	}
	
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	
	public String getUser_fname() {
		return user_fname;
	}
	
	public void setUser_fname(String user_fname) {
		this.user_fname = user_fname;
	}
	
	public String getUser_lname() {
		return user_lname;
	}
	
	public void setUser_lname(String user_lname) {
		this.user_lname = user_lname;
	}
	
	public String getUser_address() {
		return user_address;
	}
	
	public void setUser_address(String user_address) {
		this.user_address = user_address;
	}
	
	public String getUser_dob() {
		return user_dob;
	}
	
	public void setUser_dob(String user_dob) {
		this.user_dob = user_dob;
	}

	public long getPPS_balance() {
		return PPS_balance;
	}

	public void setPPS_balance(long pPS_balance) {
		PPS_balance = pPS_balance;
	}

	public double getDollar_balance() {
		return dollar_balance;
	}

	public void setDollar_balance(double dollar_balance) {
		this.dollar_balance = dollar_balance;
	}
	
	
	
	
}
