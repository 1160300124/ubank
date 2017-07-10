package com.ulaiber.model;

public class Salary {
	
	/**
	 * 流水编号
	 */
	private long sid;
	
	/**
	 * 发工资人姓名
	 */
	private String userName;
	
	/**
	 * 工资总笔数
	 */
	private int totalNumber;
	
	/**
	 * 工资总金额
	 */
	private double totalAmount;
	
	/**
	 * 工资发放时间
	 */
	private String salaryDate;
	
	/**
	 * 生成时间
	 */
	private String salary_createTime;
	
	/**
	 * 备注
	 */
	private String remark;

	public long getSid() {
		return sid;
	}

	public void setSid(long sid) {
		this.sid = sid;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getSalaryDate() {
		return salaryDate;
	}

	public void setSalaryDate(String salaryDate) {
		this.salaryDate = salaryDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSalary_createTime() {
		return salary_createTime;
	}

	public void setSalary_createTime(String salary_createTime) {
		this.salary_createTime = salary_createTime;
	}
	
}
