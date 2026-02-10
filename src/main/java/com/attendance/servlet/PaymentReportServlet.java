/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.attendance.servlet;

import com.attendance.common.SessionUtils;
import com.attendance.connections.DBConnection;
import com.attendance.dbutils.PaymentReportDBUtils;
import com.attendance.dto.Payment;
import com.attendance.dto.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author vaishnavi.dhole
 */
public class PaymentReportServlet extends HttpServlet {

     @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        if (startDate == null || startDate.isEmpty()) {
            startDate = LocalDate.now().minusDays(7).toString();
        }
        if (endDate == null || endDate.isEmpty()) {
            endDate = LocalDate.now().toString();
        }

        int siteId = 0;
        if (request.getParameter("siteId") != null && !request.getParameter("siteId").isEmpty()) {
            siteId = Integer.parseInt(request.getParameter("siteId"));
        }

        if (!"ADMIN".equals(login.getRole())) {
            siteId = login.getSiteId();
        }

        request.setAttribute("module", "Payments");
        request.setAttribute("controller", "payment-report");
        request.setAttribute("action", "list");
        request.setAttribute("subaction", "");

        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("siteId", siteId);

        try (Connection conn = DBConnection.PGConnection()) {

            List<Payment> list =
                    PaymentReportDBUtils.paymentReport(conn, startDate, endDate, siteId, login.getRole());

            request.setAttribute("paymentReportList", list);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/payments/report.jsp").forward(request, response);
    }
}