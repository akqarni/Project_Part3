 
 <%@ page import= "PPS_Project.bean.*, PPS_Project.DAO.Root_part3_DAO, PPS_Project.DAO.User_DAO, PPS_Project.DAO.Transactions_DAO, java.util.List, javax.servlet.http.HttpSession " %>
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

<%--- String userEmail = user.getUser_email();
if(request.getParameter("activityList") == null) { 
	Transactions_DAO Transactions = new Transactions_DAO();        // listed in attribute 'transactions list'
	List <Transactions> transactions_list = Transactions.getAllTransactionsOfUser(userEmail);
	request.setAttribute("activityList", transactions_list);       
}--%>


<%
Root_part3_DAO Root_DAO = new Root_part3_DAO();         // listed in attribute 'transactions list'
List <Transactions> transactions_list =  Root_DAO .get_neverbuy_user();
%>
 




<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Activity page</title>

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
	
	<a href="log-out" class="btn btn-fail" style ="float:right">Logout</a>
		<div class="topnav">
  <a class="active" href="#home">Frequent buyers</a>
  <a href="biggest_buy.jsp">Biggest buy</a>
  <a href="biggest_buyer.jsp">Biggest buyers</a>
  <a href="#news">Popular users</a>
  <a href="root-commonUsers.jsp">Common users</a>
  <a href="neverbuy_users.jsp">Neverbuy users</a>
  <a href="neversell_user.jsp">Neversell users</a>
  <a href="#news">Lucky users</a>
  <a href="inactive_user.jsp">Inactive users</a>
  <a href="#news">Statistics</a>
  <a href="log-out">Logout</a>
</div>
	
	<h3 class="container col-md-5" style="float:center"> Hello <%=userName%>! </h3>

   

<div align="center">
        <caption><h2>Neverbuy_users</h2></caption>
        <br>
        <table border="1" cellpadding="5">
            <tr>
                <th>email</th>
               
            </tr>
            
            <!-- 
            <c:forEach var="transaction" items="${activityList}">
                <tr>
                    <td><c:out value="${transaction.transaction_name}" /></td>
                    <td><c:out value="${transaction.transaction_date}" /></td>
                    <td><c:out value="${transaction.transaction_time}" /></td>
                    <td><c:out value="${transaction.dollar_amount}" /></td>
                    <td><c:out value="${transaction.PPS_amount}" /></td>
                </tr>
            </c:forEach> 
             -->
            
            
            <%
				for (Transactions transaction : transactions_list) {
			%>
			
			<tr>
				   <td>  <%=transaction.getTransaction_to_email()%>       </td>
				   
							
			</tr>

			<%
			}
			%>
            
           
            
			
        </table>
    </div>  

</body>
</html>