<%-- 
    Document   : history
    Created on : 10-Feb-2026, 2:38:25 pm
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

    List<Payment> historyList = (List<Payment>) request.getAttribute("historyList");

    String workerId = request.getParameter("workerId");
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
                        <h1 class="m-0 text-dark">Payment History</h1>
                    </div>
                </div>

                <section class="content">
                    <div class="container-fluid">

                        <!-- ADD PAYMENT FORM -->
                        <div class="card card-outline card-success">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">
                                    Record Payment
                                </h3>
                            </div>

                            <div class="card-body">
                                <form method="post" action="${pageContext.request.contextPath}/payments">
                                    <input type="hidden" name="action" value="create">
                                    <input type="hidden" name="workerId" value="<%=workerId%>">

                                    <div class="row">
                                        <div class="col-md-3">
                                            <label>Amount</label>
                                            <input type="number" step="0.01" name="amount" class="form-control" required>
                                        </div>

                                        <div class="col-md-3">
                                            <label>Payment Date</label>
                                            <input type="date" name="paymentDate" class="form-control" required>
                                        </div>

                                        <div class="col-md-3">
                                            <label>Mode</label>
                                            <select name="paymentMode" class="form-control">
                                                <option value="cash">Cash</option>
                                                <option value="upi">UPI</option>
                                                <option value="online">Online Transfer</option>
                                                <option value="bank_transfer">Bank Transfer</option>
                                            </select>
                                        </div>

                                        <div class="col-md-3">
                                            <label>Reference No</label>
                                            <input type="text" name="referenceNumber" class="form-control">
                                        </div>
                                    </div>

                                    <div class="row mt-3">
                                        <div class="col-md-12">
                                            <label>Notes</label>
                                            <textarea name="notes" class="form-control"></textarea>
                                        </div>
                                    </div>

                                    <button type="submit" class="btn btn-success mt-3">
                                        <i class="fas fa-save"></i> Save Payment
                                    </button>

                                    <a href="${pageContext.request.contextPath}/payments" class="btn btn-secondary mt-3">
                                        Back
                                    </a>
                                </form>
                            </div>
                        </div>

                        <!-- HISTORY TABLE -->
                        <div class="card card-outline card-primary">
                            <div class="card-header">
                                <h3 class="card-title font-weight-bold">Payment History</h3>
                            </div>

                            <div class="card-body table-responsive p-0">
                                <table class="table table-hover text-nowrap">
                                    <thead>
                                        <tr>
                                            <th>Date</th>
                                            <th>Amount</th>
                                            <th>Mode</th>
                                            <th>Reference</th>
                                            <th>Notes</th>
                                            <th>Recorded By</th>

                                            <% if ("ADMIN".equals(login.getRole())) { %>
                                            <th class="text-center">Action</th>
                                            <% } %>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <% if (historyList != null && historyList.size() > 0) {
                                            for (Payment p : historyList) { %>

                                        <tr>
                                            <td><%=p.getPaymentDate()%></td>
                                            <td class="font-weight-bold text-success">₹ <%=p.getAmount()%></td>
                                            <td><%=p.getPaymentMode()%></td>
                                            <td><%=p.getReferenceNumber()%></td>
                                            <td><%=p.getNotes()%></td>
                                            <td><%=p.getRecordedByName()%></td>

                                            <% if ("ADMIN".equals(login.getRole())) { %>
                                            <td class="text-center">
                                                <form method="post" action="${pageContext.request.contextPath}/payments" style="display:inline;">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="paymentId" value="<%=p.getPaymentId()%>">
                                                    <button type="submit" class="btn btn-danger btn-sm"
                                                            onclick="return confirm('Delete payment?')">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
                                                </form>
                                            </td>
                                            <% } %>

                                        </tr>

                                        <% } } else { %>
                                        <tr>
                                            <td colspan="7" class="text-center text-muted p-3">
                                                No payment history
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
