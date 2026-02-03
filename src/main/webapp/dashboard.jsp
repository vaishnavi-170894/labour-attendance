<%-- 
    Document   : dashboard
    Created on : 02-Feb-2026, 5:51:36 pm
    Author     : vaishnavi.dhole
--%>
<%@ page import="com.attendance.dto.User" %>

<%
User user = (User) session.getAttribute("LOGIN");
if (user == null || !"ADMIN".equals(user.getRole())) {
    response.sendRedirect(request.getContextPath() + "/index.jsp");
    return;
}
%>

<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
</head>
<body>

<h2>Welcome, <%= user.getFullName() %></h2>

<ul>
    <li>
        <a href="<%=request.getContextPath()%>/workers">Manage Workers</a>
    </li>
    <li>
        <a href="<%=request.getContextPath()%>/attendance">View Attendance</a>
    </li>
</ul>

<br>
<a href="<%=request.getContextPath()%>/logout">Logout</a>

</body>
</html>
