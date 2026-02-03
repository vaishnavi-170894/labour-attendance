/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.dbutils;

import com.attendance.connections.DBConnection;
import com.attendance.dto.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author vaishnavi.dhole
 */
public class UserDBUtils {
  public static User validate(String username, String password)
            throws SQLException, ClassNotFoundException {

        User user = null;

        try (Connection conn = DBConnection.PGConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "SELECT user_id, username, full_name, role, site_id, active " +
                "FROM users WHERE username=? AND password=? AND active=1"
            );
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));
                user.setSiteId(rs.getObject("site_id") != null
                        ? rs.getInt("site_id") : null);
                user.setActive(1);
            }
        }
        return user;
    }  
}
