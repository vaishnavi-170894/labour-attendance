/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.dto;

/**
 *
 * @author vaishnavi.dhole
 */
public class Base {
 
    private int status;
    private int is_deleted;
    private String created;
    private String createdby;
    private int createdbyid;

    private String modified;
    private String modifiedby;
    private int modifiedbyid;

    private String deleted;
    private String deletedby;
    private int deletedbyid;

    /*----------------------DataTable parameters-------------------*/
    private int draw;
    private int start;
    private int length;
    private String searchValue;

    private String orderColumnIndex;
    private String orderDir;
    private String orderColumn;

    /*----------------------/DataTable parameters------------------*/
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public int getCreatedbyid() {
        return createdbyid;
    }

    public void setCreatedbyid(int createdbyid) {
        this.createdbyid = createdbyid;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    public int getModifiedbyid() {
        return modifiedbyid;
    }

    public void setModifiedbyid(int modifiedbyid) {
        this.modifiedbyid = modifiedbyid;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDeletedby() {
        return deletedby;
    }

    public void setDeletedby(String deletedby) {
        this.deletedby = deletedby;
    }

    public int getDeletedbyid() {
        return deletedbyid;
    }

    public void setDeletedbyid(int deletedbyid) {
        this.deletedbyid = deletedbyid;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getOrderColumnIndex() {
        return orderColumnIndex;
    }

    public void setOrderColumnIndex(String orderColumnIndex) {
        this.orderColumnIndex = orderColumnIndex;
    }

    public String getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(String orderDir) {
        this.orderDir = orderDir;
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

}
