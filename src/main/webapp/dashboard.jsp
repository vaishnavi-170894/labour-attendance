<%-- 
    Document   : dashboard
    Created on : 02-Feb-2026, 5:51:36 pm
    Author     : vaishnavi.dhole
--%>

<%@ page import="java.util.*" %>
<%@ page import="com.attendance.dto.User" %>
<%@ page session="false" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    String module = (String) request.getAttribute("module");
    String controller = (String) request.getAttribute("controller");
    String subaction = (String) request.getAttribute("subaction");
    String action = (String) request.getAttribute("action");

    // Dashboard stats (set from controller/servlet)
    Integer totalWorkers = (Integer) request.getAttribute("total_workers");
    Integer todayAttendance = (Integer) request.getAttribute("today_attendance");
    Double todayWage = (Double) request.getAttribute("today_wage");
    Double monthWage = (Double) request.getAttribute("month_wage");
    Integer totalSites = (Integer) request.getAttribute("total_sites");
    Integer totalSupervisors = (Integer) request.getAttribute("total_supervisors");
    Double monthPayment = (Double) request.getAttribute("month_payment");
    Double pendingBalance = (Double) request.getAttribute("pending_balance");

    // Recent attendance list (List<Map<String,Object>> OR List<AttendanceDTO>)
    List<Map<String, Object>> recentAttendance = (List<Map<String, Object>>) request.getAttribute("recent_attendance");

    if (totalWorkers == null) {
        totalWorkers = 0;
    }
    if (todayAttendance == null) {
        todayAttendance = 0;
    }
    if (todayWage == null) {
        todayWage = 0.0;
    }
    if (monthWage == null) {
        monthWage = 0.0;
    }
    if (totalSites == null) {
        totalSites = 0;
    }
    if (totalSupervisors == null) {
        totalSupervisors = 0;
    }
    if (monthPayment == null) {
        monthPayment = 0.0;
    }
    if (pendingBalance == null)
        pendingBalance = 0.0;
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
                                <h1 class="m-0 text-dark"><%=module%></h1>
                            </div>
                            <div class="col-sm-6">
                                <ol class="breadcrumb float-sm-right">
                                    <li class="breadcrumb-item">
                                        <a href="${pageContext.request.contextPath}/dashboard"><%=module%></a>
                                    </li>
                                    <li class="breadcrumb-item active">home</li>
                                </ol>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.content-header -->

                <!-- Main content -->
                <section class="content">
                    <div class="container-fluid">

                        <!-- Info boxes -->
                        <div class="row">

                            <!-- Latest App Version -->
                            <div class="col-12 col-sm-6 col-md-3">
                                <div class="info-box">
                                    <span class="info-box-icon bg-info elevation-1">
                                        <i class="fas fa-cog"></i>
                                    </span>

                                    <div class="info-box-content">
                                        <span class="info-box-text">Latest App Version</span>
                                        <span class="info-box-number">
                                            1.0
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <!-- Total Workers -->
                            <div class="col-12 col-sm-6 col-md-3">
                                <div class="info-box mb-3">
                                    <span class="info-box-icon bg-danger elevation-1">
                                        <i class="fas fa-users"></i>
                                    </span>

                                    <div class="info-box-content">
                                        <span class="info-box-text">Total Workers</span>
                                        <span class="info-box-number"><%= totalWorkers%></span>
                                    </div>
                                </div>
                            </div>

                            <!-- Today's Attendance -->
                            <div class="clearfix hidden-md-up"></div>

                            <div class="col-12 col-sm-6 col-md-3">
                                <div class="info-box mb-3">
                                    <span class="info-box-icon bg-success elevation-1">
                                        <i class="fas fa-calendar-check"></i>
                                    </span>

                                    <div class="info-box-content">
                                        <span class="info-box-text">Today's Attendance</span>
                                        <span class="info-box-number"><%= todayAttendance%></span>
                                    </div>
                                </div>
                            </div>

                            <!-- Monthly Wage -->
                            <div class="col-12 col-sm-6 col-md-3">
                                <div class="info-box mb-3">
                                    <span class="info-box-icon bg-warning elevation-1">
                                        <i class="fas fa-rupee-sign"></i>
                                    </span>

                                    <div class="info-box-content">
                                        <span class="info-box-text">Monthly Wage</span>
                                        <span class="info-box-number">₹<%= String.format("%.0f", monthWage)%></span>
                                    </div>
                                </div>
                            </div>
                            <!-- Today's Wage box -->
                            <div class="col-12 col-sm-6 col-md-3">
                                <div class="info-box mb-3">
                                    <span class="info-box-icon bg-primary elevation-1">
                                        <i class="fas fa-wallet"></i>
                                    </span>

                                    <div class="info-box-content">
                                        <span class="info-box-text">Today's Wage</span>
                                        <span class="info-box-number">₹<%= String.format("%.0f", todayWage)%></span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12 col-sm-6 col-md-3">
                                <div class="info-box mb-3">
                                    <span class="info-box-icon bg-secondary elevation-1">
                                        <i class="fas fa-map-marker-alt"></i>
                                    </span>

                                    <div class="info-box-content">
                                        <span class="info-box-text">Total Sites</span>
                                        <span class="info-box-number"><%= totalSites%></span>
                                    </div>
                                </div>
                            </div>
