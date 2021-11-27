<%@ page import= "PPS_Project.bean.*, PPS_Project.DAO.*, java.util.List, javax.servlet.http.HttpSession , java.text.DecimalFormat " %>
 
 <%@ page isELIgnored="false"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <%
	String message = (String) request.getAttribute("message");
	String errorMessage = (String) request.getAttribute("errorMessage");
	
%>
<%-- 
<%
	String userEmail = (String) request.getParameter("email");
    String dollarBalance = (String) request.getParameter("dollarBalance");
    String PPS_Balance = (String) request.getParameter("PPS_Balance");

%>
--%>


<%
User user = (User) session.getAttribute("user");
if (user == null) {
	//User not signed in. Or session expired.
	// Forward to user login page.
	request.getRequestDispatcher("user-loginForm.jsp").forward(request, response);
	return;

}
String userName = user.getUser_fname() + " " + user.getUser_lname();
User_DAO userDAO = new User_DAO();

double dollarBalance = userDAO.selectUser(user.getUser_email()).getDollar_balance();
long PPS_Balance = userDAO.selectUser(user.getUser_email()).getPPS_balance();

DecimalFormat df = new DecimalFormat("#.##");

%>
    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Balance Page</title>

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
	
</head>
<body>

<header>
		<nav class="navbar navbar-expand-md navbar-dark"
			style="background-color: black">
			<div>
			<center>
			<h1 style = "color: white">PPS Crypto</h1>
			</center>
				
			</div>
		</nav>
	</header>
	<a href="log-out" class="btn" style ="float:right">Logout</a>
	<a href="user-activityPage.jsp" class="btn">Activity list</a>
	<a href="user-transferPage.jsp" class="btn">Transfer</a>
	
	<h3 class="container col-md-5" style="float:center"> Hello <%=userName%>! </h3>
	
	
	       
		<br> <br>	   
			 
		<div class="container col-md-5">
		<div class="card">
			<div class="card-body">
			
			<h5 style="align: center; color:red"><%=((errorMessage == null) || (errorMessage.trim().equals(""))) ? "" : (errorMessage)%></h5>
			<h5 style="align: center; color:green"><%=((message == null) || (message.trim().equals(""))) ? "" : (message)%></h5>
				
			<h5> Your Dollar balance is: <%=(df.format(dollarBalance))%> </h5>	
			<br>
			<h5> Your PPS balance is: <%=(PPS_Balance)%> </h5>	
			
			<br> <br>
			
			<p> Enter the dollar amount to deposit</p>
			   <form action="deposit" method="post">
				<fieldset class="form-group">
					<label>Dollar $:</label> <input type="text"
						value="" class="form-control"
						name="dollar-amount-deposit">
				</fieldset>
				<button type="submit" class="btn btn-success">Deposit</button>
				</form>
			<br> <br>
				
			<p> Enter the dollar amount to withdraw</p>
			   <form action="withdraw" method="post">
			   <fieldset class="form-group">
					<label>Dollar $:</label> <input type="text"
						value="" class="form-control"
						name="dollar-amount-withdraw">
				</fieldset>
				<button type="submit" class="btn btn-success">Withdraw</button>
				</form>
				
			</div>
		</div>
	</div>

</body>
</html>