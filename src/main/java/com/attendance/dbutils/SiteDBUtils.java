/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.dbutils;

import com.attendance.dto.Site;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vaishnavi.dhole
 */
public class SiteDBUtils {
  public static List<Site> siteList(Connection conn) throws Exception {

        List<Site> list = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement(
            "SELECT site_id, site_name FROM site_master WHERE active=1 ORDER BY site_name"
        );

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Site s = new Site();
            s.setSiteId(rs.getInt("site_id"));
            s.setSiteName(rs.getString("site_name"));
            list.add(s);
        }

        return list;
    }
   public static List<Site> getSites(Connection conn) throws Exception {
        List<Site> list = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM site_master WHERE active=1 ORDER BY site_id DESC"
        );

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Site s = new Site();
            s.setSiteId(rs.getInt("site_id"));
            s.setSiteName(rs.getString("site_name"));
            s.setSiteLat(rs.getDouble("site_lat"));
            s.setSiteLng(rs.getDouble("site_lng"));
            s.setAllowedRadius(rs.getInt("allowed_radius_m"));
            s.setActive(rs.getInt("active"));
            list.add(s);
        }

        return list;
    }

    public static void saveSite(Connection conn, Site site) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO site_master(site_name, site_lat, site_lng, allowed_radius_m, active) VALUES(?,?,?,?,1)"
        );
        ps.setString(1, site.getSiteName());
        ps.setDouble(2, site.getSiteLat());
        ps.setDouble(3, site.getSiteLng());
        ps.setInt(4, site.getAllowedRadius());
        ps.executeUpdate();
    }

    public static void updateSite(Connection conn, Site site) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE site_master SET site_name=?, site_lat=?, site_lng=?, allowed_radius_m=? WHERE site_id=?"
        );
        ps.setString(1, site.getSiteName());
        ps.setDouble(2, site.getSiteLat());
        ps.setDouble(3, site.getSiteLng());
        ps.setInt(4, site.getAllowedRadius());
        ps.setInt(5, site.getSiteId());
        ps.executeUpdate();
    }

    public static void deleteSite(Connection conn, int siteId) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE site_master SET active=0 WHERE site_id=?"
        );
        ps.setInt(1, siteId);
        ps.executeUpdate();
    }
}