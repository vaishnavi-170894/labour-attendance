<%-- 
    Document   : index.jsp
    Created on : 02-Feb-2026, 5:15:05 pm
    Author     : vaishnavi.dhole
--%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%
Integer status = (Integer) request.getAttribute("status");
String message = (String) request.getAttribute("message");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/app.css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

</head>
<body>
<div class="card">
<h2>Labour Attendance System</h2>

<% if (status != null) { %>
<div style="color:<%= status == 200 ? "green" : "red" %>">
    <%= message %>
</div>
<% } %>

<form method="post" action="<%=request.getContextPath()%>/login">
    <label>Username</label><br>
    <input type="text" name="username" required><br><br>

    <label>Password</label><br>
    <input type="password" name="password" required><br><br>

    <button type="submit">Login</button>
</form>
</div>
</body>
</html>
