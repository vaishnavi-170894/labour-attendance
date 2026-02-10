<%-- 
    Document   : settlement
    Created on : 10-Feb-2026, 3:04:51 pm
    Author     : vaishnavi.dhole
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.attendance.dto.Payment"%>

<%
    List<Payment> settlementList = (List<Payment>) request.getAttribute("settlementList");

    double totalWages = 0;
    double totalPaid = 0;
    double totalBalance = 0;

    if(settlementList != null){
        for(Payment p : settlementList){
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
                        <h1 class="m-0 text-dark">Worker Settlement Report</h1>
                    </div>
                </div>

                <section class="content">
                    <div class="container-fluid">

                        <div class="card card-outline card-warning">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">Worker Balance Settlement</h3>
                                <a href="${pageContext.request.contextPath}/payment-report"
                                   class="btn btn-secondary btn-sm float-right">
                                    Back
                                </a>
                            </div>

                            <div class="card-body table-responsive p-0">
                                <table class="table table-hover text-nowrap">
                                    <thead>
                                        <tr>
                                            <th>Worker</th>
                                            <th>Total Wages</th>
                                            <th>Total Paid</th>
                                            <th>Balance</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <% if(settlementList != null && settlementList.size() > 0){
                                            for(Payment p : settlementList){ %>

                                        <tr>
                                            <td><%=p.getWorkerName()%></td>
                                            <td>₹ <%=p.getTotalWages()%></td>
                                            <td class="text-success font-weight-bold">₹ <%=p.getTotalPaid()%></td>
                                            <td class="<%=p.getBalance() > 0 ? "text-danger font-weight-bold" : "text-success font-weight-bold"%>">
                                                ₹ <%=p.getBalance()%>
                                            </td>
                                        </tr>

                                        <% } } else { %>
                                        <tr>
                                            <td colspan="4" class="text-center text-muted p-3">
                                                No settlement data found
                                            </td>
                                        </tr>
                                        <% } %>
                                    </tbody>
                                </table>
                            </div>

                            <div class="card-footer text-right font-weight-bold">
                                Total Wages: ₹ <%=totalWages%> |
                                Paid: ₹ <%=totalPaid%> |
                                Balance: ₹ <%=totalBalance%>
                            </div>

                        </div>

                    </div>
                </section>

            </div>

            <jsp:include page="/WEB-INF/layout/footer.jsp" />

        </div>
    </body>
</html>
