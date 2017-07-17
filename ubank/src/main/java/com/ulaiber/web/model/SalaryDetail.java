package com.ulaiber.web.model;

public class SalaryDetail {
	
	/**
	 * 流水编号
	 */
	private long did;
	
	/**
	 * 工资流水号  对应salaryInfo的sid
	 */
	private long sid;
	
	/**
	 * 对应excel导入的编号,没有实际作用
	 */
	private String eid;
	
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
	private double salaries;
	
	/**
	 * 工资发放时间
	 */
	private String salaryDate;
	
	/**
	 * 备注
	 */
	private String remark;
	
	public long getDid() {
		return did;
	}

	public void setDid(long did) {
		this.did = did;
	}

	public long getSid() {
		return sid;
	}

	public void setSid(long sid) {
		this.sid = sid;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
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

	public double getSalaries() {
		return salaries;
	}

	public void setSalaries(double salaries) {
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
