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
Follows_DAO Follows = new Follows_DAO();       
List <Follows> followers_list = Follows.selectALLFollowersByFollowingID(user.getUser_email());
List <Follows> following_list = Follows.selectALLFollowingsByFollowerID(user.getUser_email());
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
		<div class="card">
			<div class="card-body">
			
			<h5 style="align: center; color:red"><%=((errorMessage == null) || (errorMessage.trim().equals(""))) ? "" : (errorMessage)%></h5>
			<h5 style="align: center; color:green"><%=((message == null) || (message.trim().equals(""))) ? "" : (message)%></h5>
			
			<form action="follow" method="post">
				<fieldset class="form-group">
					<label> Enter email to follow </label> <input type="text"
						value="" class="form-control"
						name="receiver-email" required="required" >
				</fieldset>
				<button type="submit" class="btn btn-success">Follow</button>
				
				</form>
				<caption><h3>Followers List</h3></caption>
        <br>
        <table border="1" cellpadding="5">
            <%
				for (Follows Follow : followers_list) {
			%>
			
			<tr>
				   <td>  <%=Follow.getFollower_email()%>         </td>
							
			</tr>

			<%
			}
			%>
        </table>
		<br> <br>
		<caption><h3>Following List</h3></caption>
        <br>
        <table border="1" cellpadding="5">           
            <%
				for (Follows Follow : following_list) {
			%>
			
			<tr>
				   <td>  <a href= "showFollowingInfo?followingEmail=<%=Follow.getFollowing_email()%>" > <%=Follow.getFollowing_email()%>   </a>      </td>
							
			</tr>

			<%
			}
			%>
        </table>
				
			</div>
		</div>
	</div>
</body>
</html>