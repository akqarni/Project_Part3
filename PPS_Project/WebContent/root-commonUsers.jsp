<%@ page import= "PPS_Project.bean.*, PPS_Project.DAO.*, java.util.List, javax.servlet.http.HttpSession " %>
 
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
String message = (String) request.getAttribute("message");
String errorMessage = (String) request.getAttribute("errorMessage");


Root_part3_DAO Root_DAO = new Root_part3_DAO();        
List <User> usersEmailsList = Root_DAO.allUsersEmails();
List <String> commonFollowers = (List) request.getAttribute("commonFollowers");
%>
 




<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Common Followers page</title>

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


<div align="center">
       	

        <caption><h2>Common Followers List</h2></caption>
        <br>
        <form action = "findCommonFollowers"> 
        <select name = "firstUserEmail">
            <%
				for (User user1 : usersEmailsList) {
			%>
			<option value = <%=user1.getUser_email()%> > <%=user1.getUser_email()%> </option>
        
            <%
			}
			%>
       
         </select>
         
         <select name = "secondUserEmail">
            <%
				for (User user2 : usersEmailsList) {
			%>
			<option value = <%=user2.getUser_email()%> > <%=user2.getUser_email()%> </option>
        
            <%
			}
			%>
       
         </select>
         
         <br><br>
         <input type="submit"  class="btn btn-success" value="Submit">
         
         </form>
         
         <br>
         
		<br></br>


		<h5 style="align: center; color:red"><%=((errorMessage == null) || (errorMessage.trim().equals(""))) ? "" : (errorMessage)%></h5>
	    <h5 style="align: center; color:green"><%=((message == null) || (message.trim().equals(""))) ? "" : (message)%></h5>
		
		 <%
// Iterating through commonFollowersList
if(request.getAttribute("commonFollowers") != null)  // Null check for the object
{
	%>
	<table cellspacing="2" cellpadding="2"  border="1" >
    <tr><th>Common Followers:</th></tr>	 
	<%
	for (String follower_email : commonFollowers) {
	%>
	<tr>
	<td>  <%=follower_email%>           </td>
	</tr>
	<%
	}
	%>
	</table>
	<%
}
    %>		
			
    </div>  

</body>
</html>