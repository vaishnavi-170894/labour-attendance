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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vaishnavi.dhole
 */
public class AttendanceDBUtils {
 public static double getWage(Connection conn, int workTypeId, String dayType)
            throws Exception {

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

    public static boolean checkAlreadyMarked(Connection conn, String date, int workerId, int siteId)
            throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "SELECT attendance_id FROM attendance WHERE attendance_date=? AND worker_id=? AND site_id=?"
        );

        ps.setDate(1, java.sql.Date.valueOf(date));
        ps.setInt(2, workerId);
        ps.setInt(3, siteId);

        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public static void saveAttendance(Connection conn, Attendance att)
            throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO attendance " +
            "(attendance_date, worker_id, site_id, user_id, work_type_id, day_type, wage, " +
            "photo_path, latitude, longitude, gps_accuracy) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?)"
        );

        ps.setDate(1, java.sql.Date.valueOf(att.getAttendanceDate()));
        ps.setInt(2, att.getWorkerId());
        ps.setInt(3, att.getSiteId());
        ps.setInt(4, att.getUserId());
        ps.setInt(5, att.getWorkTypeId());
        ps.setString(6, att.getDayType());
        ps.setDouble(7, att.getWage());
        ps.setString(8, att.getPhotoPath());
        ps.setDouble(9, att.getLatitude());
        ps.setDouble(10, att.getLongitude());
        ps.setDouble(11, att.getAccuracy());

        ps.executeUpdate();
    }

    public static List<Attendance> attendanceListByDate(Connection conn, String date, int siteId)
            throws Exception {

        List<Attendance> list = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement(
            "SELECT a.attendance_id, a.attendance_date, a.day_type, a.wage, a.photo_path, " +
            "w.worker_name, wt.work_type_name " +
            "FROM attendance a " +
            "INNER JOIN worker_master w ON a.worker_id=w.worker_id " +
            "INNER JOIN work_type wt ON a.work_type_id=wt.work_type_id " +
            "WHERE a.site_id=? AND a.attendance_date=? " +
            "ORDER BY a.attendance_id DESC"
        );

        ps.setInt(1, siteId);
        ps.setDate(2, java.sql.Date.valueOf(date));

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

            list.add(a);
        }

        return list;
    }

    public static Attendance getById(Connection conn, long attendanceId)
            throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "SELECT attendance_id, attendance_date, worker_id, work_type_id, day_type, wage, photo_path " +
            "FROM attendance WHERE attendance_id=?"
        );

        ps.setLong(1, attendanceId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Attendance a = new Attendance();
            a.setAttendanceId(rs.getLong("attendance_id"));
            a.setAttendanceDate(rs.getDate("attendance_date").toString());
            a.setWorkerId(rs.getInt("worker_id"));
            a.setWorkTypeId(rs.getInt("work_type_id"));
            a.setDayType(rs.getString("day_type"));
            a.setWage(rs.getDouble("wage"));
            a.setPhotoPath(rs.getString("photo_path"));
            return a;
        }

        return null;
    }

    public static void updateAttendance(Connection conn, Attendance att)
            throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "UPDATE attendance SET worker_id=?, work_type_id=?, day_type=?, wage=?, photo_path=? " +
            "WHERE attendance_id=?"
        );

        ps.setInt(1, att.getWorkerId());
        ps.setInt(2, att.getWorkTypeId());
        ps.setString(3, att.getDayType());
        ps.setDouble(4, att.getWage());
        ps.setString(5, att.getPhotoPath());
        ps.setLong(6, att.getAttendanceId());

        ps.executeUpdate();
    }

    public static void deleteAttendance(Connection conn, long attendanceId)
            throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "DELETE FROM attendance WHERE attendance_id=?"
        );

        ps.setLong(1, attendanceId);
        ps.executeUpdate();
    }
}