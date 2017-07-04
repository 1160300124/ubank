package com.ulaiber.model;

public class SalaryInfo {
	
	/**
	 * 流水编号
	 */
	private long sid;
	
	/**
	 * 员工姓名
	 */
	private String userName;
	
	/**
	 * 身份证号
	 */
	private String cardNo;
	
	/**
	 * 工资金额
	 */
	private String salaries;
	
	/**
	 * 工资发放时间
	 */
	private String salaryDate;
	
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getSalaries() {
		return salaries;
	}

	public void setSalaries(String salaries) {
		this.salaries = salaries;
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

}
