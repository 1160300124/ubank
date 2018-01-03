package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 个人申请信息
 * Created by daiqingwen on 2017/8/22.
 */
public class LeaveRecord implements Serializable{
    private int id;
    private String userid;
    private String leaveType;
    private String startDate;
    private String endDate;
    private String startType;
    private String endType;
    private int restDay;
    private float leaveTime;
    private float realLeaveTime; //实际请假时长,如请假的时间段内有销假,则为请假时长(leaveTime)-销假时长
    private String auditor;
    private String reason;
    private String disable;
    private String type;
    private String createDate;
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

    public String getStartType() {
        return startType;
    }

    public void setStartType(String startType) {
        this.startType = startType;
    }

    public String getEndType() {
        return endType;
    }

    public void setEndType(String endType) {
        this.endType = endType;
    }

    public int getRestDay() {
        return restDay;
    }

    public void setRestDay(int restDay) {
        this.restDay = restDay;
    }

    public float getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(float leaveTime) {
        this.leaveTime = leaveTime;
    }

    public float getRealLeaveTime() {
		return realLeaveTime;
	}

	public void setRealLeaveTime(float realLeaveTime) {
		this.realLeaveTime = realLeaveTime;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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
