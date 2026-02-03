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
public class WorkType extends Base{
     private int workTypeId;
    private String workTypeName;
    private double fullDayWage;
    private double halfDayWage;
    private int active;

    /* for list pages / dropdowns */
    private List<WorkType> list;

    /* ================= GETTERS & SETTERS ================= */ 

    public int getWorkTypeId() {
        return workTypeId;
    }

    public void setWorkTypeId(int workTypeId) {
        this.workTypeId = workTypeId;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }

    public double getFullDayWage() {
        return fullDayWage;
    }

    public void setFullDayWage(double fullDayWage) {
        this.fullDayWage = fullDayWage;
    }

    public double getHalfDayWage() {
        return halfDayWage;
    }

    public void setHalfDayWage(double halfDayWage) {
        this.halfDayWage = halfDayWage;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public List<WorkType> getList() {
        return list;
    }

    public void setList(List<WorkType> list) {
        this.list = list;
    }
    
}
