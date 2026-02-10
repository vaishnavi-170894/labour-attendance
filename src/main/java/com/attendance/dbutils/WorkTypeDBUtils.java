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
    public static List<WorkType> workTypes(Connection conn) throws Exception {

        List<WorkType> list = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement(
            "SELECT work_type_id, work_type_name, full_day_wage, half_day_wage " +
            "FROM work_type WHERE active=1 ORDER BY work_type_name"
        );

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            WorkType wt = new WorkType();
            wt.setWorkTypeId(rs.getInt("work_type_id"));
            wt.setWorkTypeName(rs.getString("work_type_name"));
            wt.setFullDayWage(rs.getDouble("full_day_wage"));
            wt.setHalfDayWage(rs.getDouble("half_day_wage"));

            list.add(wt);
        }

        return list;
    }

    public static void addWorkType(Connection conn, WorkType wt) throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO work_type(work_type_name, full_day_wage, half_day_wage, active) " +
            "VALUES(?,?,?,1)"
        );

        ps.setString(1, wt.getWorkTypeName());
        ps.setDouble(2, wt.getFullDayWage());
        ps.setDouble(3, wt.getHalfDayWage());

        ps.executeUpdate();
    }

    public static void updateWorkType(Connection conn, WorkType wt) throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "UPDATE work_type SET work_type_name=?, full_day_wage=?, half_day_wage=? " +
            "WHERE work_type_id=?"
        );

        ps.setString(1, wt.getWorkTypeName());
        ps.setDouble(2, wt.getFullDayWage());
        ps.setDouble(3, wt.getHalfDayWage());
        ps.setInt(4, wt.getWorkTypeId());

        ps.executeUpdate();
    }

    public static void deleteWorkType(Connection conn, int workTypeId) throws Exception {

        // soft delete recommended
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE work_type SET active=0 WHERE work_type_id=?"
        );

        ps.setInt(1, workTypeId);
        ps.executeUpdate();
    }

    public static WorkType getById(Connection conn, int id) throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "SELECT work_type_id, work_type_name, full_day_wage, half_day_wage " +
            "FROM work_type WHERE work_type_id=?"
        );

        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            WorkType wt = new WorkType();
            wt.setWorkTypeId(rs.getInt("work_type_id"));
            wt.setWorkTypeName(rs.getString("work_type_name"));
            wt.setFullDayWage(rs.getDouble("full_day_wage"));
            wt.setHalfDayWage(rs.getDouble("half_day_wage"));
            return wt;
        }

        return null;
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
