/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.attendance.servlet;

import com.attendance.common.SessionUtils;
import com.attendance.connections.DBConnection;
import com.attendance.dbutils.WorkerDBUtils;
import com.attendance.dto.User;
import com.attendance.dto.Worker;
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
public class WorkerServlet extends HttpServlet {

  /* ================= LIST WORKERS ================= */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"ADMIN".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        Worker workerData = new Worker();

        try (Connection conn = DBConnection.PGConnection()) {
            Worker worker = new Worker();
            List<Worker> workerList = WorkerDBUtils.workers(conn, worker);
            workerData.setList(workerList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("worker-data", workerData);

        request.setAttribute("module", "Worker");
        request.setAttribute("controller", "workers");
        request.setAttribute("action", "list");
        request.setAttribute("subaction", "");

        request.getRequestDispatcher("/user/workers.jsp")
               .forward(request, response);
    }

    /* ================= CREATE WORKER (POST) ================= */
    protected void processRequestCreate_POST(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"ADMIN".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String name = request.getParameter("workerName");
        String mobile = request.getParameter("mobile");

        try (Connection conn = DBConnection.PGConnection()) {

            Worker worker = new Worker();
            worker.setWorkerName(name);
            worker.setMobileNo(mobile);

            WorkerDBUtils.addWorker(conn, worker);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Worker added successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to add worker");
        }

        processRequest(request, response);
    }

    /* ================= DELETE / DISABLE WORKER ================= */
    protected void processRequestDisable_POST(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"ADMIN".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        int workerId = Integer.parseInt(request.getParameter("workerId"));

        try (Connection conn = DBConnection.PGConnection()) {

            WorkerDBUtils.disableWorker(conn, workerId);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Worker removed successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to remove worker");
        }

        processRequest(request, response);
    }

    /* ================= DO GET ================= */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    /* ================= DO POST ================= */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            processRequestCreate_POST(request, response);
        }

        if ("delete".equals(action)) {
            processRequestDisable_POST(request, response);
        }
    }
}
