package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 确认审批的数据
 * Created by daiqingwen  on 2017/9/15.
 */
public class AuditData implements Serializable {
    private String userId;          //用户ID
    private String status;          //当前申请记录的审批状态
    private String reason;          //审批的理由
    private String auditorStatus;   //当前审批人审批的状态
    private String currentAuditor;  //当前申请记录的审批人
    private String recordNo;        //申请记录
    private String currentUserId;   //申请记录的当前审批人ID
    private String type;            //记录类型； 0 请假记录， 1 加班记录 , 2 报销记录,3 工资发放记录,4 补卡记录

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAuditorStatus() {
        return auditorStatus;
    }

    public void setAuditorStatus(String auditorStatus) {
        this.auditorStatus = auditorStatus;
    }

    public String getCurrentAuditor() {
        return currentAuditor;
    }

    public void setCurrentAuditor(String currentAuditor) {
        this.currentAuditor = currentAuditor;
    }

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
