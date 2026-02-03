/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.attendance.servlet;

import com.attendance.connections.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author vaishnavi.dhole
 */
public class AdminWorkerServlet extends HttpServlet {

  
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = req.getParameter("action");

        try (Connection con = DBConnection.PGConnection()) {

            // ✅ ADD WORKER
            if ("ADD".equals(action)) {

                String name = req.getParameter("workerName");
                String mobile = req.getParameter("mobile");

                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO worker_master (worker_name, mobile_no, active) " +
                    "VALUES (?, ?, true)"
                );
                ps.setString(1, name);
                ps.setString(2, mobile);
                ps.executeUpdate();
            }

            // ❌ DELETE WORKER (soft delete)
            if ("DELETE".equals(action)) {

                int workerId = Integer.parseInt(req.getParameter("workerId"));

                PreparedStatement ps = con.prepareStatement(
                    "UPDATE worker_master SET active=false WHERE worker_id=?"
                );
                ps.setInt(1, workerId);
                ps.executeUpdate();
            }

            resp.sendRedirect(req.getContextPath() + "/admin/workers.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500);
        }
    }

}
