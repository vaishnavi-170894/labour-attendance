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

    String module = (String) request.getAttribute("module");
    String controller = (String) request.getAttribute("controller");
    String subaction = (String) request.getAttribute("subaction");
    String action = (String) request.getAttribute("action");

    if (module == null) module = "Worker";
    if (controller == null) controller = "Workers";
    if (action == null) action = "";
%>

<!DOCTYPE html>
<html lang="en">

    <jsp:include page="/WEB-INF/layout/head.jsp" />

    <body class="hold-transition sidebar-mini layout-fixed layout-navbar-fixed layout-footer-fixed text-sm">

        <div class="wrapper">

            <!-- Navbar -->
            <jsp:include page="/WEB-INF/layout/header.jsp" />
            <!-- /.navbar -->

            <!-- Main Sidebar Container -->
            <aside class="main-sidebar sidebar-dark-primary elevation-4 sidebar-dark-success">
                <jsp:include page="/WEB-INF/layout/sidemenu.jsp" />
            </aside>

            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper">

                <!-- Content Header (Page header) -->
                <div class="content-header">
                    <div class="container-fluid">
                        <div class="row mb-2">
                            <div class="col-sm-6">
                                <h1 class="m-0 text-dark">Worker Management</h1>
                            </div>

                            <div class="col-sm-6">
                                <ol class="breadcrumb float-sm-right">
                                    <li class="breadcrumb-item">
                                        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                                    </li>
                                    <li class="breadcrumb-item active">Workers</li>
                                </ol>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.content-header -->


                <!-- Main content -->
                <section class="content">
                    <div class="container-fluid">

                        <% if (status != null) { %>
                        <div class="alert <%= status == 200 ? "alert-success" : "alert-danger" %>">
                            <%= message %>
                        </div>
                        <% } %>

                        <div class="row">

                            <!-- Add Worker Form -->
                            <div class="col-md-4">
                                <div class="card card-outline card-success">
                                    <div class="card-header">
                                        <h3 class="card-title font-weight-bold">
                                            <i class="fas fa-user-plus mr-1"></i> Add Worker
                                        </h3>
                                    </div>

                                    <div class="card-body">
                                        <form method="post" action="<%=request.getContextPath()%>/workers">
                                            <input type="hidden" name="action" value="create">

                                            <div class="form-group">
                                                <label>Worker Name</label>
                                                <input type="text" name="workerName" class="form-control" placeholder="Worker Name" required>
                                            </div>

                                            <div class="form-group">
                                                <label>Mobile No</label>
                                                <input type="text" name="mobile" class="form-control" placeholder="Mobile No">
                                            </div>

                                            <button type="submit" class="btn btn-success btn-block">
                                                <i class="fas fa-save mr-1"></i> Add Worker
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>


                            <!-- Worker List -->
                            <div class="col-md-8">
                                <div class="card card-outline card-primary">
                                    <div class="card-header">
                                        <h3 class="card-title font-weight-bold">
                                            <i class="fas fa-users mr-1"></i> Worker List
                                        </h3>
                                    </div>

                                    <div class="card-body table-responsive p-0">
                                        <table class="table table-hover text-nowrap">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Name</th>
                                                    <th>Mobile</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>

                                            <tbody>
                                                <% if (workers != null && workers.size() > 0) {
                                                    for (Worker w : workers) { %>

                                                <tr>
                                                    <td><%= w.getWorkerId() %></td>
                                                    <td><b><%= w.getWorkerName() %></b></td>
                                                    <td><%= w.getMobileNo() %></td>
                                                    <td>
                                                        <form method="post" action="<%=request.getContextPath()%>/workers" style="display:inline">
                                                            <input type="hidden" name="action" value="delete">
                                                            <input type="hidden" name="workerId" value="<%= w.getWorkerId() %>">

                                                            <button type="submit" class="btn btn-danger btn-sm"
                                                                    onclick="return confirm('Remove worker?')">
                                                                <i class="fas fa-trash"></i> Delete
                                                            </button>
                                                        </form>
                                                    </td>
                                                </tr>

                                                <%  }
                                                } else { %>

                                                <tr>
                                                    <td colspan="4" class="text-center text-muted p-3">
                                                        No workers found.
                                                    </td>
                                                </tr>

                                                <% } %>
                                            </tbody>
                                        </table>
                                    </div>

                                </div>
                            </div>

                        </div>
                        <!-- /.row -->

                    </div>
                </section>
                <!-- /.content -->

            </div>
            <!-- /.content-wrapper -->


            <!-- Main Footer -->
            <jsp:include page="/WEB-INF/layout/footer.jsp" />

        </div>
        <!-- ./wrapper -->

    </body>
</html>
