/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.dbutils;

import com.attendance.dto.WorkType;
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
public class WorkTypeDBUtils {
    /* ================= LIST ACTIVE WORK TYPES ================= */
    public static List<WorkType> workTypes(Connection conn) throws SQLException {

        List<WorkType> list = new ArrayList<>();

        String sql =
            "SELECT work_type_id, work_type_name, full_day_wage, half_day_wage, active " +
            "FROM work_type WHERE active=1 ORDER BY work_type_name";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                WorkType wt = new WorkType();
                wt.setWorkTypeId(rs.getInt("work_type_id"));
                wt.setWorkTypeName(rs.getString("work_type_name"));
                wt.setFullDayWage(rs.getDouble("full_day_wage"));
                wt.setHalfDayWage(rs.getDouble("half_day_wage"));
                wt.setActive(rs.getInt("active"));
                list.add(wt);
            }
        }
        return list;
    }

    /* ================= GET SINGLE WORK TYPE ================= */
    public static WorkType workType(Connection conn, int workTypeId) throws SQLException {

        WorkType wt = null;

        String sql =
            "SELECT work_type_id, work_type_name, full_day_wage, half_day_wage, active " +
            "FROM work_type WHERE work_type_id=? AND active=1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    wt = new WorkType();
                    wt.setWorkTypeId(rs.getInt("work_type_id"));
                    wt.setWorkTypeName(rs.getString("work_type_name"));
                    wt.setFullDayWage(rs.getDouble("full_day_wage"));
                    wt.setHalfDayWage(rs.getDouble("half_day_wage"));
                    wt.setActive(rs.getInt("active"));
                }
            }
        }
        return wt;
    }

    /* ================= ADD WORK TYPE (ADMIN) ================= */
    public static String addWorkType(Connection conn, WorkType wt) throws SQLException {

        String sql =
            "INSERT INTO work_type " +
            "(work_type_name, full_day_wage, half_day_wage, active) " +
            "VALUES (?, ?, ?, 1)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, wt.getWorkTypeName());
            ps.setDouble(2, wt.getFullDayWage());
            ps.setDouble(3, wt.getHalfDayWage());
            ps.executeUpdate();
        }
        return "success";
    }

    /* ================= SOFT DELETE WORK TYPE ================= */
    public static String disableWorkType(Connection conn, int workTypeId) throws SQLException {

        String sql =
            "UPDATE work_type SET active=0 WHERE work_type_id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workTypeId);
            ps.executeUpdate();
        }
        return "success";
    } 
}
