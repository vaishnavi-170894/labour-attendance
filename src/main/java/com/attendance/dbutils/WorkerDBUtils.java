/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.dbutils;

import com.attendance.dto.Worker;
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
public class WorkerDBUtils {
     /* ================= ADD WORKER ================= */
    public static String addWorker(Connection conn, Worker worker) throws SQLException {

        String sql = "INSERT INTO worker_master (worker_name, mobile_no, active) " +
                     "VALUES (?, ?, 1)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, worker.getWorkerName());
            ps.setString(2, worker.getMobileNo());
            ps.executeUpdate();
        }

        return "success";
    }

    /* ================= LIST ACTIVE WORKERS ================= */
    public static List<Worker> workers(Connection conn, Worker worker) throws SQLException {

        List<Worker> list = new ArrayList<>();

        String sql = "SELECT worker_id, worker_name, mobile_no, active " +
                     "FROM worker_master WHERE active=1 ORDER BY worker_name";

        if (worker.getWorkerId() != 0) {
            sql = "SELECT worker_id, worker_name, mobile_no, active " +
                  "FROM worker_master WHERE worker_id=? AND active=1";
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            if (worker.getWorkerId() != 0) {
                ps.setInt(1, worker.getWorkerId());
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Worker w = new Worker();
                w.setWorkerId(rs.getInt("worker_id"));
                w.setWorkerName(rs.getString("worker_name"));
                w.setMobileNo(rs.getString("mobile_no"));
                w.setActive(rs.getInt("active"));
                list.add(w);
            }
        }

        return list;
    }

    /* ================= SOFT DELETE WORKER ================= */
    public static String disableWorker(Connection conn, int workerId) throws SQLException {

        String sql = "UPDATE worker_master SET active=0 WHERE worker_id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workerId);
            ps.executeUpdate();
        }

        return "success";
    }
}
