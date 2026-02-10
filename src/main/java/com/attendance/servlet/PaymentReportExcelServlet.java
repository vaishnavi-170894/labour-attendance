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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author vaishnavi.dhole
 */
public class PaymentReportExcelServlet extends HttpServlet {

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

        int siteId = login.getSiteId();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=payment_report.xlsx");

        try (Connection conn = DBConnection.PGConnection();
             Workbook workbook = new XSSFWorkbook()) {

            List<Payment> list = PaymentReportDBUtils.paymentReport(conn, startDate, endDate, siteId, login.getRole());

            Sheet sheet = workbook.createSheet("Payment Report");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Date");
            header.createCell(1).setCellValue("Worker");
            header.createCell(2).setCellValue("Site");
            header.createCell(3).setCellValue("Amount");
            header.createCell(4).setCellValue("Mode");
            header.createCell(5).setCellValue("Reference");
            header.createCell(6).setCellValue("Notes");
            header.createCell(7).setCellValue("Recorded By");

            int rowIndex = 1;
            double total = 0;

            for (Payment p : list) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(p.getPaymentDate());
                row.createCell(1).setCellValue(p.getWorkerName());
                row.createCell(2).setCellValue(p.getSiteName());
                row.createCell(3).setCellValue(p.getAmount());
                row.createCell(4).setCellValue(p.getPaymentMode());
                row.createCell(5).setCellValue(p.getReferenceNumber());
                row.createCell(6).setCellValue(p.getNotes());
                row.createCell(7).setCellValue(p.getRecordedByName());

                total += p.getAmount();
            }

            Row totalRow = sheet.createRow(rowIndex + 1);
            totalRow.createCell(2).setCellValue("TOTAL");
            totalRow.createCell(3).setCellValue(total);

            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}