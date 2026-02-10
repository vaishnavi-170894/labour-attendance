/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.attendance.servlet;

import com.attendance.common.Constants;
import com.attendance.common.SessionUtils;
import com.attendance.connections.DBConnection;
import com.attendance.dbutils.DailyReportDBUtils;
import com.attendance.dto.DailyReport;
import com.attendance.dto.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author vaishnavi.dhole
 */
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class DailyReportServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        request.setAttribute("module", "DailyReports");
        request.setAttribute("controller", "daily-reports");
        request.setAttribute("action", "list");
        request.setAttribute("subaction", "");

        // Default filter last 7 days
        String startDate = request.getParameter("start_date");
        String endDate = request.getParameter("end_date");

        if (startDate == null || startDate.isEmpty()) {
            startDate = LocalDate.now().minusDays(7).toString();
        }
        if (endDate == null || endDate.isEmpty()) {
            endDate = LocalDate.now().toString();
        }

        request.setAttribute("start_date", startDate);
        request.setAttribute("end_date", endDate);

        try (Connection conn = DBConnection.PGConnection()) {

            List<DailyReport> reports =
                    DailyReportDBUtils.getReports(conn, startDate, endDate, login.getSiteId());

            request.setAttribute("reports", reports);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/dailyreports/index.jsp").forward(request, response);
    }

    protected void processRequestCreate_POST(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try (Connection conn = DBConnection.PGConnection()) {

            String date = request.getParameter("date");
            String reportText = request.getParameter("reportText");

            DailyReport r = new DailyReport();
            r.setReportDate(date);
            r.setReportText(reportText);
            r.setCreatedBy(login.getUserId());

            // Save report
            long reportId = DailyReportDBUtils.saveReport(conn, r);

            // Upload folder
            String basePath = (Constants.UPLOAD_BASE_PATH);
            File uploadDir = new File(basePath, "daily_reports/" + date);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Multiple images upload
            List<Part> parts = (List<Part>) request.getParts();

            for (Part p : parts) {
                if (p.getName().equals("images") && p.getSize() > 0) {

                    String fileName = "dr_" + reportId + "_" + System.currentTimeMillis() + ".jpg";
                    File savedFile = new File(uploadDir, fileName);
                    p.write(savedFile.getAbsolutePath());

                    // store relative path in DB
                    String dbPath = "daily_reports/" + date + "/" + fileName;
                    DailyReportDBUtils.saveReportPhoto(conn, reportId, dbPath);
                }
            }

            request.setAttribute("status", 200);
            request.setAttribute("message", "Daily report added successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to add report");
        }

        processRequest(request, response);
    }

    protected void processRequestDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"ADMIN".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try (Connection conn = DBConnection.PGConnection()) {

            long reportId = Long.parseLong(request.getParameter("reportId"));

            DailyReportDBUtils.deleteReport(conn, reportId);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Report deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to delete report");
        }

        processRequest(request, response);
    }

    protected void processRequestUpdate_POST(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try (Connection conn = DBConnection.PGConnection()) {

            long reportId = Long.parseLong(request.getParameter("reportId"));
            String reportText = request.getParameter("reportText");

            DailyReportDBUtils.updateReport(conn, reportId, reportText);

            // remove old photos from db
            DailyReportDBUtils.deleteAllPhotos(conn, reportId);

            String date = request.getParameter("date");

//            String basePath = getServletContext().getInitParameter(Constants.UPLOAD_BASE_PATH);
 String basePath = (Constants.UPLOAD_BASE_PATH);
            File uploadDir = new File(basePath, "daily_reports/" + date);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            List<Part> parts = (List<Part>) request.getParts();

            for (Part p : parts) {
                if (p.getName().equals("images") && p.getSize() > 0) {

                    String fileName = "dr_" + reportId + "_" + System.currentTimeMillis() + ".jpg";
                    File savedFile = new File(uploadDir, fileName);
                    p.write(savedFile.getAbsolutePath());

                    String dbPath = "daily_reports/" + date + "/" + fileName;
                    DailyReportDBUtils.saveReportPhoto(conn, reportId, dbPath);
                }
            }

            request.setAttribute("status", 200);
            request.setAttribute("message", "Report updated successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to update report");
        }

        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            processRequestCreate_POST(request, response);
        } else if ("delete".equals(action)) {
            processRequestDelete(request, response);
        } else if ("update".equals(action)) {
            processRequestUpdate_POST(request, response);
        } else {
            processRequest(request, response);
        }
    }
}
