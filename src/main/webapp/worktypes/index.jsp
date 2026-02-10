<%-- 
    Document   : index
    Created on : 10-Feb-2026, 12:27:38 pm
    Author     : vaishnavi.dhole
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.attendance.dto.WorkType"%>

<%
    List<WorkType> workTypes = (List<WorkType>) request.getAttribute("workTypes");

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
                                <h1 class="m-0 text-dark">Work Types & Rates</h1>
                            </div>

                            <div class="col-sm-6">
                                <ol class="breadcrumb float-sm-right">
                                    <li class="breadcrumb-item">
                                        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                                    </li>
                                    <li class="breadcrumb-item active">Work Types</li>
                                </ol>
                            </div>
                        </div>
                    </div>
                </div>

                <section class="content">
                    <div class="container-fluid">

                        <% if (status != null) {%>
                        <div class="alert <%= status == 200 ? "alert-success" : "alert-danger"%>">
                            <%= message%>
                        </div>
                        <% } %>

                        <!-- Add Work Type -->
                        <div class="card card-outline card-success">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">
                                    <i class="fas fa-plus mr-1"></i> Add Work Type
                                </h3>
                            </div>

                            <div class="card-body">
                                <form method="post" action="${pageContext.request.contextPath}/work-types">
                                    <input type="hidden" name="action" value="create">

                                    <div class="row">
                                        <div class="col-md-4">
                                            <label>Work Type Name</label>
                                            <input type="text" name="name" class="form-control" required>
                                        </div>

                                        <div class="col-md-4">
                                            <label>Full Day Rate</label>
                                            <input type="number" step="0.01" name="full_day_rate" class="form-control" required>
                                        </div>

                                        <div class="col-md-4">
                                            <label>Half Day Rate</label>
                                            <input type="number" step="0.01" name="half_day_rate" class="form-control" required>
                                        </div>
                                    </div>

                                    <button type="submit" class="btn btn-success mt-3">
                                        <i class="fas fa-save mr-1"></i> Save
                                    </button>
                                </form>
                            </div>
                        </div>


                        <!-- Work Type List -->
                        <div class="card card-outline card-primary">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">
                                    <i class="fas fa-list mr-1"></i> Work Types List
                                </h3>
                            </div>

                            <div class="card-body table-responsive p-0">
                                <table class="table table-hover text-nowrap">
                                    <thead>
                                        <tr>
                                            <th>Work Type</th>
                                            <th>Full Day Rate</th>
                                            <th>Half Day Rate</th>
                                            <th class="text-center">Actions</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <% if (workTypes != null && workTypes.size() > 0) {
                                                for (WorkType wt : workTypes) {%>

                                        <tr>
                                            <td><b><%= wt.getWorkTypeName()%></b></td>
                                            <td>₹ <%= wt.getFullDayWage()%></td>
                                            <td>₹ <%= wt.getHalfDayWage()%></td>

                                            <td class="text-center">

                                                <!-- Update -->
                                                <form method="post" action="${pageContext.request.contextPath}/work-types" style="display:inline;">
                                                    <input type="hidden" name="action" value="update">
                                                    <input type="hidden" name="workTypeId" value="<%= wt.getWorkTypeId()%>">

                                                    <input type="hidden" name="name" value="<%= wt.getWorkTypeName()%>">
                                                    <input type="hidden" name="full_day_rate" value="<%= wt.getFullDayWage()%>">
                                                    <input type="hidden" name="half_day_rate" value="<%= wt.getHalfDayWage()%>">

                                                    <button type="submit" class="btn btn-warning btn-sm">
                                                        <a href="${pageContext.request.contextPath}/work-types?action=edit&id=<%= wt.getWorkTypeId()%>"
                                                           class="btn btn-warning btn-sm">
                                                            <i class="fas fa-edit"></i> Edit
                                                        </a>
                                                    </button>
                                                </form>

                                                <!-- Delete -->
                                                <form method="post" action="${pageContext.request.contextPath}/work-types" style="display:inline;">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="workTypeId" value="<%= wt.getWorkTypeId()%>">

                                                    <button type="submit" class="btn btn-danger btn-sm"
                                                            onclick="return confirm('Delete this work type?')">
                                                        <i class="fas fa-trash"></i> Delete
                                                    </button>
                                                </form>

                                            </td>
                                        </tr>

                                        <% }
                                        } else { %>

                                        <tr>
                                            <td colspan="4" class="text-center text-muted p-3">
                                                No work types found
                                            </td>
                                        </tr>

                                        <% }%>
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
