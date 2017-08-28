package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 个人申请记录信息
 * Created by daiqingwen on 2017/8/23.
 */
public class ApplyForVO implements Serializable{
    private int id;
    private String userid;
    private String leaveType;
    private String startDate;
    private String endDate;
    private float leaveTime;
    private String auditor;
    private String reason;
    private String disable;
    private String status;
    private Object auditorStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public float getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(float leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDisable() {
        return disable;
    }

    public void setDisable(String disable) {
        this.disable = disable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getAuditorStatus() {
        return auditorStatus;
    }

    public void setAuditorStatus(Object auditorStatus) {
        this.auditorStatus = auditorStatus;
    }
}
