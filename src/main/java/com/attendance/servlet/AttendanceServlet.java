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
   /* ================= MARK PAGE (LIST + FORM) ================= */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"SUPERVISOR".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String selectedDate = request.getParameter("date");
        if (selectedDate == null || selectedDate.isEmpty()) {
            selectedDate = LocalDate.now().toString();
        }

        request.setAttribute("selectedDate", selectedDate);

        request.setAttribute("module", "Attendance");
        request.setAttribute("controller", "attendance");
        request.setAttribute("action", "mark");
        request.setAttribute("subaction", "");

        try (Connection conn = DBConnection.PGConnection()) {

            // Worker list
            Worker w = new Worker();
            List<Worker> workerList = WorkerDBUtils.workers(conn, w);
            request.setAttribute("workerList", workerList);

            // Work type list
            List<WorkType> workTypeList = WorkTypeDBUtils.workTypes(conn);
            request.setAttribute("workTypeList", workTypeList);

            // Attendance list by date
            List<Attendance> attendanceList =
                    AttendanceDBUtils.attendanceListByDate(conn, selectedDate, login.getSiteId());

            request.setAttribute("attendanceList", attendanceList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/attendance/mark.jsp").forward(request, response);
    }

    /* ================= CREATE ================= */
    protected void processRequestCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"SUPERVISOR".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String date = request.getParameter("attendanceDate");

        try (Connection conn = DBConnection.PGConnection()) {

            int workerId = Integer.parseInt(request.getParameter("workerId"));
            int workTypeId = Integer.parseInt(request.getParameter("workTypeId"));
            String dayType = request.getParameter("dayType");

            double latitude = Double.parseDouble(request.getParameter("latitude"));
            double longitude = Double.parseDouble(request.getParameter("longitude"));
            double accuracy = Double.parseDouble(request.getParameter("accuracy"));

            // check already marked
            if (AttendanceDBUtils.checkAlreadyMarked(conn, date, workerId, login.getSiteId())) {
                request.setAttribute("status", 300);
                request.setAttribute("message", "Attendance already marked for this worker on selected date.");
                request.setAttribute("date", date);
                processRequest(request, response);
                return;
            }

            Part photo = request.getPart("photo");

//            String basePath = getServletContext().getInitParameter(Constants.UPLOAD_BASE_PATH);
 String basePath = (Constants.UPLOAD_BASE_PATH);
            File uploadDir = new File(basePath, "attendance/" + date);

            if (!uploadDir.exists())
            {uploadDir.mkdirs();}

            String fileName = "att_" + workerId + "_" + System.currentTimeMillis() + ".jpg";
            File savedFile = new File(uploadDir, fileName);
            photo.write(savedFile.getAbsolutePath());

            String dbPhotoPath = "attendance/" + date + "/" + fileName;

            double wage = AttendanceDBUtils.getWage(conn, workTypeId, dayType);

            Attendance att = new Attendance();
            att.setAttendanceDate(date);
            att.setWorkerId(workerId);
            att.setWorkTypeId(workTypeId);
            att.setDayType(dayType);
            att.setLatitude(latitude);
            att.setLongitude(longitude);
            att.setAccuracy(accuracy);
            att.setPhotoPath(dbPhotoPath);
            att.setUserId(login.getUserId());
            att.setSiteId(login.getSiteId());
            att.setWage(wage);

            AttendanceDBUtils.saveAttendance(conn, att);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Attendance marked successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to mark attendance.");
        }

        request.setAttribute("date", date);
        processRequest(request, response);
    }

    /* ================= EDIT GET ================= */
    protected void processRequestEdit_GET(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"SUPERVISOR".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        long attendanceId = Long.parseLong(request.getParameter("id"));

        request.setAttribute("module", "Attendance");
        request.setAttribute("controller", "attendance");
        request.setAttribute("action", "edit");
        request.setAttribute("subaction", "");

        try (Connection conn = DBConnection.PGConnection()) {

            Attendance att = AttendanceDBUtils.getById(conn, attendanceId);
            request.setAttribute("attendanceData", att);

            Worker w = new Worker();
            List<Worker> workerList = WorkerDBUtils.workers(conn, w);
            request.setAttribute("workerList", workerList);

            List<WorkType> workTypeList = WorkTypeDBUtils.workTypes(conn);
            request.setAttribute("workTypeList", workTypeList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/attendance/edit.jsp").forward(request, response);
    }

    /* ================= UPDATE POST ================= */
    protected void processRequestUpdate_POST(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"SUPERVISOR".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try (Connection conn = DBConnection.PGConnection()) {

            long attendanceId = Long.parseLong(request.getParameter("attendanceId"));
            int workerId = Integer.parseInt(request.getParameter("workerId"));
            int workTypeId = Integer.parseInt(request.getParameter("workTypeId"));
            String dayType = request.getParameter("dayType");

            Attendance old = AttendanceDBUtils.getById(conn, attendanceId);

            String photoPath = old.getPhotoPath();

            Part photo = request.getPart("photo");
            if (photo != null && photo.getSize() > 0) {

                String date = old.getAttendanceDate();
//                String basePath = getServletContext().getInitParameter(Constants.UPLOAD_BASE_PATH);
 String basePath = (Constants.UPLOAD_BASE_PATH);
                File uploadDir = new File(basePath, "attendance/" + date);

                if (!uploadDir.exists()) uploadDir.mkdirs();

                String fileName = "att_" + workerId + "_" + System.currentTimeMillis() + ".jpg";
                File savedFile = new File(uploadDir, fileName);
                photo.write(savedFile.getAbsolutePath());

                photoPath = "attendance/" + date + "/" + fileName;
            }

            double wage = AttendanceDBUtils.getWage(conn, workTypeId, dayType);

            Attendance att = new Attendance();
            att.setAttendanceId(attendanceId);
            att.setWorkerId(workerId);
            att.setWorkTypeId(workTypeId);
            att.setDayType(dayType);
            att.setWage(wage);
            att.setPhotoPath(photoPath);

            AttendanceDBUtils.updateAttendance(conn, att);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Attendance updated successfully");

            request.setAttribute("date", old.getAttendanceDate());

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to update attendance");
        }

        processRequest(request, response);
    }

    /* ================= DELETE POST ================= */
    protected void processRequestDelete_POST(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"SUPERVISOR".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String date = request.getParameter("date");

        try (Connection conn = DBConnection.PGConnection()) {

            long attendanceId = Long.parseLong(request.getParameter("attendanceId"));
            AttendanceDBUtils.deleteAttendance(conn, attendanceId);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Attendance deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to delete attendance");
        }

        request.setAttribute("date", date);
        processRequest(request, response);
    }

    /* ================= DO GET ================= */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            processRequestEdit_GET(request, response);
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
        } else if ("update".equals(action)) {
            processRequestUpdate_POST(request, response);
        } else if ("delete".equals(action)) {
            processRequestDelete_POST(request, response);
        }
    }
}