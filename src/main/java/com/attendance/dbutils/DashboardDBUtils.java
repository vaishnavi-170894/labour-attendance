/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.dbutils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vaishnavi.dhole
 */
public class DashboardDBUtils {
   // Total workers
    public static int getTotalWorkers(Connection conn) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT COUNT(*) AS total FROM worker_master WHERE active=1"
        );
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("total");
    }

    // Total sites
    public static int getTotalSites(Connection conn) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT COUNT(*) AS total FROM site_master WHERE active=1"
        );
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("total");
    }

    // Total supervisors
    public static int getTotalSupervisors(Connection conn) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT COUNT(*) AS total FROM users WHERE active=1 AND role='SUPERVISOR'"
        );
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("total");
    }

    // Today's attendance count
    public static int getTodayAttendance(Connection conn) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT COUNT(*) AS total FROM attendance WHERE attendance_date=CURRENT_DATE"
        );
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("total");
    }

    // Today's total wage
    public static double getTodayWage(Connection conn) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT COALESCE(SUM(wage),0) AS total FROM attendance WHERE attendance_date=CURRENT_DATE"
        );
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getDouble("total");
    }

    // This month total wage
    public static double getMonthWage(Connection conn) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT COALESCE(SUM(wage),0) AS total " +
            "FROM attendance " +
            "WHERE DATE_TRUNC('month', attendance_date)=DATE_TRUNC('month', CURRENT_DATE)"
        );
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getDouble("total");
    }

    // This month total payment
    public static double getMonthPayment(Connection conn) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT COALESCE(SUM(amount),0) AS total " +
            "FROM payment_master " +
            "WHERE active=1 " +
            "AND DATE_TRUNC('month', payment_date)=DATE_TRUNC('month', CURRENT_DATE)"
        );
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getDouble("total");
    }

    // Overall pending balance = wages - payments
    public static double getPendingBalance(Connection conn) throws Exception {

        double wages = 0;
        double paid = 0;

        PreparedStatement ps1 = conn.prepareStatement(
            "SELECT COALESCE(SUM(wage),0) AS total FROM attendance"
        );
        ResultSet rs1 = ps1.executeQuery();
        if (rs1.next()) wages = rs1.getDouble("total");

        PreparedStatement ps2 = conn.prepareStatement(
            "SELECT COALESCE(SUM(amount),0) AS total FROM payment_master WHERE active=1"
        );
        ResultSet rs2 = ps2.executeQuery();
        if (rs2.next()) paid = rs2.getDouble("total");

        return wages - paid;
    }

    // Recent Attendance (latest 10)
    public static List<Map<String, Object>> getRecentAttendance(Connection conn) throws Exception {

        List<Map<String, Object>> list = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement(
            "SELECT a.attendance_date, w.worker_name, wt.work_type_name, a.day_type, a.wage " +
            "FROM attendance a " +
            "INNER JOIN worker_master w ON a.worker_id=w.worker_id " +
            "INNER JOIN work_type wt ON a.work_type_id=wt.work_type_id " +
            "ORDER BY a.attendance_date DESC, a.attendance_id DESC " +
            "LIMIT 10"
        );

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Map<String, Object> row = new HashMap<>();
            row.put("date", rs.getDate("attendance_date").toString());
            row.put("worker_name", rs.getString("worker_name"));
            row.put("work_type_name", rs.getString("work_type_name"));

            // convert FULL/HALF into full_day/half_day for badge
            String type = rs.getString("day_type");
            if ("FULL".equalsIgnoreCase(type)) {
                row.put("attendance_type", "full_day");
            } else {
                row.put("attendance_type", "half_day");
            }

            row.put("wage", rs.getDouble("wage"));

            list.add(row);
        }

        return list;
    } 
}
