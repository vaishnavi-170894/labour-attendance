/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.common;

import com.attendance.dto.User;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author vaishnavi.dhole
 */
public class SessionUtils {
     public static void storeLoginedUser(HttpSession session, User login) {
        // On the JSP can access via ${LOGIN}
        session.setAttribute("LOGIN", login);
    }

    public static User getLoginedUser(HttpSession session) {
        return (User) session.getAttribute("LOGIN");
    }
     /* ================= LOGOUT ================= */
    public static void logout(HttpSession session) {
        if (session != null) {
            session.removeAttribute("LOGIN");
            session.invalidate();   // 🔴 IMPORTANT
        }
    }
}
