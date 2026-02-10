/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.attendance.servlet;

import com.attendance.common.SessionUtils;
import com.attendance.connections.DBConnection;
import com.attendance.dbutils.AttendanceReportDBUtils;
import com.attendance.dbutils.SiteDBUtils;
import com.attendance.dto.Attendance;
import com.attendance.dto.Site;
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
public class AttendanceReportServlet extends HttpServlet {

   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String siteIdStr = request.getParameter("siteId");

        if (startDate == null || startDate.isEmpty()) {
            startDate = LocalDate.now().minusDays(7).toString();
        }

        if (endDate == null || endDate.isEmpty()) {
            endDate = LocalDate.now().toString();
        }

        int siteId = 0;

        if ("ADMIN".equals(login.getRole())) {
            if (siteIdStr != null && !siteIdStr.isEmpty()) {
                siteId = Integer.parseInt(siteIdStr);
            }
        } else {
            siteId = login.getSiteId();
        }

        request.setAttribute("module", "Reports");
        request.setAttribute("controller", "attendance-report");
        request.setAttribute("action", "report");
        request.setAttribute("subaction", "");

        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("siteId", siteId);

        try (Connection conn = DBConnection.PGConnection()) {

            // Site dropdown only for ADMIN
            if ("ADMIN".equals(login.getRole())) {
                List<Site> sites = SiteDBUtils.siteList(conn);
                request.setAttribute("sites", sites);
            }

            List<Attendance> reportList =
                    AttendanceReportDBUtils.getAttendanceReport(conn, startDate, endDate, siteId, login.getRole());

            request.setAttribute("reportList", reportList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/attendance/report.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}