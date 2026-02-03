/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author vaishnavi.dhole
 */
public class DBConnection {
     /*-------------Local Credentials-------------------*/
    private final static String HOSTNAME = "localhost";
    private final static String DBNAME = "labour_attendance";
    private final static String USERNAME = "postgres";
    private final static String PASS = "postgres";
//    private final static String SCHEMA="public";
    /*-------------/Local Credentials------------------*/
 /*-------------Live Credentials-------------------*/
 /*    
    private final static String HOSTNAME = "10.10.0.122";
    private final static String DBNAME = "forest_vanayukta_shivar";
    private final static String USERNAME = "mrsac";
    private final static String PASS = "foresT@18745";
    private final static String SCHEMA = "public";
    
     */

    /*-------------/Live Credentials-------------------*/
    public static Connection PGConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection("jdbc:postgresql://" + HOSTNAME + ":5432/" + DBNAME, USERNAME, PASS);
        return conn;
    }
}
