<%@ page import= "PPS_Project.bean.*, PPS_Project.DAO.*, java.util.List, javax.servlet.http.HttpSession " %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page isELIgnored="false"%>
<%
User user = (User) session.getAttribute("user");
if (user == null) {
	//User not signed in. Or session expired.
	// Forward to user login page.
	request.getRequestDispatcher("user-loginForm.jsp").forward(request, response);
	return;

}

String userName = user.getUser_fname() + " " + user.getUser_lname();

%>
    
    <%
	String message = (String) request.getAttribute("message");
	String errorMessage = (String) request.getAttribute("errorMessage");
	
%>
    
    
    <%
    List <String> followingInfo = (List) request.getAttribute("followingInfo");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>My followers Page</title>
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
	
	<a href="log-out" class="btn btn-fail" style ="float:right">Logout</a>
	
	<h3 class="container col-md-5" style="float:center"> Hello <%=userName%>! </h3>

    <a href="user-balancePage.jsp" class="btn">Your balance</a>
    <a href="user-activityPage.jsp" class="btn">Activity list</a>
   	
				<br> <br>	   
			 
		<div class="container col-md-5">
		 <caption><h2>Select information to be visible to followers</h2></caption>
		<div class="card">
			<div class="card-body">
			
			<h5 style="align: center; color:red"><%=((errorMessage == null) || (errorMessage.trim().equals(""))) ? "" : (errorMessage)%></h5>
			<h5 style="align: center; color:green"><%=((message == null) || (message.trim().equals(""))) ? "" : (message)%></h5>
			
			<br>
			
					

					 
					 
					 
					 
					 
					 
					 
					 
					 
					 <FORM ACTION="selectinfo" METHOD="post">
		             <INPUT TYPE="CHECKBOX" NAME="check1" VALUE="check1" >
		             Email
		            <BR>
		            <INPUT TYPE="CHECKBOX" NAME="check2" VALUE="check2">
		            first name
		            <BR>
		            <INPUT TYPE="CHECKBOX" NAME="check3" VALUE="check3">
		           Last name
		            <BR>
		             
		            <INPUT TYPE="CHECKBOX" NAME="check4" VALUE="check4">
		           Address
		            <BR>
		            
		            <INPUT TYPE="CHECKBOX" NAME="check5" VALUE="check5">
		            Date of birth
		            <BR>
		            
		            <INPUT TYPE="CHECKBOX" NAME="check6" VALUE="check6">
		          PPS Balance
		            <BR>
		       
		            <INPUT TYPE="CHECKBOX" NAME="check7" VALUE="check7">
		          Dollar Balance
		            <BR>
		            <BR>
		            <INPUT TYPE="SUBMIT" VALUE="Submit">
		        </FORM>
		        
   	
			
    </div> 
				
			</div>
		</div>
	</div>
</body>
</html>