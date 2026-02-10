<%-- 
    Document   : index
    Created on : 10-Feb-2026, 2:37:45 pm
    Author     : vaishnavi.dhole
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.attendance.dto.Payment"%>
<%@page import="com.attendance.dto.User"%>
<%@page import="com.attendance.common.SessionUtils"%>

<%
    HttpSession sessions = request.getSession(false);
    User login = SessionUtils.getLoginedUser(sessions);

    List<Payment> paymentSummary = (List<Payment>) request.getAttribute("paymentSummary");

    Integer status = (Integer) request.getAttribute("status");
    String message = (String) request.getAttribute("message");

    double totalWages = 0;
    double totalPaid = 0;
    double totalBalance = 0;

    if (paymentSummary != null) {
        for (Payment p : paymentSummary) {
            totalWages += p.getTotalWages();
            totalPaid += p.getTotalPaid();
            totalBalance += p.getBalance();
        }
    }
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
                        <h1 class="m-0 text-dark">Payment Management</h1>
                    </div>
                </div>

                <section class="content">
                    <div class="container-fluid">

                        <% if (status != null) { %>
                        <div class="alert <%=status == 200 ? "alert-success" : "alert-danger"%>">
                            <%=message%>
                        </div>
                        <% } %>

                        <!-- SUMMARY CARDS -->
                        <div class="row">

                            <div class="col-md-4">
                                <div class="info-box">
                                    <span class="info-box-icon bg-info elevation-1">
                                        <i class="fas fa-rupee-sign"></i>
                                    </span>
                                    <div class="info-box-content">
                                        <span class="info-box-text">Total Wages</span>
                                        <span class="info-box-number">₹ <%=totalWages%></span>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-4">
                                <div class="info-box">
                                    <span class="info-box-icon bg-success elevation-1">
                                        <i class="fas fa-arrow-up"></i>
                                    </span>
                                    <div class="info-box-content">
                                        <span class="info-box-text">Total Paid</span>
                                        <span class="info-box-number">₹ <%=totalPaid%></span>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-4">
                                <div class="info-box">
                                    <span class="info-box-icon bg-danger elevation-1">
                                        <i class="fas fa-arrow-down"></i>
                                    </span>
                                    <div class="info-box-content">
                                        <span class="info-box-text">Total Balance</span>
                                        <span class="info-box-number">₹ <%=totalBalance%></span>
                                    </div>
                                </div>
                            </div>

                        </div>

                        <!-- TABLE -->
                        <div class="card card-outline card-primary">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">
                                    Worker Payment Status
                                </h3>
                            </div>

                            <div class="card-body table-responsive p-0">
                                <table class="table table-hover text-nowrap">
                                    <thead>
                                        <tr>
                                            <th>Worker</th>
                                            <th>Total Wages</th>
                                            <th>Total Paid</th>
                                            <th>Balance</th>
                                            <th>Last Payment</th>
                                            <th class="text-center">Action</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <% if (paymentSummary != null && paymentSummary.size() > 0) {
                                            for (Payment p : paymentSummary) { %>

                                        <tr>
                                            <td><%=p.getWorkerName()%></td>
                                            <td>₹ <%=p.getTotalWages()%></td>
                                            <td class="text-success font-weight-bold">₹ <%=p.getTotalPaid()%></td>

                                            <td class="<%=p.getBalance() > 0 ? "text-danger font-weight-bold" : "text-success font-weight-bold"%>">
                                                ₹ <%=p.getBalance()%>
                                            </td>

                                            <td>
                                                <% if (p.getLastPaymentDate() != null) { %>
                                                <%=p.getLastPaymentDate()%> <br>
                                                <small class="text-muted">₹ <%=p.getLastPaymentAmount()%></small>
                                                <% } else { %>
                                                No payment yet
                                                <% } %>
                                            </td>

                                            <td class="text-center">
                                                <a href="${pageContext.request.contextPath}/payments?action=history&workerId=<%=p.getWorkerId()%>"
                                                   class="btn btn-warning btn-sm">
                                                    <i class="fas fa-plus"></i> Pay
                                                </a>
                                            </td>
                                        </tr>

                                        <% } } else { %>
                                        <tr>
                                            <td colspan="6" class="text-center text-muted p-3">
                                                No payment data available
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
