/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.dto;

import java.util.List;

/**
 *
 * @author vaishnavi.dhole
 */
public class Site {
  private int siteId;
    private String siteName;
      private double siteLat;
    private double siteLng;
    private int allowedRadius;
    private int active;
     private List<Site> list;

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public double getSiteLat() {
        return siteLat;
    }

    public void setSiteLat(double siteLat) {
        this.siteLat = siteLat;
    }

    public double getSiteLng() {
        return siteLng;
    }

    public void setSiteLng(double siteLng) {
        this.siteLng = siteLng;
    }

    public int getAllowedRadius() {
        return allowedRadius;
    }

    public void setAllowedRadius(int allowedRadius) {
        this.allowedRadius = allowedRadius;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public List<Site> getList() {
        return list;
    }

    public void setList(List<Site> list) {
        this.list = list;
    }
    
}
