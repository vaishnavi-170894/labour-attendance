/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.dbutils;

import com.attendance.dto.Attendance;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author vaishnavi.dhole
 */
public class AttendanceDBUtils {
  public static double getWage(Connection conn, int workTypeId, String dayType)
            throws SQLException {

        PreparedStatement ps = conn.prepareStatement(
            "SELECT full_day_wage, half_day_wage FROM work_type WHERE work_type_id=?"
        );
        ps.setInt(1, workTypeId);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return "FULL".equals(dayType)
                ? rs.getDouble("full_day_wage")
                : rs.getDouble("half_day_wage");
    }

    public static void saveAttendance(Connection conn, Attendance att)
            throws SQLException {

        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO attendance " +
            "(attendance_date, worker_id, site_id, user_id, work_type_id, day_type, wage, " +
            "photo_path, latitude, longitude, gps_accuracy) " +
            "VALUES (CURRENT_DATE,?,?,?,?,?,?,?,?,?,?)"
        );

        ps.setInt(1, att.getWorkerId());
        ps.setInt(2, att.getSiteId());
        ps.setInt(3, att.getUserId());
        ps.setInt(4, att.getWorkTypeId());
        ps.setString(5, att.getDayType());
        ps.setDouble(6, att.getWage());
        ps.setString(7, att.getPhotoPath());
        ps.setDouble(8, att.getLatitude());
        ps.setDouble(9, att.getLongitude());
        ps.setDouble(10, att.getAccuracy());

        ps.executeUpdate();
    }
}
