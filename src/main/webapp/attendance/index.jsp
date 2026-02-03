<%-- 
    Document   : index
    Created on : 03-Feb-2026, 12:29:03 pm
    Author     : vaishnavi.dhole
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.attendance.dto.User" %>

<%
User user = (User) session.getAttribute("LOGIN");
if (user == null || !"SUPERVISOR".equals(user.getRole())) {
    response.sendRedirect(request.getContextPath() + "/index.jsp");
    return;
}

Integer status = (Integer) request.getAttribute("status");
String message = (String) request.getAttribute("message");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Attendance</title>
</head>
<body>

<h2>Welcome, <%= user.getFullName() %></h2>

<% if (status != null) { %>
<div style="color:<%= status == 200 ? "green" : "red" %>">
    <%= message %>
</div>
<% } %>

<a href="<%=request.getContextPath()%>/attendance?action=create">
    ➕ Mark Attendance
</a>

<br><br>
<a href="<%=request.getContextPath()%>/logout">Logout</a>

</body>
</html>
