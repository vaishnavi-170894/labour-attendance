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
public class PaymentDBUtils {
   // Worker wise summary (wages, paid, balance)
    public static List<Payment> paymentSummary(Connection conn, int siteId, String role) throws Exception {

        List<Payment> list = new ArrayList<>();

        String sql =
                "SELECT w.worker_id, w.worker_name, " +
                "COALESCE(SUM(a.wage),0) AS total_wages, " +
                "COALESCE((SELECT SUM(p.amount) FROM payment_master p " +
                "          WHERE p.worker_id=w.worker_id AND p.active=1),0) AS total_paid, " +
                "COALESCE((SELECT MAX(p.payment_date) FROM payment_master p " +
                "          WHERE p.worker_id=w.worker_id AND p.active=1),NULL) AS last_payment_date, " +
                "COALESCE((SELECT p.amount FROM payment_master p " +
                "          WHERE p.worker_id=w.worker_id AND p.active=1 " +
                "          ORDER BY p.payment_date DESC, p.payment_id DESC LIMIT 1),0) AS last_payment_amount " +
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

            if (rs.getDate("last_payment_date") != null) {
                p.setLastPaymentDate(rs.getDate("last_payment_date").toString());
            }

            p.setLastPaymentAmount(rs.getDouble("last_payment_amount"));

            list.add(p);
        }

        return list;
    }

    // Payment History worker wise
    public static List<Payment> paymentHistory(Connection conn, int workerId) throws Exception {

        List<Payment> list = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement(
            "SELECT p.payment_id, p.amount, p.payment_date, p.payment_mode, p.reference_number, p.notes, " +
            "u.full_name AS recorded_by_name " +
            "FROM payment_master p " +
            "INNER JOIN users u ON p.recorded_by=u.user_id " +
            "WHERE p.worker_id=? AND p.active=1 " +
            "ORDER BY p.payment_date DESC, p.payment_id DESC"
        );

        ps.setInt(1, workerId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Payment p = new Payment();
            p.setPaymentId(rs.getLong("payment_id"));
            p.setAmount(rs.getDouble("amount"));
            p.setPaymentDate(rs.getDate("payment_date").toString());
            p.setPaymentMode(rs.getString("payment_mode"));
            p.setReferenceNumber(rs.getString("reference_number"));
            p.setNotes(rs.getString("notes"));
            p.setRecordedByName(rs.getString("recorded_by_name"));

            list.add(p);
        }

        return list;
    }

    // Save Payment
    public static void savePayment(Connection conn, Payment p) throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO payment_master(worker_id, site_id, amount, payment_date, payment_mode, reference_number, notes, recorded_by) " +
            "VALUES(?,?,?,?,?,?,?,?)"
        );

        ps.setInt(1, p.getWorkerId());
        ps.setInt(2, p.getSiteId());
        ps.setDouble(3, p.getAmount());
        ps.setDate(4, java.sql.Date.valueOf(p.getPaymentDate()));
        ps.setString(5, p.getPaymentMode());
        ps.setString(6, p.getReferenceNumber());
        ps.setString(7, p.getNotes());
        ps.setInt(8, p.getRecordedBy());

        ps.executeUpdate();
    }

    // Delete Payment (Soft delete)
    public static void deletePayment(Connection conn, long paymentId) throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "UPDATE payment_master SET active=0 WHERE payment_id=?"
        );

        ps.setLong(1, paymentId);
        ps.executeUpdate();
    }
}
