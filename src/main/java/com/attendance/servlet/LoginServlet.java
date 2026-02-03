/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.attendance.servlet;

import com.attendance.common.SessionUtils;
import com.attendance.connections.DBConnection;
import com.attendance.dbutils.UserDBUtils;
import com.attendance.dto.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author vaishnavi.dhole
 */
public class LoginServlet extends HttpServlet {

     /* ================= LOGIN PAGE ================= */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("module", "Auth");
        request.setAttribute("controller", "login");
        request.setAttribute("action", "view");
        request.setAttribute("subaction", "");

        request.getRequestDispatcher("/index.jsp")
               .forward(request, response);
    }

    /* ================= LOGIN POST ================= */
    protected void processRequest_POST(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = UserDBUtils.validate(username, password);

            if (user != null) {
                HttpSession session = request.getSession(true);
                SessionUtils.storeLoginedUser(session, user);

                if ("ADMIN".equals(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/attendance");
                }
                return;
            }

            request.setAttribute("status", 300);
            request.setAttribute("message", "Invalid username or password");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Login failed");
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
        processRequest_POST(request, response);
    }
}
