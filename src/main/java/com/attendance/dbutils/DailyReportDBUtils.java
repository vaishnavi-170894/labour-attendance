/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.dbutils;

import com.attendance.dto.DailyReport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vaishnavi.dhole
 */
public class DailyReportDBUtils {
  public static long saveReport(Connection conn, DailyReport r) throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO daily_reports(report_date, report_text, created_by) VALUES(?,?,?) RETURNING report_id"
        );

        ps.setDate(1, java.sql.Date.valueOf(r.getReportDate()));
        ps.setString(2, r.getReportText());
        ps.setInt(3, r.getCreatedBy());

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getLong("report_id");
    }

    public static void saveReportPhoto(Connection conn, long reportId, String fileName) throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO daily_report_photos(report_id, file_name) VALUES(?,?)"
        );

        ps.setLong(1, reportId);
        ps.setString(2, fileName);

        ps.executeUpdate();
    }

    public static void deleteReport(Connection conn, long reportId) throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "DELETE FROM daily_reports WHERE report_id=?"
        );

        ps.setLong(1, reportId);
        ps.executeUpdate();
    }

    public static void updateReport(Connection conn, long reportId, String reportText) throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "UPDATE daily_reports SET report_text=? WHERE report_id=?"
        );

        ps.setString(1, reportText);
        ps.setLong(2, reportId);

        ps.executeUpdate();
    }

    public static void deleteAllPhotos(Connection conn, long reportId) throws Exception {

        PreparedStatement ps = conn.prepareStatement(
            "DELETE FROM daily_report_photos WHERE report_id=?"
        );

        ps.setLong(1, reportId);
        ps.executeUpdate();
    }

    public static List<String> getReportPhotos(Connection conn, long reportId) throws Exception {

        List<String> list = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement(
            "SELECT file_name FROM daily_report_photos WHERE report_id=? ORDER BY photo_id"
        );

        ps.setLong(1, reportId);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(rs.getString("file_name"));
        }

        return list;
    }

    public static List<DailyReport> getReports(Connection conn, String startDate, String endDate, int siteId)
            throws Exception {

        List<DailyReport> list = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement(
            "SELECT dr.report_id, dr.report_date, dr.report_text, dr.created_at, " +
            "u.user_id, u.full_name " +
            "FROM daily_reports dr " +
            "INNER JOIN users u ON dr.created_by=u.user_id " +
            "WHERE u.site_id=? " +
            "AND dr.report_date BETWEEN ? AND ? " +
            "ORDER BY dr.report_date DESC, dr.report_id DESC"
        );

        ps.setInt(1, siteId);
        ps.setDate(2, java.sql.Date.valueOf(startDate));
        ps.setDate(3, java.sql.Date.valueOf(endDate));

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            DailyReport r = new DailyReport();
            r.setReportId(rs.getLong("report_id"));
            r.setReportDate(rs.getDate("report_date").toString());
            r.setReportText(rs.getString("report_text"));
            r.setCreatedBy(rs.getInt("user_id"));
            r.setCreatedByName(rs.getString("full_name"));
            r.setCreatedAt(rs.getTimestamp("created_at").toString());

            // load images
            r.setImages(getReportPhotos(conn, r.getReportId()));

            list.add(r);
        }

        return list;
    }  
}
