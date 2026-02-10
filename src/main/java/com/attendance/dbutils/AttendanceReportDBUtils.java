/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.dbutils;

import com.attendance.dto.Attendance;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vaishnavi.dhole
 */
public class AttendanceReportDBUtils {
 public static List<Attendance> getAttendanceReport(Connection conn,
            String startDate, String endDate, int siteId, String role) throws Exception {

        List<Attendance> list = new ArrayList<>();

        String sql =
                "SELECT a.attendance_id, a.attendance_date, a.day_type, a.wage, a.photo_path, " +
                "w.worker_name, wt.work_type_name, u.full_name AS supervisor_name, s.site_name " +
                "FROM attendance a " +
                "INNER JOIN worker_master w ON a.worker_id=w.worker_id " +
                "INNER JOIN work_type wt ON a.work_type_id=wt.work_type_id " +
                "INNER JOIN users u ON a.user_id=u.user_id " +
                "INNER JOIN site_master s ON a.site_id=s.site_id " +
                "WHERE a.attendance_date BETWEEN ? AND ? ";

        if (!"ADMIN".equals(role)) {
            sql += " AND a.site_id=? ";
        } else {
            if (siteId > 0) {
                sql += " AND a.site_id=? ";
            }
        }

        sql += " ORDER BY a.attendance_date DESC, a.attendance_id DESC";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setDate(1, java.sql.Date.valueOf(startDate));
        ps.setDate(2, java.sql.Date.valueOf(endDate));

        if (!"ADMIN".equals(role)) {
            ps.setInt(3, siteId);
        } else {
            if (siteId > 0) {
                ps.setInt(3, siteId);
            }
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Attendance a = new Attendance();

            a.setAttendanceId(rs.getLong("attendance_id"));
            a.setAttendanceDate(rs.getDate("attendance_date").toString());
            a.setDayType(rs.getString("day_type"));
            a.setWage(rs.getDouble("wage"));
            a.setPhotoPath(rs.getString("photo_path"));

            a.setWorkerName(rs.getString("worker_name"));
            a.setWorkTypeName(rs.getString("work_type_name"));
            a.setSupervisorName(rs.getString("supervisor_name"));

            // Extra field in Attendance DTO if you want:
            // a.setSiteName(rs.getString("site_name"));

            list.add(a);
        }

        return list;
    }   
}
