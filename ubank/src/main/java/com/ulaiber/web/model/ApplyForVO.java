package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 个人申请记录信息
 * Created by daiqingwen on 2017/8/23.
 */
public class ApplyForVO implements Serializable{
    private int id;             // 申请记录ID
    private String userid;      // 用户ID
    private String leaveType;   // 请假类型
    private String startDate;   // 开始时间
    private String endDate;     // 结束时间
    private float leaveTime;    // 请假时长
    private String auditor;     // 审批人
    private String reason;      // 原因
    private String disable;     // 是否作废； 0 否 ，1 是
    private String type;        // 记录类型； 0 请假记录， 1 加班记录
    private String createDate;  // 创建时间
    private String status;      // 审批状态
    private Object auditorStatus;//
    private String holiday;      // 是否节假日；0 否，1 是
    private String mode;         // 加班核算方式；0 调休，1 发工资

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

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
