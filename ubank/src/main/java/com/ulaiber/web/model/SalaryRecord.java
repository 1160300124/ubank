package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 工资申请审批表实体类
 * Created by daiqingwen on 2017/9/21.
 */
public class SalaryRecord implements Serializable {
    private int id;
    private int recordNo;
	private int totalNumber;    //工资总笔数
    private double totalAmount; //工资总金额
    private String salaryId;    //工资流水id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecordNo() {
        return recordNo;
    }

    public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	public void setRecordNo(int recordNo) {
		this.recordNo = recordNo;
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
