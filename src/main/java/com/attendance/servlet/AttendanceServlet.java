/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.attendance.servlet;

import com.attendance.common.Constants;
import com.attendance.common.SessionUtils;
import com.attendance.connections.DBConnection;
import com.attendance.dbutils.AttendanceDBUtils;
import com.attendance.dbutils.WorkTypeDBUtils;
import com.attendance.dbutils.WorkerDBUtils;
import com.attendance.dto.Attendance;
import com.attendance.dto.User;
import com.attendance.dto.WorkType;
import com.attendance.dto.Worker;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
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
public class AttendanceServlet extends HttpServlet {
   /* ================= LIST (SUPERVISOR HOME) ================= */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"SUPERVISOR".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        request.setAttribute("module", "Attendance");
        request.setAttribute("controller", "attendance");
        request.setAttribute("action", "list");
        request.setAttribute("subaction", "");

        request.getRequestDispatcher("/attendance/index.jsp")
               .forward(request, response);
    }

    /* ================= CREATE (GET) ================= */
    protected void processRequestCreate_GET(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"SUPERVISOR".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try (Connection conn = DBConnection.PGConnection()) {

            // 🔹 Worker list
            Worker w = new Worker();
            List<Worker> workerList = WorkerDBUtils.workers(conn, w);
            System.err.println("================"+workerList);
            request.setAttribute("workerList", workerList);

            // 🔹 Work type list
            List<WorkType> workTypeList = WorkTypeDBUtils.workTypes(conn);
            request.setAttribute("workTypeList", workTypeList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("module", "Attendance");
        request.setAttribute("controller", "attendance");
        request.setAttribute("action", "create");
        request.setAttribute("subaction", "");

        request.getRequestDispatcher("/attendance/create.jsp")
               .forward(request, response);
    }

    /* ================= CREATE (POST) ================= */
    protected void processRequestCreate_POST(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"SUPERVISOR".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try (Connection conn = DBConnection.PGConnection()) {

            // 🔹 Read form values
            int workerId = Integer.parseInt(request.getParameter("workerId"));
            int workTypeId = Integer.parseInt(request.getParameter("workTypeId"));
            String dayType = request.getParameter("dayType");

            double latitude = Double.parseDouble(request.getParameter("latitude"));
            double longitude = Double.parseDouble(request.getParameter("longitude"));
            double accuracy = Double.parseDouble(request.getParameter("accuracy"));

            // 🔹 Photo upload
            Part photo = request.getPart("photo");

            String basePath = getServletContext()
                    .getInitParameter(Constants.UPLOAD_BASE_PATH);

            String today = LocalDate.now().toString();
            File uploadDir = new File(basePath, today);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = "w_" + workerId + "_" + System.currentTimeMillis() + ".jpg";
            File savedFile = new File(uploadDir, fileName);
            photo.write(savedFile.getAbsolutePath());

            String dbPhotoPath = today + "/" + fileName;

            // 🔹 Prepare DTO
            Attendance att = new Attendance();
            att.setWorkerId(workerId);
            att.setWorkTypeId(workTypeId);
            att.setDayType(dayType);
            att.setLatitude(latitude);
            att.setLongitude(longitude);
            att.setAccuracy(accuracy);
            att.setPhotoPath(dbPhotoPath);
            att.setUserId(login.getUserId());
            att.setSiteId(login.getSiteId());

            // 🔹 Calculate wage
            double wage = AttendanceDBUtils.getWage(conn, workTypeId, dayType);
            att.setWage(wage);

            // 🔹 Save attendance
            AttendanceDBUtils.saveAttendance(conn, att);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Attendance marked successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to mark attendance");
        }

        // 🔁 Show list page with message
        processRequest(request, response);
    }

    /* ================= DO GET ================= */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            processRequestCreate_GET(request, response);
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
            processRequestCreate_POST(request, response);
        }
    }
}