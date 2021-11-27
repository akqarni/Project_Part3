

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <%
	String message = (String) request.getAttribute("message");
	String errorMessage = (String) request.getAttribute("errorMessage");
%>

<%
	String userEmail = (String) request.getParameter("email");
%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login in</title>
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
				
			   <form action="login" method="post">


				<fieldset class="form-group">
					<label>User Email</label> <input type="text"
						value="" class="form-control"
						name="email">
				</fieldset>
				
				<fieldset class="form-group">
					<label>User Password</label> <input type="password"
						value="" class="form-control"
						name="password">
				</fieldset>


				<button type="submit" class="btn btn-success">Login</button>
				<a href="${pageContext.request.contextPath}/user-signupForm.jsp"
							style = "float: right" class="btn btn-success">Sign up </a>&nbsp;&nbsp;&nbsp;
				
				</form>
			</div>
		</div>
	</div>

</body>
</html>