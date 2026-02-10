/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.attendance.servlet;

import com.attendance.common.SessionUtils;
import com.attendance.connections.DBConnection;
import com.attendance.dbutils.PaymentDBUtils;
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
public class PaymentServlet extends HttpServlet {

   /* ================= LIST SUMMARY ================= */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        request.setAttribute("module", "Payments");
        request.setAttribute("controller", "payments");
        request.setAttribute("action", "list");
        request.setAttribute("subaction", "");

        try (Connection conn = DBConnection.PGConnection()) {

            List<Payment> summary =
                    PaymentDBUtils.paymentSummary(conn, login.getSiteId(), login.getRole());

            request.setAttribute("paymentSummary", summary);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/payments/index.jsp").forward(request, response);
    }

    /* ================= WORKER HISTORY ================= */
    protected void processRequestHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        long workerId = Long.parseLong(request.getParameter("workerId"));

        try (Connection conn = DBConnection.PGConnection()) {

            List<Payment> history = PaymentDBUtils.paymentHistory(conn, (int) workerId);

            request.setAttribute("historyList", history);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/payments/history.jsp").forward(request, response);
    }

    /* ================= CREATE PAYMENT ================= */
    protected void processRequestCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try (Connection conn = DBConnection.PGConnection()) {

            Payment p = new Payment();
            p.setWorkerId(Integer.parseInt(request.getParameter("workerId")));
            p.setAmount(Double.parseDouble(request.getParameter("amount")));
            p.setPaymentDate(request.getParameter("paymentDate"));
            p.setPaymentMode(request.getParameter("paymentMode"));
            p.setReferenceNumber(request.getParameter("referenceNumber"));
            p.setNotes(request.getParameter("notes"));

            p.setSiteId(login.getSiteId());
            p.setRecordedBy(login.getUserId());

            PaymentDBUtils.savePayment(conn, p);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Payment recorded successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to record payment");
        }

        processRequest(request, response);
    }

    /* ================= DELETE PAYMENT (ADMIN ONLY) ================= */
    protected void processRequestDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"ADMIN".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try (Connection conn = DBConnection.PGConnection()) {

            long paymentId = Long.parseLong(request.getParameter("paymentId"));
            PaymentDBUtils.deletePayment(conn, paymentId);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Payment deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to delete payment");
        }

        processRequest(request, response);
    }

    /* ================= DO GET ================= */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("history".equals(action)) {
            processRequestHistory(request, response);
        } else {
            processRequest(request, response);
        }
    }

    /* ================= DO POST ================= */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            processRequestCreate(request, response);
        } else if ("delete".equals(action)) {
            processRequestDelete(request, response);
        } else {
            processRequest(request, response);
        }
    }
}
