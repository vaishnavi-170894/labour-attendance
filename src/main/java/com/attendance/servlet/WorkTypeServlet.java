/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.attendance.servlet;

import com.attendance.common.SessionUtils;
import com.attendance.connections.DBConnection;
import com.attendance.dbutils.WorkTypeDBUtils;
import com.attendance.dto.User;
import com.attendance.dto.WorkType;
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
public class WorkTypeServlet extends HttpServlet {

    /* ================= LIST ================= */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"ADMIN".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        request.setAttribute("module", "WorkTypes");
        request.setAttribute("controller", "work-types");
        request.setAttribute("action", "list");
        request.setAttribute("subaction", "");

        try (Connection conn = DBConnection.PGConnection()) {

            List<WorkType> list = WorkTypeDBUtils.workTypes(conn);
            request.setAttribute("workTypes", list);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/worktypes/index.jsp").forward(request, response);
    }

    /* ================= EDIT GET ================= */
    protected void processRequestEdit_GET(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"ADMIN".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        request.setAttribute("module", "WorkTypes");
        request.setAttribute("controller", "work-types");
        request.setAttribute("action", "edit");
        request.setAttribute("subaction", "");

        try (Connection conn = DBConnection.PGConnection()) {

            int id = Integer.parseInt(request.getParameter("id"));
            WorkType wt = WorkTypeDBUtils.getById(conn, id);

            request.setAttribute("workType", wt);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/worktypes/edit.jsp").forward(request, response);
    }

    /* ================= CREATE ================= */
    protected void processRequestCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DBConnection.PGConnection()) {

            String name = request.getParameter("name");
            double fullDay = Double.parseDouble(request.getParameter("full_day_rate"));
            double halfDay = Double.parseDouble(request.getParameter("half_day_rate"));

            WorkType wt = new WorkType();
            wt.setWorkTypeName(name);
            wt.setFullDayWage(fullDay);
            wt.setHalfDayWage(halfDay);

            WorkTypeDBUtils.addWorkType(conn, wt);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Work type added successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to add work type");
        }

        processRequest(request, response);
    }

    /* ================= UPDATE ================= */
    protected void processRequestUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DBConnection.PGConnection()) {

            int id = Integer.parseInt(request.getParameter("workTypeId"));
            String name = request.getParameter("name");
            double fullDay = Double.parseDouble(request.getParameter("full_day_rate"));
            double halfDay = Double.parseDouble(request.getParameter("half_day_rate"));

            WorkType wt = new WorkType();
            wt.setWorkTypeId(id);
            wt.setWorkTypeName(name);
            wt.setFullDayWage(fullDay);
            wt.setHalfDayWage(halfDay);

            WorkTypeDBUtils.updateWorkType(conn, wt);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Work type updated successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to update work type");
        }

        processRequest(request, response);
    }

    /* ================= DELETE ================= */
    protected void processRequestDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DBConnection.PGConnection()) {

            int id = Integer.parseInt(request.getParameter("workTypeId"));
            WorkTypeDBUtils.deleteWorkType(conn, id);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Work type deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to delete work type");
        }

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
            processRequestUpdate(request, response);
        } else if ("delete".equals(action)) {
            processRequestDelete(request, response);
        } else {
            processRequest(request, response);
        }
    }
}