<%-- 
    Document   : index
    Created on : 10-Feb-2026, 4:38:32 pm
    Author     : vaishnavi.dhole
--%>

<%@page import="java.util.List"%>
<%@page import="com.attendance.dto.Site"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
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
                <h1>Site Management</h1>
            </div>
        </section>

        <section class="content">
            <div class="container-fluid">

                <% if(status != null){ %>
                    <div class="alert <%= status==200 ? "alert-success" : "alert-danger" %>">
                        <%= message %>
                    </div>
                <% } %>

                <div class="card card-outline card-primary">
                    <div class="card-header">
                        <h3 class="card-title">Add New Site</h3>
                    </div>

                    <div class="card-body">
                        <form method="post" action="<%=request.getContextPath()%>/sites">
                            <input type="hidden" name="action" value="create">

                            <div class="row">
                                <div class="col-md-3">
                                    <input type="text" name="siteName" class="form-control" placeholder="Site Name" required>
                                </div>
                                <div class="col-md-2">
                                    <input type="text" name="siteLat" class="form-control" placeholder="Latitude" required>
                                </div>
                                <div class="col-md-2">
                                    <input type="text" name="siteLng" class="form-control" placeholder="Longitude" required>
                                </div>
                                <div class="col-md-2">
                                    <input type="number" name="allowedRadius" class="form-control" placeholder="Radius (m)" required>
                                </div>
                                <div class="col-md-3">
                                    <button class="btn btn-primary btn-block">
                                        <i class="fas fa-plus"></i> Add Site
                                    </button>
                                </div>
                            </div>

                        </form>
                    </div>
                </div>

                <div class="card card-outline card-success">
                    <div class="card-header">
                        <h3 class="card-title">Site List</h3>
                    </div>

                    <div class="card-body table-responsive p-0">
                        <table class="table table-hover text-nowrap">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Site Name</th>
                                    <th>Latitude</th>
                                    <th>Longitude</th>
                                    <th>Radius</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if(siteList != null){
                                    for(Site s : siteList){ %>

                                    <tr>
                                        <td><%=s.getSiteId()%></td>
                                        <td><%=s.getSiteName()%></td>
                                        <td><%=s.getSiteLat()%></td>
                                        <td><%=s.getSiteLng()%></td>
                                        <td><%=s.getAllowedRadius()%></td>
                                        <td>
                                            <form method="post" action="<%=request.getContextPath()%>/sites" style="display:inline;">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="siteId" value="<%=s.getSiteId()%>">
                                                <button class="btn btn-danger btn-sm" onclick="return confirm('Delete site?')">
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
