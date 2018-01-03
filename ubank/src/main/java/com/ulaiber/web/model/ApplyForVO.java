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
    private String startType;   //开始时间类型。0 上半天 1 下半天
    private String endType;     //结束时间类型。0 上半天 1 下半天
    private int restDay;        //节假日休息天数
    private float leaveTime;    // 请假时长
    private String auditor;     // 审批人
    private String reason;      // 原因
    private String disable;     // 是否作废； 0 否 ，1 是
    private String type;        // 记录类型； 0 请假记录， 1 加班记录,2 报销记录
    private String createDate;  // 创建时间
    private String status;      // 审批状态
    private Object auditorStatus;//
    private String holiday;      // 是否节假日；0 否，1 是
    private String mode;         // 加班核算方式；0 调休，1 发工资
    private String username;     //用户名
    private String image;        //头像
    private String currentAuditor; //当前记录审批人
    private String morning;     //上午打卡时间
    private String afternoon;   //下午打卡时间
    private String remedyType;  //补卡类型。0 上午补卡 ， 1 下午补卡 ， 2 全天补卡
    private String remedyDate;  //补卡时间

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCurrentAuditor() {
        return currentAuditor;
    }

    public void setCurrentAuditor(String currentAuditor) {
        this.currentAuditor = currentAuditor;
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(String afternoon) {
        this.afternoon = afternoon;
    }

    public String getRemedyType() {
        return remedyType;
    }

    public void setRemedyType(String remedyType) {
        this.remedyType = remedyType;
    }

    public String getRemedyDate() {
        return remedyDate;
    }

    public void setRemedyDate(String remedyDate) {
        this.remedyDate = remedyDate;
    }
}
