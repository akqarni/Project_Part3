<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <%
	String message = (String) request.getAttribute("message");
	String errorMessage = (String) request.getAttribute("errorMessage");
%>
    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sign up</title>

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

	<br>
	
	<div class="container col-md-5">
		<div class="card">
			<div class="card-body">
				
			<h5 style="align: center; color:red"><%=((errorMessage == null) || (errorMessage.trim().equals(""))) ? "" : (errorMessage)%></h5>
			<h5 style="align: center; color:green"><%=((message == null) || (message.trim().equals(""))) ? "" : (message)%></h5>
				
			   <form action="insert" method="post">

				<fieldset class="form-group">
					<label>First Name</label> <input type="text"
						value="" class="form-control"
						name="fname" required="required">
				</fieldset>
				
				<fieldset class="form-group">
					<label>Last Name</label> <input type="text"
						value="" class="form-control"
						name="lname" required="required">
				</fieldset>

				<fieldset class="form-group">
					<label>User Email</label> <input type="text"
						value="" class="form-control"
						name="email" required="required">
				</fieldset>
				
				<fieldset class="form-group">
					<label>User Password</label> <input type="password"
						value="" class="form-control"
						name="password1" required="required">
				</fieldset>
				
				<fieldset class="form-group">
					<label>Confirm Password</label> <input type="password"
						value="" class="form-control"
						name="password2" required="required">
				</fieldset>
				

				<fieldset class="form-group">
					<label>User address</label> <input type="text"
						value="" class="form-control"
						name="address" required="required">
				</fieldset>
				
				<fieldset class="form-group">
				<i Style = "text-align: right; color:grey; size: 10"> Use the format: YYYY-MM-DD</i>
					<label>User Date of birth</label> <input type="date"
						value="" class="form-control"
						name="dob" required="required">
				</fieldset>

				<button type="submit" class="btn btn-success">Sign up</button> 
				<a href="${pageContext.request.contextPath}/user-loginForm.jsp"
							style = "float: right" class="btn btn-success">Login </a>&nbsp;&nbsp;&nbsp;
				
				</form>
				
	            
				
			</div>
		</div>
	</div>
</body>
</html>