<%-- 
    Document   : edit
    Created on : 10-Feb-2026, 12:55:04 pm
    Author     : vaishnavi.dhole
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.attendance.dto.Attendance"%>
<%@page import="com.attendance.dto.Worker"%>
<%@page import="com.attendance.dto.WorkType"%>

<%
    Attendance att = (Attendance) request.getAttribute("attendanceData");
    List<Worker> workerList = (List<Worker>) request.getAttribute("workerList");
    List<WorkType> workTypeList = (List<WorkType>) request.getAttribute("workTypeList");
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
                        <h1 class="m-0 text-dark">Edit Attendance</h1>
                    </div>
                </div>

                <section class="content">
                    <div class="container-fluid">

                        <% if (att == null) { %>
                        <div class="alert alert-danger">Attendance not found</div>
                        <% } else { %>

                        <div class="card card-outline card-warning">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">Update Attendance</h3>
                            </div>

                            <div class="card-body">

                                <form method="post" action="${pageContext.request.contextPath}/attendance" enctype="multipart/form-data">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="attendanceId" value="<%=att.getAttendanceId()%>">

                                    <div class="form-group">
                                        <label>Date</label>
                                        <input type="text" class="form-control" value="<%=att.getAttendanceDate()%>" readonly>
                                    </div>

                                    <div class="form-group">
                                        <label>Worker</label>
                                        <select name="workerId" class="form-control" required>
                                            <% for (Worker w : workerList) { %>
                                            <option value="<%=w.getWorkerId()%>"
                                                    <%= (w.getWorkerId() == att.getWorkerId()) ? "selected" : "" %>>
                                                <%=w.getWorkerName()%>
                                            </option>
                                            <% } %>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label>Work Type</label>
                                        <select name="workTypeId" class="form-control" required>
                                            <% for (WorkType wt : workTypeList) { %>
                                            <option value="<%=wt.getWorkTypeId()%>"
                                                    <%= (wt.getWorkTypeId() == att.getWorkTypeId()) ? "selected" : "" %>>
                                                <%=wt.getWorkTypeName()%>
                                            </option>
                                            <% } %>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label>Day Type</label>
                                        <select name="dayType" class="form-control" required>
                                            <option value="FULL" <%= "FULL".equals(att.getDayType()) ? "selected" : "" %>>FULL</option>
                                            <option value="HALF" <%= "HALF".equals(att.getDayType()) ? "selected" : "" %>>HALF</option>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label>Old Photo</label><br>
                                        <a href="${pageContext.request.contextPath}/uploads/<%=att.getPhotoPath()%>" target="_blank">
                                            View Old Photo
                                        </a>
                                    </div>

                                    <div class="form-group">
                                        <label>Upload New Photo (Optional)</label>
                                        <input type="file" name="photo" class="form-control" accept="image/*">
                                    </div>

                                    <button type="submit" class="btn btn-warning">
                                        <i class="fas fa-save"></i> Update
                                    </button>

                                    <a href="${pageContext.request.contextPath}/attendance?date=<%=att.getAttendanceDate()%>"
                                       class="btn btn-secondary">
                                        Cancel
                                    </a>
                                </form>

                            </div>
                        </div>

                        <% } %>

                    </div>
                </section>

            </div>

            <jsp:include page="/WEB-INF/layout/footer.jsp" />

        </div>
    </body>
</html>
