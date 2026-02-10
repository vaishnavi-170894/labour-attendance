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
import java.util.ArrayList;
import java.util.List;

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
  public static List<User> getSupervisors(Connection conn) throws Exception {

        List<User> list = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement(
            "SELECT u.user_id, u.username, u.full_name, u.role, u.site_id, s.site_name " +
            "FROM users u " +
            "LEFT JOIN site_master s ON u.site_id=s.site_id " +
            "WHERE u.active=1 AND u.role='SUPERVISOR' " +
            "ORDER BY u.user_id DESC"
        );

        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            User u = new User();
            u.setUserId(rs.getInt("user_id"));
            u.setUsername(rs.getString("username"));
            u.setFullName(rs.getString("full_name"));
            u.setRole(rs.getString("role"));
            u.setSiteId(rs.getInt("site_id"));

//            u.setExtra1(rs.getString("site_name")); // store site name in extra field
            list.add(u);
        }

        return list;
    }

    public static void addSupervisor(Connection conn, User user) throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO users(username,password,full_name,role,site_id,active) VALUES(?,?,?,?,?,1)"
        );

        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getFullName());
        ps.setString(4, "SUPERVISOR");
        ps.setInt(5, user.getSiteId());

        ps.executeUpdate();
    }

    public static void deleteUser(Connection conn, int userId) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE users SET active=0 WHERE user_id=?"
        );
        ps.setInt(1, userId);
        ps.executeUpdate();
    }
}
