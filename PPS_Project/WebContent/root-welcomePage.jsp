<%@ page import= "PPS_Project.bean.*, PPS_Project.DAO.*, java.util.List, javax.servlet.http.HttpSession " %>
 
 <%@ page isELIgnored="false"%>
 
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
     <%
	String message = (String) request.getAttribute("message");
	String errorMessage = (String) request.getAttribute("errorMessage");
	User user = (User) session.getAttribute("user");
	
	if (user == null) {
		//User not signed in. Or session expired.
		// Forward to user login page.
		request.getRequestDispatcher("user-loginForm.jsp").forward(request, response);
		return;

	}
	String userName = user.getUser_fname() + " " + user.getUser_lname();
%>

<!DOCTYPE html>
<html>
<head>

<title> Admin Page </title>

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">

</head>
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



<div class="container col-md-5">

<a href="log-out" class="btn" style ="float:right">Logout</a>
	
	<h3 class="container col-md-5" style="float:center"> Hello <%=userName%>! </h3>
	<br><br><br><br><br><br><br><br><br>
		<div class="card">
			<div class="card-body">
			
			<h5 style="align: center; color:red"><%=((errorMessage == null) || (errorMessage.trim().equals(""))) ? "" : (errorMessage)%></h5>
			<h5 style="align: center; color:green"><%=((message == null) || (message.trim().equals(""))) ? "" : (message)%></h5>
				
			    <form action="initialize" method="post">

				<button type="submit" class="btn btn-success"> Initialize Database </button>
				<a href="root-allProjectInfo.jsp" class="btn btn-success"> PPS Project Updates </a>
				</form>
			</div>
			
		</div>
	</div>

</body>
</html>