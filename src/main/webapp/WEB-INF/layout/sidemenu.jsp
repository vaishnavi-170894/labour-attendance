<%@page import="com.attendance.common.SessionUtils"%>
<%@page import="com.attendance.dto.User"%>
<%@page import="com.attendance.common.Constants"%>


<%
    HttpSession usersession = request.getSession(false);
    User user = SessionUtils.getLoginedUser(usersession);

    String module = (String) request.getAttribute("module");
    String controller = (String) request.getAttribute("controller");
    String subaction = (String) request.getAttribute("subaction");
    String action = (String) request.getAttribute("action");

    if (module == null) {
        module = "";
    }
    if (controller == null) {
        controller = "";
    }
    if (action == null)
        action = "";
%>

<!-- Brand Logo -->
<a href="${pageContext.request.contextPath}/dashboard" class="brand-link">
    <img src="${pageContext.request.contextPath}/assets/dist/img/AdminLTELogo.png"
         alt="AdminLTE Logo"
         class="brand-image img-circle elevation-3"
         style="opacity: .8">
    <span class="brand-text font-weight-light"><%=Constants.PROJECTNAME%></span>
</a>

<!-- Sidebar -->
<div class="sidebar">

    <!-- Sidebar user panel (optional) -->
    <div class="user-panel mt-3 pb-3 mb-3 d-flex">
        <div class="image">
            <img src="${pageContext.request.contextPath}/assets/dist/img/avatar.png"
                 class="img-circle elevation-2"
                 alt="User Image">
        </div>
        <div class="info">
            <a href="#" class="d-block"><%= user != null ? user.getFullName() : "Guest"%></a>
        </div>
    </div>

    <!-- Sidebar Menu -->
    <nav class="mt-2">
        <ul class="nav nav-pills nav-sidebar flex-column nav-compact nav-child-indent"
            data-widget="treeview"
            role="menu"
            data-accordion="false">

            <!-- Dashboard -->
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/dashboard"
                   class="nav-link <%= module.equals("Dashboard") ? "active" : ""%>">
                    <i class="nav-icon fas fa-tachometer-alt"></i>
                    <p>Dashboard</p>
                </a>
            </li>


            <!-- Workers -->
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/workers"
                   class="nav-link <%= module.equals("Worker") ? "active" : ""%>">
                    <i class="nav-icon fas fa-users"></i>
                    <p>Workers</p>
                </a>
            </li>


            <!-- Work Types -->
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/work-types"
                   class="nav-link <%= module.equals("WorkTypes") ? "active" : ""%>">
                    <i class="nav-icon fas fa-briefcase"></i>
                    <p>Work Types</p>
                </a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/sites" class="nav-link">
                    <i class="nav-icon fas fa-map-marker-alt"></i>
                    <p>Sites</p>
                </a>
            </li>

            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/users" class="nav-link">
                    <i class="nav-icon fas fa-user-tie"></i>
                    <p>Supervisors</p>
                </a>
            </li>


            <!-- Attendance -->
            <li class="nav-item has-treeview <%= module.equals("Attendance") ? "menu-open" : ""%>">
                <a href="#"
                   class="nav-link <%= module.equals("Attendance") ? "active" : ""%>">
                    <i class="nav-icon fas fa-calendar-check"></i>
                    <p>
                        Attendance
                        <i class="right fas fa-angle-left"></i>
                    </p>
                </a>

                <ul class="nav nav-treeview">

                    <!-- Mark Attendance -->
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/attendance?action=mark"
                           class="nav-link <%= action.equals("mark") ? "active" : ""%>">
                            <i class="far fa-circle nav-icon"></i>
                            <p>Mark Attendance</p>
                        </a>
                    </li>

                    <!-- Backdated Attendance -->
                    <!--                    <li class="nav-item">
                                            <a href="${pageContext.request.contextPath}/attendance?action=backdated"
                                               class="nav-link <%= action.equals("backdated") ? "active" : ""%>">
                                                <i class="far fa-circle nav-icon"></i>
                                                <p>Back-dated Attendance</p>
                                            </a>
                                        </li>-->

                    <!-- Attendance Reports -->
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/attendance-report"
                           class="nav-link <%= controller.equals("attendance-report") ? "active" : ""%>">
                            <i class="nav-icon fas fa-file-alt"></i>
                            <p>Attendance Report</p>
                        </a>
                    </li>


                </ul>
            </li>


            <!-- Payment Management -->
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/payments"
                   class="nav-link <%= module.equals("Payments") ? "active" : ""%>">
                    <i class="nav-icon fas fa-wallet"></i>
                    <p>Payments</p>
                </a>
            </li>
            <!-- Payment Reports -->
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/payment-report"
                   class="nav-link <%= controller.equals("payment-report") ? "active" : ""%>">
                    <i class="nav-icon fas fa-file-invoice"></i>
                    <p>Payment Report</p>
                </a>
            </li>


            <!-- Daily Reports -->
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/daily-reports"
                   class="nav-link <%= module.equals("DailyReports") ? "active" : ""%>">
                    <i class="nav-icon fas fa-clipboard-list"></i>
                    <p>Daily Reports</p>
                </a>
            </li>

        </ul>
    </nav>
    <!-- /.sidebar-menu -->
</div>
