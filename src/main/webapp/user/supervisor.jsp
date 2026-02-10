<%-- 
    Document   : supervisor
    Created on : 10-Feb-2026, 4:44:50 pm
    Author     : vaishnavi.dhole
--%>

<%@page import="java.util.List"%>
<%@page import="com.attendance.dto.User"%>
<%@page import="com.attendance.dto.Site"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    List<User> supervisorList = (List<User>) request.getAttribute("supervisorList");
    List<Site> siteList = (List<Site>) request.getAttribute("siteList");

    Integer status = (Integer) request.getAttribute("status");
    String message = (String) request.getAttribute("message");
%>

<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/layout/head.jsp" />

<body class="hold-transition sidebar-mini layout-fixed layout-navbar-fixed layout-footer-fixed text-sm">
<div class="wrapper">

    <jsp:include page="/WEB-INF/layout/header.jsp" />

    <aside class="main-sidebar sidebar-dark-primary elevation-4 sidebar-dark-success">
        <jsp:include page="/WEB-INF/layout/sidemenu.jsp" />
    </aside>

    <div class="content-wrapper">

        <section class="content-header">
            <div class="container-fluid">
                <h1>Supervisor Management</h1>
            </div>
        </section>

        <section class="content">
            <div class="container-fluid">

                <% if(status != null){ %>
                    <div class="alert <%= status==200 ? "alert-success" : "alert-danger" %>">
                        <%= message %>
                    </div>
                <% } %>

                <!-- Add Supervisor -->
                <div class="card card-outline card-primary">
                    <div class="card-header">
                        <h3 class="card-title">Add Supervisor</h3>
                    </div>

                    <div class="card-body">
                        <form method="post" action="<%=request.getContextPath()%>/users">
                            <input type="hidden" name="action" value="create">

                            <div class="row">
                                <div class="col-md-3">
                                    <input type="text" name="fullName" class="form-control" placeholder="Full Name" required>
                                </div>

                                <div class="col-md-2">
                                    <input type="text" name="username" class="form-control" placeholder="Username" required>
                                </div>

                                <div class="col-md-2">
                                    <input type="text" name="password" class="form-control" placeholder="Password" required>
                                </div>

                                <div class="col-md-3">
                                    <select name="siteId" class="form-control" required>
                                        <option value="">-- Select Site --</option>
                                        <% if(siteList != null){
                                            for(Site s : siteList){ %>
                                                <option value="<%=s.getSiteId()%>"><%=s.getSiteName()%></option>
                                        <% } } %>
                                    </select>
                                </div>

                                <div class="col-md-2">
                                    <button class="btn btn-primary btn-block">
                                        <i class="fas fa-plus"></i> Add
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Supervisor List -->
                <div class="card card-outline card-success">
                    <div class="card-header">
                        <h3 class="card-title">Supervisor List</h3>
                    </div>

                    <div class="card-body table-responsive p-0">
                        <table class="table table-hover text-nowrap">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Full Name</th>
                                    <th>Username</th>
                                    <th>Site</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>

                                <% if(supervisorList != null){
                                    for(User u : supervisorList){ %>

                                    <tr>
                                        <td><%=u.getUserId()%></td>
                                        <td><%=u.getFullName()%></td>
                                        <td><%=u.getUsername()%></td>
                                        <td><%=u.getExtra1()%></td>
                                        <td>
                                            <form method="post" action="<%=request.getContextPath()%>/supervisors" style="display:inline;">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="userId" value="<%=u.getUserId()%>">
                                                <button class="btn btn-danger btn-sm" onclick="return confirm('Remove supervisor?')">
                                                    Delete
                                                </button>
                                            </form>
                                        </td>
                                    </tr>

                                <% } } %>

                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
        </section>

    </div>

    <jsp:include page="/WEB-INF/layout/footer.jsp" />

</div>
</body>
</html>
