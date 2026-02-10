<%-- 
    Document   : report
    Created on : 10-Feb-2026, 1:56:36 pm
    Author     : vaishnavi.dhole
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.attendance.dto.Attendance"%>
<%@page import="com.attendance.dto.Site"%>
<%@page import="com.attendance.dto.User"%>
<%@page import="com.attendance.common.SessionUtils"%>

<%
    HttpSession sessions = request.getSession(false);
    User login = SessionUtils.getLoginedUser(sessions);

    List<Attendance> reportList = (List<Attendance>) request.getAttribute("reportList");
    List<Site> sites = (List<Site>) request.getAttribute("sites");

    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");
    Integer siteId = (Integer) request.getAttribute("siteId");
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
                                <h1 class="m-0 text-dark">Attendance Report</h1>
                            </div>
                            <div class="col-sm-6">
                                <ol class="breadcrumb float-sm-right">
                                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Attendance Report</li>
                                </ol>
                            </div>
                        </div>
                    </div>
                </div>

                <section class="content">
                    <div class="container-fluid">

                        <!-- FILTER CARD -->
                        <div class="card card-outline card-primary">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">
                                    <i class="fas fa-filter mr-1"></i> Filters
                                </h3>
                            </div>

                            <div class="card-body">
                                <form method="get" action="${pageContext.request.contextPath}/attendance-report">
                                    <div class="row">

                                        <div class="col-md-3">
                                            <label>Start Date</label>
                                            <input type="date" name="startDate" class="form-control"
                                                   value="<%=startDate%>" required>
                                        </div>

                                        <div class="col-md-3">
                                            <label>End Date</label>
                                            <input type="date" name="endDate" class="form-control"
                                                   value="<%=endDate%>" required>
                                        </div>

                                        <% if ("ADMIN".equals(login.getRole())) { %>
                                        <div class="col-md-3">
                                            <label>Site</label>
                                            <select name="siteId" class="form-control">
                                                <option value="0">All Sites</option>
                                                <% if (sites != null) {
                                                    for (Site s : sites) { %>
                                                <option value="<%=s.getSiteId()%>"
                                                        <%= (siteId != null && siteId == s.getSiteId()) ? "selected" : "" %>>
                                                    <%=s.getSiteName()%>
                                                </option>
                                                <% } } %>
                                            </select>
                                        </div>
                                        <% } %>

                                        <div class="col-md-3 mt-4">
                                            <button type="submit" class="btn btn-primary btn-block">
                                                <i class="fas fa-search mr-1"></i> Search
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <!-- EXPORT BUTTONS -->
                        <div class="mb-3">
                            <a href="${pageContext.request.contextPath}/attendance-report-excel?startDate=<%=startDate%>&endDate=<%=endDate%>&siteId=<%=siteId%>"
                               class="btn btn-success">
                                <i class="fas fa-file-excel"></i> Export Excel
                            </a>

                            <a href="${pageContext.request.contextPath}/attendance-report-pdf?startDate=<%=startDate%>&endDate=<%=endDate%>&siteId=<%=siteId%>"
                               class="btn btn-danger">
                                <i class="fas fa-file-pdf"></i> Export PDF
                            </a>
                        </div>

                        <!-- REPORT TABLE -->
                        <div class="card card-outline card-success">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">
                                    <i class="fas fa-list mr-1"></i> Attendance List
                                </h3>
                            </div>

                            <div class="card-body table-responsive p-0">
                                <table class="table table-hover text-nowrap">
                                    <thead>
                                        <tr>
                                            <th>Date</th>
                                            <th>Worker</th>
                                            <th>Work Type</th>
                                            <th>Day Type</th>
                                            <th>Wage</th>
                                            <th>Supervisor</th>
                                            <th>Photo</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <% if (reportList != null && reportList.size() > 0) {
                                            for (Attendance a : reportList) { %>

                                        <tr>
                                            <td><%=a.getAttendanceDate()%></td>
                                            <td><%=a.getWorkerName()%></td>
                                            <td><%=a.getWorkTypeName()%></td>
                                            <td><%=a.getDayType()%></td>
                                            <td>₹ <%=a.getWage()%></td>
                                            <td><%=a.getSupervisorName()%></td>

                                            <td>
                                                <a href="${pageContext.request.contextPath}/uploads/<%=a.getPhotoPath()%>"
                                                   target="_blank">
                                                    View
                                                </a>
                                            </td>
                                        </tr>

                                        <% } } else { %>
                                        <tr>
                                            <td colspan="7" class="text-center text-muted p-3">
                                                No record found
                                            </td>
                                        </tr>
                                        <% } %>
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
