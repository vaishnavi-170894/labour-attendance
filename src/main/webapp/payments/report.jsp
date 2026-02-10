<%-- 
    Document   : report
    Created on : 10-Feb-2026, 3:09:02 pm
    Author     : vaishnavi.dhole
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.attendance.dto.Payment"%>

<%
    List<Payment> paymentReportList = (List<Payment>) request.getAttribute("paymentReportList");

    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");

    double total = 0;
    if(paymentReportList != null){
        for(Payment p : paymentReportList){
            total += p.getAmount();
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
                        <h1 class="m-0 text-dark">Payment Report</h1>
                    </div>
                </div>

                <section class="content">
                    <div class="container-fluid">

                        <!-- FILTER FORM -->
                        <div class="card card-outline card-primary">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">Filter</h3>
                            </div>

                            <div class="card-body">
                                <form method="get" action="${pageContext.request.contextPath}/payment-report">
                                    <div class="row">

                                        <div class="col-md-3">
                                            <label>Start Date</label>
                                            <input type="date" name="startDate" value="<%=startDate%>" class="form-control">
                                        </div>

                                        <div class="col-md-3">
                                            <label>End Date</label>
                                            <input type="date" name="endDate" value="<%=endDate%>" class="form-control">
                                        </div>

                                        <div class="col-md-6 mt-4">
                                            <button type="submit" class="btn btn-primary">
                                                <i class="fas fa-search"></i> Fetch
                                            </button>

                                            <a href="${pageContext.request.contextPath}/payment-report-excel?startDate=<%=startDate%>&endDate=<%=endDate%>"
                                               class="btn btn-success">
                                                <i class="fas fa-file-excel"></i> Export Excel
                                            </a>

                                            <a href="${pageContext.request.contextPath}/payment-report-pdf?startDate=<%=startDate%>&endDate=<%=endDate%>"
                                               class="btn btn-danger">
                                                <i class="fas fa-file-pdf"></i> Export PDF
                                            </a>

                                            <a href="${pageContext.request.contextPath}/payment-settlement"
                                               class="btn btn-warning">
                                                <i class="fas fa-balance-scale"></i> Settlement Report
                                            </a>
                                        </div>

                                    </div>
                                </form>
                            </div>
                        </div>

                        <!-- REPORT TABLE -->
                        <div class="card card-outline card-success">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">Payment Report List</h3>
                            </div>

                            <div class="card-body table-responsive p-0">
                                <table class="table table-hover text-nowrap">
                                    <thead>
                                        <tr>
                                            <th>Date</th>
                                            <th>Worker</th>
                                            <th>Site</th>
                                            <th>Amount</th>
                                            <th>Mode</th>
                                            <th>Reference</th>
                                            <th>Notes</th>
                                            <th>Recorded By</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <% if(paymentReportList != null && paymentReportList.size() > 0){
                                            for(Payment p : paymentReportList){ %>

                                        <tr>
                                            <td><%=p.getPaymentDate()%></td>
                                            <td><%=p.getWorkerName()%></td>
                                            <td><%=p.getSiteName()%></td>
                                            <td class="font-weight-bold text-success">₹ <%=p.getAmount()%></td>
                                            <td><%=p.getPaymentMode()%></td>
                                            <td><%=p.getReferenceNumber()%></td>
                                            <td><%=p.getNotes()%></td>
                                            <td><%=p.getRecordedByName()%></td>
                                        </tr>

                                        <% } } else { %>
                                        <tr>
                                            <td colspan="8" class="text-center text-muted p-3">
                                                No payment data found
                                            </td>
                                        </tr>
                                        <% } %>
                                    </tbody>
                                </table>
                            </div>

                            <div class="card-footer text-right font-weight-bold">
                                Total Paid: ₹ <%=total%>
                            </div>

                        </div>

                    </div>
                </section>

            </div>

            <jsp:include page="/WEB-INF/layout/footer.jsp" />

        </div>
    </body>
</html>
