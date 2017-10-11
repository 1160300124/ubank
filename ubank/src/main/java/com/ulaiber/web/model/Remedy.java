package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 补卡信息
 * Created by daiqingwen on 2017/9/27.
 */
public class Remedy implements Serializable {
    private int recordNo;       //申请记录ID
    private String morning;     //上午打卡时间
    private String afternoon;   //下午打卡时间
    private String type;        //补卡类型。0 上午补卡 ， 1 下午补卡 ， 2 全天补卡
    private String userId;      //用户ID
    private String auditor;     //审批人
    private String reason;      //备注
    private String remedyDate;  //补卡时间

    public int getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(int recordNo) {
        this.recordNo = recordNo;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getRemedyDate() {
        return remedyDate;
    }

    public void setRemedyDate(String remedyDate) {
        this.remedyDate = remedyDate;
    }
}
