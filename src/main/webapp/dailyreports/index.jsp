<%-- 
    Document   : index
    Created on : 10-Feb-2026, 12:05:45 pm
    Author     : vaishnavi.dhole
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.attendance.dto.DailyReport"%>

<%
    List<DailyReport> reports = (List<DailyReport>) request.getAttribute("reports");

    String startDate = (String) request.getAttribute("start_date");
    String endDate = (String) request.getAttribute("end_date");

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

                <div class="content-header">
                    <div class="container-fluid">
                        <div class="row mb-2">
                            <div class="col-sm-6">
                                <h1 class="m-0 text-dark">Daily Reports</h1>
                            </div>
                            <div class="col-sm-6">
                                <ol class="breadcrumb float-sm-right">
                                    <li class="breadcrumb-item">
                                        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                                    </li>
                                    <li class="breadcrumb-item active">Daily Reports</li>
                                </ol>
                            </div>
                        </div>
                    </div>
                </div>

                <section class="content">
                    <div class="container-fluid">

                        <% if (status != null) { %>
                        <div class="alert <%= status == 200 ? "alert-success" : "alert-danger" %>">
                            <%= message %>
                        </div>
                        <% } %>

                        <!-- Filter -->
                        <div class="card card-outline card-primary">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">
                                    <i class="fas fa-filter mr-1"></i> Filter Reports
                                </h3>
                            </div>

                            <div class="card-body">
                                <form method="get" action="${pageContext.request.contextPath}/daily-reports">
                                    <div class="row">
                                        <div class="col-md-4">
                                            <label>Start Date</label>
                                            <input type="date" name="start_date" value="<%=startDate%>" class="form-control" required>
                                        </div>

                                        <div class="col-md-4">
                                            <label>End Date</label>
                                            <input type="date" name="end_date" value="<%=endDate%>" class="form-control" required>
                                        </div>

                                        <div class="col-md-4">
                                            <label>&nbsp;</label>
                                            <button type="submit" class="btn btn-primary btn-block">
                                                <i class="fas fa-search mr-1"></i> Fetch
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>


                        <!-- Add Report -->
                        <div class="card card-outline card-success">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">
                                    <i class="fas fa-plus mr-1"></i> Add Daily Report
                                </h3>
                            </div>

                            <div class="card-body">
                                <form method="post" action="${pageContext.request.contextPath}/daily-reports" enctype="multipart/form-data">
                                    <input type="hidden" name="action" value="create">

                                    <div class="row">
                                        <div class="col-md-4">
                                            <label>Date</label>
                                            <input type="date" name="date" class="form-control" required>
                                        </div>
                                        <div class="col-md-8">
                                            <label>Upload Photos (Multiple)</label>
                                            <input type="file" name="images" class="form-control" accept="image/*" multiple>
                                        </div>
                                    </div>

                                    <div class="form-group mt-3">
                                        <label>Report Text</label>
                                        <textarea name="reportText" class="form-control" rows="6" required></textarea>
                                    </div>

                                    <button type="submit" class="btn btn-success">
                                        <i class="fas fa-save mr-1"></i> Save Report
                                    </button>
                                </form>
                            </div>
                        </div>


                        <!-- Report List -->
                        <div class="card card-outline card-warning">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">
                                    <i class="fas fa-list mr-1"></i> Report List
                                </h3>
                            </div>

                            <div class="card-body">
                                <% if (reports != null && reports.size() > 0) { 
                                    for (DailyReport r : reports) { %>

                                <div class="border p-3 mb-3">
                                    <h5>
                                        <b><%= r.getReportDate() %></b>
                                        <span class="badge badge-info ml-2"><%= r.getCreatedByName() %></span>
                                    </h5>

                                    <small class="text-muted">Created At: <%= r.getCreatedAt() %></small>

                                    <pre class="mt-2"><%= r.getReportText() %></pre>

                                    <!-- Images -->
                                    <% if (r.getImages() != null && r.getImages().size() > 0) { %>
                                    <div class="row">
                                        <% for (String img : r.getImages()) { %>
                                        <div class="col-md-3">
                                            <img src="${pageContext.request.contextPath}/uploads/<%= img %>"
                                                 class="img-fluid img-thumbnail"
                                                 style="height:120px; object-fit:cover;">
                                        </div>
                                        <% } %>
                                    </div>
                                    <% } %>

                                    <!-- Delete button -->
                                    <form method="post" action="${pageContext.request.contextPath}/daily-reports" style="margin-top:10px;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="reportId" value="<%= r.getReportId() %>">
                                        <button class="btn btn-danger btn-sm"
                                                onclick="return confirm('Delete this report?')">
                                            <i class="fas fa-trash"></i> Delete
                                        </button>
                                    </form>
                                </div>

                                <% } 
                                } else { %>

                                <div class="text-center text-muted p-4">
                                    No reports found
                                </div>

                                <% } %>
                            </div>
                        </div>

                    </div>
                </section>
            </div>

            <jsp:include page="/WEB-INF/layout/footer.jsp" />
        </div>
    </body>
</html>
