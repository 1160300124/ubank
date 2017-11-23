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
	private int totalNumber;    //工资总笔数
    private double totalAmount; //工资总金额
    private String salaryId;    //工资流水id
    
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

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getSalaryId() {
		return salaryId;
	}

	public void setSalaryId(String salaryId) {
		this.salaryId = salaryId;
	}

}
