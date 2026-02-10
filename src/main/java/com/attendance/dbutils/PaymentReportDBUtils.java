/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.dbutils;

import com.attendance.dto.Payment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vaishnavi.dhole
 */
public class PaymentReportDBUtils {
   // ================= PAYMENT REPORT LIST ==================
    public static List<Payment> paymentReport(Connection conn,
            String startDate, String endDate, int siteId, String role) throws Exception {

        List<Payment> list = new ArrayList<>();

        String sql =
            "SELECT p.payment_id, p.payment_date, p.amount, p.payment_mode, p.reference_number, p.notes, " +
            "w.worker_name, s.site_name, u.full_name AS recorded_by_name " +
            "FROM payment_master p " +
            "INNER JOIN worker_master w ON p.worker_id=w.worker_id " +
            "INNER JOIN site_master s ON p.site_id=s.site_id " +
            "INNER JOIN users u ON p.recorded_by=u.user_id " +
            "WHERE p.active=1 " +
            "AND p.payment_date BETWEEN ? AND ? ";

        if (!"ADMIN".equals(role)) {
            sql += " AND p.site_id=? ";
        } else {
            if (siteId > 0) {
                sql += " AND p.site_id=? ";
            }
        }

        sql += " ORDER BY p.payment_date DESC, p.payment_id DESC";

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
            Payment p = new Payment();
            p.setPaymentId(rs.getLong("payment_id"));
            p.setPaymentDate(rs.getDate("payment_date").toString());
            p.setAmount(rs.getDouble("amount"));
            p.setPaymentMode(rs.getString("payment_mode"));
            p.setReferenceNumber(rs.getString("reference_number"));
            p.setNotes(rs.getString("notes"));

            p.setWorkerName(rs.getString("worker_name"));
            p.setSiteName(rs.getString("site_name"));
            p.setRecordedByName(rs.getString("recorded_by_name"));

            list.add(p);
        }

        return list;
    }

    // ================= SETTLEMENT REPORT ==================
    public static List<Payment> settlementReport(Connection conn, int siteId, String role) throws Exception {

        List<Payment> list = new ArrayList<>();

        String sql =
            "SELECT w.worker_id, w.worker_name, " +
            "COALESCE(SUM(a.wage),0) AS total_wages, " +
            "COALESCE((SELECT SUM(p.amount) FROM payment_master p WHERE p.worker_id=w.worker_id AND p.active=1),0) AS total_paid " +
            "FROM worker_master w " +
            "LEFT JOIN attendance a ON w.worker_id=a.worker_id ";

        if (!"ADMIN".equals(role)) {
            sql += " AND a.site_id=? ";
        }

        sql += " WHERE w.active=1 GROUP BY w.worker_id, w.worker_name ORDER BY w.worker_name";

        PreparedStatement ps = conn.prepareStatement(sql);

        if (!"ADMIN".equals(role)) {
            ps.setInt(1, siteId);
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Payment p = new Payment();
            p.setWorkerId(rs.getInt("worker_id"));
            p.setWorkerName(rs.getString("worker_name"));

            p.setTotalWages(rs.getDouble("total_wages"));
            p.setTotalPaid(rs.getDouble("total_paid"));
            p.setBalance(p.getTotalWages() - p.getTotalPaid());

            list.add(p);
        }

        return list;
    }
}