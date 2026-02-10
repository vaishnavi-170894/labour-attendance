/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.attendance.servlet;

import com.attendance.common.SessionUtils;
import com.attendance.connections.DBConnection;
import com.attendance.dbutils.AttendanceDBUtils;
import com.attendance.dbutils.DashboardDBUtils;
import com.attendance.dbutils.WorkerDBUtils;
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
import java.util.Map;

/**
 *
 * @author vaishnavi.dhole
 */
public class DashboardServlet extends HttpServlet {

     protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = SessionUtils.getLoginedUser(session);

        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        request.setAttribute("module", "Dashboard");
        request.setAttribute("controller", "dashboard");
        request.setAttribute("action", "view");
        request.setAttribute("subaction", "");

        try (Connection conn = DBConnection.PGConnection()) {

            int totalWorkers = DashboardDBUtils.getTotalWorkers(conn);
            int todayAttendance = DashboardDBUtils.getTodayAttendance(conn);

            double todayWage = DashboardDBUtils.getTodayWage(conn);
            double monthWage = DashboardDBUtils.getMonthWage(conn);

            int totalSites = DashboardDBUtils.getTotalSites(conn);
            int totalSupervisors = DashboardDBUtils.getTotalSupervisors(conn);

            double monthPayment = DashboardDBUtils.getMonthPayment(conn);
            double pendingBalance = DashboardDBUtils.getPendingBalance(conn);

            List<Map<String, Object>> recentAttendance = DashboardDBUtils.getRecentAttendance(conn);

            request.setAttribute("total_workers", totalWorkers);
            request.setAttribute("today_attendance", todayAttendance);
            request.setAttribute("today_wage", todayWage);
            request.setAttribute("month_wage", monthWage);

            // extra features
            request.setAttribute("total_sites", totalSites);
            request.setAttribute("total_supervisors", totalSupervisors);
            request.setAttribute("month_payment", monthPayment);
            request.setAttribute("pending_balance", pendingBalance);

            request.setAttribute("recent_attendance", recentAttendance);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}