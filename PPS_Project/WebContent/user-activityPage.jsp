<%@ page import= "PPS_Project.bean.*, PPS_Project.DAO.Transactions_DAO, java.util.List, javax.servlet.http.HttpSession " %>
 
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
Transactions_DAO Transactions = new Transactions_DAO();        // listed in attribute 'transactions list'
List <Transactions> transactions_list = Transactions.getAllTransactionsOfUser(user.getUser_email());
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
    <a href="user-transferPage.jsp" class="btn">Transfer</a>
    <a href="user-buyPage.jsp" class="btn">buy</a>
    <a href="user-sellPage.jsp" class="btn">sell</a>

<div align="center">
        <caption><h2>Activity List</h2></caption>
        <br>
        <table border="1" cellpadding="5">
            <tr>
                <th>Transaction Type</th>
                <th>Date</th>
                <th>Time</th>
                <th>Dollar amount</th>
                <th>PPS amount</th>
                <th>From</th>
                <th>To</th>
                <th>PPS Price</th>
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
				   <td>  <%=transaction.getTransaction_name()%>           </td>
				   <td>  <%=transaction.getTransaction_date().toString()%></td>
				   <td>  <%=transaction.getTransaction_time().toString()%></td>
				   <td>  <%=transaction.getDollar_amount()%>              </td>
				   <td>  <%=transaction.getPPS_amount()%>                 </td>
				   <td>  <%=transaction.getTransaction_from_email()%>     </td>
				   <td>  <%=transaction.getTransaction_to_email()%>       </td>
				   <td>  <%=transaction.getPPS_price()%>       </td>
							
			</tr>

			<%
			}
			%>
            
           
            
			
        </table>
    </div>  

</body>
</html>