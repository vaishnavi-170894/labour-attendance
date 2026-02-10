<%-- 
    Document   : mark
    Created on : 10-Feb-2026, 12:48:39 pm
    Author     : vaishnavi.dhole
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.attendance.dto.Worker"%>
<%@page import="com.attendance.dto.WorkType"%>
<%@page import="com.attendance.dto.Attendance"%>

<%
    List<Worker> workerList = (List<Worker>) request.getAttribute("workerList");
    List<WorkType> workTypeList = (List<WorkType>) request.getAttribute("workTypeList");
    List<Attendance> attendanceList = (List<Attendance>) request.getAttribute("attendanceList");

    String selectedDate = (String) request.getAttribute("selectedDate");

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
                        <h1 class="m-0 text-dark">Mark Attendance</h1>
                    </div>
                </div>

                <section class="content">
                    <div class="container-fluid">

                        <% if (status != null) { %>
                        <div class="alert <%= status == 200 ? "alert-success" : "alert-danger" %>">
                            <%= message %>
                        </div>
                        <% } %>

                        <!-- Mark Attendance Form -->
                        <div class="card card-outline card-success">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">Attendance Entry</h3>
                            </div>

                            <div class="card-body">

                                <form method="post" action="${pageContext.request.contextPath}/attendance" enctype="multipart/form-data">
                                    <input type="hidden" name="action" value="create">

                                    <div class="row">

                                        <div class="col-md-3">
                                            <label>Date</label>
                                            <input type="date" name="attendanceDate" value="<%=selectedDate%>"
                                                   class="form-control" required>
                                        </div>

                                        <div class="col-md-3">
                                            <label>Worker</label>
                                            <select name="workerId" class="form-control" required>
                                                <option value="">-- Select Worker --</option>
                                                <% for (Worker w : workerList) { %>
                                                <option value="<%= w.getWorkerId() %>"><%= w.getWorkerName() %></option>
                                                <% } %>
                                            </select>
                                        </div>

                                        <div class="col-md-3">
                                            <label>Work Type</label>
                                            <select name="workTypeId" class="form-control" required>
                                                <option value="">-- Select Work Type --</option>
                                                <% for (WorkType wt : workTypeList) { %>
                                                <option value="<%= wt.getWorkTypeId() %>"><%= wt.getWorkTypeName() %></option>
                                                <% } %>
                                            </select>
                                        </div>

                                        <div class="col-md-3">
                                            <label>Day Type</label>
                                            <select name="dayType" class="form-control" required>
                                                <option value="FULL">FULL</option>
                                                <option value="HALF">HALF</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="row mt-3">
                                        <div class="col-md-6">
                                            <label>Upload Photo</label>
                                            <input type="file" name="photo" class="form-control" accept="image/*" required>
                                        </div>
                                    </div>

                                    <input type="hidden" name="latitude" id="latitude">
                                    <input type="hidden" name="longitude" id="longitude">
                                    <input type="hidden" name="accuracy" id="accuracy">

                                    <button type="submit" class="btn btn-success mt-3">
                                        <i class="fas fa-save"></i> Save Attendance
                                    </button>
                                </form>

                            </div>
                        </div>

                        <!-- Attendance List -->
                        <div class="card card-outline card-primary">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">
                                    Attendance List for <%=selectedDate%>
                                </h3>
                            </div>

                            <div class="card-body table-responsive p-0">
                                <table class="table table-hover text-nowrap">
                                    <thead>
                                        <tr>
                                            <th>Worker</th>
                                            <th>Work Type</th>
                                            <th>Day Type</th>
                                            <th>Wage</th>
                                            <th>Photo</th>
                                            <th class="text-center">Action</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <% if (attendanceList != null && attendanceList.size() > 0) {
                                            for (Attendance a : attendanceList) { %>

                                        <tr>
                                            <td><%= a.getWorkerName() %></td>
                                            <td><%= a.getWorkTypeName() %></td>
                                            <td><%= a.getDayType() %></td>
                                            <td>₹ <%= a.getWage() %></td>

                                            <td>
                                                <a href="${pageContext.request.contextPath}/uploads/<%= a.getPhotoPath() %>"
                                                   target="_blank">
                                                    View
                                                </a>
                                            </td>

                                            <td class="text-center">

                                                <a class="btn btn-warning btn-sm"
                                                   href="${pageContext.request.contextPath}/attendance?action=edit&id=<%=a.getAttendanceId()%>">
                                                    <i class="fas fa-edit"></i>
                                                </a>

                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/attendance"
                                                      style="display:inline;">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="attendanceId" value="<%=a.getAttendanceId()%>">
                                                    <input type="hidden" name="date" value="<%=selectedDate%>">

                                                    <button type="submit"
                                                            class="btn btn-danger btn-sm"
                                                            onclick="return confirm('Delete this attendance?')">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
                                                </form>

                                            </td>
                                        </tr>

                                        <% } } else { %>
                                        <tr>
                                            <td colspan="6" class="text-center text-muted p-3">
                                                No attendance found
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

        <script>
            navigator.geolocation.getCurrentPosition(function (pos) {
                document.getElementById("latitude").value = pos.coords.latitude;
                document.getElementById("longitude").value = pos.coords.longitude;
                document.getElementById("accuracy").value = pos.coords.accuracy;
            });
        </script>

    </body>
</html>
