/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.attendance.servlet;

import com.attendance.common.SessionUtils;
import com.attendance.connections.DBConnection;
import com.attendance.dbutils.SiteDBUtils;
import com.attendance.dbutils.UserDBUtils;
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
import java.util.List;

/**
 *
 * @author vaishnavi.dhole
 */
public class UserServlet extends HttpServlet {

     protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"ADMIN".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        request.setAttribute("module", "Supervisors");
        request.setAttribute("controller", "supervisors");
        request.setAttribute("action", "list");
        request.setAttribute("subaction", "");

        try (Connection conn = DBConnection.PGConnection()) {

            List<User> supervisors = UserDBUtils.getSupervisors(conn);
            List<Site> sites = SiteDBUtils.getSites(conn);

            request.setAttribute("supervisorList", supervisors);
            request.setAttribute("siteList", sites);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/user/supervisor.jsp").forward(request, response);
    }

    protected void processCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DBConnection.PGConnection()) {

            User u = new User();
            u.setUsername(request.getParameter("username"));
            u.setPassword(request.getParameter("password")); // later encrypt
            u.setFullName(request.getParameter("fullName"));
            u.setSiteId(Integer.parseInt(request.getParameter("siteId")));

            UserDBUtils.addSupervisor(conn, u);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Supervisor added successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to add supervisor");
        }

        processRequest(request, response);
    }

    protected void processDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DBConnection.PGConnection()) {

            int userId = Integer.parseInt(request.getParameter("userId"));
            UserDBUtils.deleteUser(conn, userId);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Supervisor removed successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to delete supervisor");
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
            processCreate(request, response);
        } else if ("delete".equals(action)) {
            processDelete(request, response);
        } else {
            processRequest(request, response);
        }
    }
}