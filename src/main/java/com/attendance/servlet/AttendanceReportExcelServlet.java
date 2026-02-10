/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.attendance.servlet;

import com.attendance.common.SessionUtils;
import com.attendance.connections.DBConnection;
import com.attendance.dbutils.AttendanceReportDBUtils;
import com.attendance.dto.Attendance;
import com.attendance.dto.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author vaishnavi.dhole
 */
@WebServlet(name = "AttendanceReportExcelServlet", urlPatterns = {"/attendance-report-excel"})
public class AttendanceReportExcelServlet extends HttpServlet {

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
        int siteId = Integer.parseInt(request.getParameter("siteId"));

        if (!"ADMIN".equals(login.getRole())) {
            siteId = login.getSiteId();
        }

        try (Connection conn = DBConnection.PGConnection()) {

            List<Attendance> list =
                    AttendanceReportDBUtils.getAttendanceReport(conn, startDate, endDate, siteId, login.getRole());

            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("Attendance Report");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Date");
            header.createCell(1).setCellValue("Worker");
            header.createCell(2).setCellValue("Work Type");
            header.createCell(3).setCellValue("Day Type");
            header.createCell(4).setCellValue("Wage");
            header.createCell(5).setCellValue("Supervisor");
            header.createCell(6).setCellValue("Photo Path");

            int rowNum = 1;
            for (Attendance a : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(a.getAttendanceDate());
                row.createCell(1).setCellValue(a.getWorkerName());
                row.createCell(2).setCellValue(a.getWorkTypeName());
                row.createCell(3).setCellValue(a.getDayType());
                row.createCell(4).setCellValue(a.getWage());
                row.createCell(5).setCellValue(a.getSupervisorName());
                row.createCell(6).setCellValue(a.getPhotoPath());
            }

            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            response.setContentType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition",
                    "attachment; filename=attendance_report.xlsx");

            wb.write(response.getOutputStream());
            wb.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
