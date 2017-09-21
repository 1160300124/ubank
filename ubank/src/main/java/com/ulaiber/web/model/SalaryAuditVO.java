package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 工资审批请求参数
 * Created by daiqingwen on 2017/9/21.
 */
public class SalaryAuditVO implements Serializable{
    private String userId;  //用户ID
    private String auditor; //审批人
    private String reason;  //备注
    private String salary;  //工资

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

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

}
