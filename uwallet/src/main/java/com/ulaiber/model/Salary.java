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
	 * 企业名称
	 */
	private String company;
	
	/**
	 * 状态  调银企直联接口会返回7种状态, 状态5代表成功
	 * 0-未处理数据文件
	 * 1-正在处理数据文件
	 * 2-数据文件处理成功
	 * 3-数据文件有误，不能进行
	 * 4-正在处理业务数据
	 * 5-业务数据处理成功
	 * 6-业务数据处理失败
	 * 7-撤销
	 */
	private String status;
	
	/**
	 * 业务委托编号
	 */
	private String entrustSeqNo;
	
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
	
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSalary_createTime() {
		return salary_createTime;
	}

	public void setSalary_createTime(String salary_createTime) {
		this.salary_createTime = salary_createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEntrustSeqNo() {
		return entrustSeqNo;
	}

	public void setEntrustSeqNo(String entrustSeqNo) {
		this.entrustSeqNo = entrustSeqNo;
	}

}
