<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    


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
  Message from session: <b><%= session.getAttribute("mySecretMessage") %></b>
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

<br><br><br><br><br><br><br><br><br>

<div class="container col-md-5">
		<div class="card">
			<div class="card-body">
			
			
			   <form action="" method="post">
 
				<a href="${pageContext.request.contextPath}/transfer.jsp"
							style = "float: right" class="btn btn-success">transfer-pps </a>&nbsp;&nbsp;&nbsp;
				
				
				
				</form>
			</div>
		</div>
	</div>

</body>
</html>