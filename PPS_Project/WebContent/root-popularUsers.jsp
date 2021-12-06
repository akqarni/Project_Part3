<%@ page import= "PPS_Project.bean.*, PPS_Project.DAO.Root_part3_DAO, PPS_Project.DAO.User_DAO, PPS_Project.DAO.Transactions_DAO, java.util.List, javax.servlet.http.HttpSession " %>
 
 <%@ page isELIgnored="false"%>


<%--- String userEmail = user.getUser_email();
if(request.getParameter("activityList") == null) { 
	Transactions_DAO Transactions = new Transactions_DAO();        // listed in attribute 'transactions list'
	List <Transactions> transactions_list = Transactions.getAllTransactionsOfUser(userEmail);
	request.setAttribute("activityList", transactions_list);       
}--%>


<%
Root_part3_DAO Root_DAO = new Root_part3_DAO();           // listed in attribute 'transactions list'
List <User> User_list =Root_DAO.allPopularUsers();
%>
 




<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Popular Users</title>

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
	<style>
body {
  margin: 0;
  font-family: Arial, Helvetica, sans-serif;
}

.topnav {
  overflow: hidden;
  background-color: #333;
}

.topnav a {
  float: left;
  color: #f2f2f2;
  text-align: center;
  padding: 12px 14px;
  text-decoration: none;
  font-size: 10px;
}

.topnav a:hover {
  background-color: #ddd;
  color: black;
}

.topnav a.active {
  background-color: #04AA6D;
  color: white;
}
</style>
	

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

<div class="topnav">
  <a class="active" href="#home">Frequent buyers</a>
  <a href="biggest_buy.jsp">Biggest buy</a>
  <a href="biggest_buyer.jsp">Biggest buyers</a>
  <a href="root-popularUsers.jsp">Popular users</a>
  <a href="root-commonUsers.jsp">Common users</a>
  <a href="neverbuy_users.jsp">Neverbuy users</a>
  <a href="neversell_user.jsp">Neversell users</a>
  <a href="root-luckyUsers.jsp">Lucky users</a>
  <a href="inactive_user.jsp">Inactive users</a>
  <a href="root-systemStatistics.jsp">Statistics</a>
  <a href="log-out">Logout</a>
</div>

<div align="center">
        <caption><h2>Popular Users</h2></caption>
        <br>
        <table border="1" cellpadding="5">
            <tr>
                <th>Popular User</th>
                
            </tr>
            
       
             <%
				for (User user : User_list) {
			%>
			
			<tr>
				   <td>  <%=user.getUser_email()%>           </td>
				   
			</tr>

			<%
			}
			%>
            
           
            
			
        </table>
    </div>  

</body>
</html>