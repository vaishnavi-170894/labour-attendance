<%-- 
    Document   : workers
    Created on : 03-Feb-2026, 11:08:50 am
    Author     : vaishnavi.dhole
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.attendance.dto.Worker" %>
<%@ page import="java.util.List" %>

<%
Worker workerData = (Worker) request.getAttribute("worker-data");
List<Worker> workers = workerData != null ? workerData.getList() : null;

Integer status = (Integer) request.getAttribute("status");
String message = (String) request.getAttribute("message");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Worker Management</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/app.css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

</head>
<body>
<div class="card">
<h2>Worker Management</h2>

<% if (status != null) { %>
<div style="color:<%= status == 200 ? "green" : "red" %>">
    <%= message %>
</div>
<% } %>

<!-- ADD WORKER -->
<form method="post" action="<%=request.getContextPath()%>/workers">
    <input type="hidden" name="action" value="create">

    <input type="text" name="workerName" placeholder="Worker Name" required>
    <input type="text" name="mobile" placeholder="Mobile No">

    <button type="submit">Add Worker</button>
</form>

<hr>

<!-- WORKER LIST -->
<table border="1" cellpadding="8">
<tr>
    <th>ID</th>
    <th>Name</th>
    <th>Mobile</th>
    <th>Action</th>
</tr>

<% if (workers != null) {
    for (Worker w : workers) { %>
<tr>
    <td><%= w.getWorkerId() %></td>
    <td><%= w.getWorkerName() %></td>
    <td><%= w.getMobileNo() %></td>
    <td>
        <form method="post" action="<%=request.getContextPath()%>/workers" style="display:inline">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="workerId" value="<%= w.getWorkerId() %>">
            <button type="submit" onclick="return confirm('Remove worker?')">Delete</button>
        </form>
    </td>
</tr>
<%  }
} %>

</table>

<br>
<a href="<%=request.getContextPath()%>/dashboard">Back to Dashboard</a> |
<a href="<%=request.getContextPath()%>/logout">Logout</a>

</div>
</body>
</html>