<div class="col-12 col-sm-6 col-md-3">
    <div class="info-box mb-3">
        <span class="info-box-icon bg-success elevation-1">
            <i class="fas fa-user-tie"></i>
        </span>

        <div class="info-box-content">
            <span class="info-box-text">Supervisors</span>
            <span class="info-box-number"><%= totalSupervisors %></span>
        </div>
    </div>
</div>
<div class="col-12 col-sm-6 col-md-3">
    <div class="info-box mb-3">
        <span class="info-box-icon bg-info elevation-1">
            <i class="fas fa-money-bill-wave"></i>
        </span>

        <div class="info-box-content">
            <span class="info-box-text">Monthly Payment</span>
            <span class="info-box-number">₹<%= String.format("%.0f", monthPayment) %></span>
        </div>
    </div>
</div>
        <div class="col-12 col-sm-6 col-md-3">
    <div class="info-box mb-3">
        <span class="info-box-icon bg-danger elevation-1">
            <i class="fas fa-balance-scale"></i>
        </span>

        <div class="info-box-content">
            <span class="info-box-text">Pending Balance</span>
            <span class="info-box-number">₹<%= String.format("%.0f", pendingBalance) %></span>
        </div>
    </div>
</div>



                        </div>
                        <!-- /.row -->
<div class="card card-outline card-primary">
    <div class="card-header">
        <h3 class="card-title font-weight-bold">
            Quick Actions
        </h3>
    </div>

    <div class="card-body">
        <a href="${pageContext.request.contextPath}/attendance?action=create" class="btn btn-success mr-2">
            <i class="fas fa-calendar-check"></i> Mark Attendance
        </a>

        <a href="${pageContext.request.contextPath}/daily-reports" class="btn btn-info mr-2">
            <i class="fas fa-file-alt"></i> Daily Report
        </a>

        <a href="${pageContext.request.contextPath}/payment-report" class="btn btn-warning mr-2">
            <i class="fas fa-wallet"></i> Payment Report
        </a>

        <a href="${pageContext.request.contextPath}/attendance-report" class="btn btn-primary">
            <i class="fas fa-file-pdf"></i> Attendance Report
        </a>
    </div>
</div>


                        <!-- Second row (Today's Wage + Recent Attendance Table) -->
                        <div class="row">


                            <!-- Recent Attendance -->
                            <div class="col-12 col-md-8">
                                <div class="card card-outline card-success">
                                    <div class="card-header">
                                        <h3 class="card-title font-weight-bold">
                                            <i class="fas fa-list mr-1"></i> Recent Attendance
                                        </h3>
                                    </div>

                                    <div class="card-body table-responsive p-0">
                                        <table class="table table-hover text-nowrap">
                                            <thead>
                                                <tr>
                                                    <th>Date</th>
                                                    <th>Worker</th>
                                                    <th>Work Type</th>
                                                    <th>Type</th>
                                                    <th>Wage</th>
                                                </tr>
                                            </thead>
                                            <tbody>

                                                <%
                                                    if (recentAttendance != null && recentAttendance.size() > 0) {
                                                        int limit = Math.min(recentAttendance.size(), 10);

                                                        for (int i = 0; i < limit; i++) {
                                                            Map<String, Object> row = recentAttendance.get(i);

                                                            String date = row.get("date") != null ? row.get("date").toString() : "";
                                                            String workerName = row.get("worker_name") != null ? row.get("worker_name").toString() : "";
                                                            String workTypeName = row.get("work_type_name") != null ? row.get("work_type_name").toString() : "";
                                                            String attendanceType = row.get("attendance_type") != null ? row.get("attendance_type").toString() : "";
                                                            String wage = row.get("wage") != null ? row.get("wage").toString() : "";

                                                            String badgeClass = "badge badge-warning";
                                                            if ("full_day".equalsIgnoreCase(attendanceType)) {
                                                                badgeClass = "badge badge-success";
                                                            }

                                                            String displayType = attendanceType.replace("_", " ");
                                                %>

                                                <tr>
                                                    <td><%= date%></td>
                                                    <td><b><%= workerName%></b></td>
                                                    <td><%= workTypeName%></td>
                                                    <td><span class="<%= badgeClass%>"><%= displayType%></span></td>
                                                    <td><b>₹<%= wage%></b></td>
                                                </tr>

                                                <%
                                                    }
                                                } else {
                                                %>

                                                <tr>
                                                    <td colspan="5" class="text-center text-muted p-3">
                                                        No attendance records yet
                                                    </td>
                                                </tr>

                                                <%
                                                    }
                                                %>

                                            </tbody>
                                        </table>
                                    </div>

                                </div>
                            </div>

                        </div>
                        <!-- /.row -->


                    </div><!--/. container-fluid -->
                </section>
                <!-- /.content -->
            </div>
            <!-- /.content-wrapper -->

            <!-- Control Sidebar -->
            <aside class="control-sidebar control-sidebar-dark"></aside>

            <!-- Main Footer -->
            <jsp:include page="/WEB-INF/layout/footer.jsp" />

        </div>
        <!-- ./wrapper -->

    </body>
</html>
