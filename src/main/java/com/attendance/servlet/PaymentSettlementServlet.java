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
import java.util.List;

/**
 *
 * @author vaishnavi.dhole
 */
public class PaymentSettlementServlet extends HttpServlet {

     @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        request.setAttribute("module", "Payments");
        request.setAttribute("controller", "payment-settlement");
        request.setAttribute("action", "list");
        request.setAttribute("subaction", "");

        try (Connection conn = DBConnection.PGConnection()) {

            List<Payment> list =
                    PaymentReportDBUtils.settlementReport(conn, login.getSiteId(), login.getRole());

            request.setAttribute("settlementList", list);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/payments/settlement.jsp").forward(request, response);
    }
}
