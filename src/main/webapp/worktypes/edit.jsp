<%-- 
    Document   : edit
    Created on : 10-Feb-2026, 12:34:43 pm
    Author     : vaishnavi.dhole
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.attendance.dto.WorkType"%>

<%
    WorkType wt = (WorkType) request.getAttribute("workType");
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
                                <h1 class="m-0 text-dark">Edit Work Type</h1>
                            </div>

                            <div class="col-sm-6">
                                <ol class="breadcrumb float-sm-right">
                                    <li class="breadcrumb-item">
                                        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                                    </li>
                                    <li class="breadcrumb-item">
                                        <a href="${pageContext.request.contextPath}/work-types">Work Types</a>
                                    </li>
                                    <li class="breadcrumb-item active">Edit</li>
                                </ol>
                            </div>
                        </div>
                    </div>
                </div>

                <section class="content">
                    <div class="container-fluid">

                        <% if (wt == null) { %>
                        <div class="alert alert-danger">
                            Work type not found.
                        </div>
                        <% } else { %>

                        <div class="card card-outline card-warning">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">
                                    <i class="fas fa-edit mr-1"></i> Update Work Type
                                </h3>
                            </div>

                            <div class="card-body">
                                <form method="post" action="${pageContext.request.contextPath}/work-types">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="workTypeId" value="<%= wt.getWorkTypeId() %>">

                                    <div class="form-group">
                                        <label>Work Type Name</label>
                                        <input type="text" name="name" class="form-control"
                                               value="<%= wt.getWorkTypeName() %>" required>
                                    </div>

                                    <div class="form-group">
                                        <label>Full Day Rate</label>
                                        <input type="number" step="0.01" name="full_day_rate"
                                               class="form-control"
                                               value="<%= wt.getFullDayWage() %>" required>
                                    </div>

                                    <div class="form-group">
                                        <label>Half Day Rate</label>
                                        <input type="number" step="0.01" name="half_day_rate"
                                               class="form-control"
                                               value="<%= wt.getHalfDayWage() %>" required>
                                    </div>

                                    <button type="submit" class="btn btn-warning">
                                        <i class="fas fa-save mr-1"></i> Update
                                    </button>

                                    <a href="${pageContext.request.contextPath}/work-types"
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
