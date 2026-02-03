/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author vaishnavi.dhole
 */
public class Worker extends Base implements Serializable {

    private int workerId;
    private String workerName;
    private String mobileNo;
    private int active;

    /* For list pages */
    private List<Worker> list;

    /* ================= GETTERS & SETTERS ================= */

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public List<Worker> getList() {
        return list;
    }

    public void setList(List<Worker> list) {
        this.list = list;
    }
    
    
}
