/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.attendance.servlet;

import com.attendance.common.SessionUtils;
import com.attendance.connections.DBConnection;
import com.attendance.dbutils.SiteDBUtils;
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
public class SiteServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User login = SessionUtils.getLoginedUser(session);

        if (login == null || !"ADMIN".equals(login.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        request.setAttribute("module", "Sites");
        request.setAttribute("controller", "sites");
        request.setAttribute("action", "list");
        request.setAttribute("subaction", "");

        try (Connection conn = DBConnection.PGConnection()) {
            List<Site> sites = SiteDBUtils.getSites(conn);
            request.setAttribute("siteList", sites);
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/sites/index.jsp").forward(request, response);
    }

    protected void processCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DBConnection.PGConnection()) {

            Site s = new Site();
            s.setSiteName(request.getParameter("siteName"));
            s.setSiteLat(Double.parseDouble(request.getParameter("siteLat")));
            s.setSiteLng(Double.parseDouble(request.getParameter("siteLng")));
            s.setAllowedRadius(Integer.parseInt(request.getParameter("allowedRadius")));

            SiteDBUtils.saveSite(conn, s);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Site added successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to add site");
        }

        processRequest(request, response);
    }

    protected void processUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DBConnection.PGConnection()) {

            Site s = new Site();
            s.setSiteId(Integer.parseInt(request.getParameter("siteId")));
            s.setSiteName(request.getParameter("siteName"));
            s.setSiteLat(Double.parseDouble(request.getParameter("siteLat")));
            s.setSiteLng(Double.parseDouble(request.getParameter("siteLng")));
            s.setAllowedRadius(Integer.parseInt(request.getParameter("allowedRadius")));

            SiteDBUtils.updateSite(conn, s);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Site updated successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to update site");
        }

        processRequest(request, response);
    }

    protected void processDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DBConnection.PGConnection()) {

            int siteId = Integer.parseInt(request.getParameter("siteId"));
            SiteDBUtils.deleteSite(conn, siteId);

            request.setAttribute("status", 200);
            request.setAttribute("message", "Site deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", 300);
            request.setAttribute("message", "Unable to delete site");
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
        } else if ("update".equals(action)) {
            processUpdate(request, response);
        } else if ("delete".equals(action)) {
            processDelete(request, response);
        } else {
            processRequest(request, response);
        }
    }
}