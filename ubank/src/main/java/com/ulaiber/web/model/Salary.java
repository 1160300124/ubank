package com.ulaiber.web.model;

import java.util.List;

public class Salary {
	
	/**
	 * 流水编号
	 */
	private String sid;
	
	/**
	 * 发工资人Id
	 */
	private long operateUserId;
	
	/**
	 * 发工资人姓名
	 */
	private String operateUserName;
	
	/**
	 * 公司id
	 */
	private String companyId;
	
	/**
	 * 公司名称
	 */
	private String companyName;
	
	/**
	 * 工资总笔数
	 */
	private int totalNumber;
	
	/**
	 * 工资总金额
	 */
	private double totalAmount;
	
	/**
	 * 工资统计月份
	 */
	private String salaryMonth;
	
	/**
	 * 工资发放时间
	 */
	private String salaryDate;
	
	/**
	 * 工资单生成时间
	 */
	private String salaryCreateTime;
	
	/**
	 * 审批者id
	 */
	private String approveIds;
	
	/**
	 * 审批者姓名
	 */
	private String approveNames;
	
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
	
	/**
	 * 工资详细
	 */
	List<SalaryDetail> details;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public long getOperateUserId() {
		return operateUserId;
	}

	public void setOperateUserId(long operateUserId) {
		this.operateUserId = operateUserId;
	}

	public String getOperateUserName() {
		return operateUserName;
	}

	public void setOperateUserName(String operateUserName) {
		this.operateUserName = operateUserName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getSalaryMonth() {
		return salaryMonth;
	}

	public void setSalaryMonth(String salaryMonth) {
		this.salaryMonth = salaryMonth;
	}

	public String getSalaryDate() {
		return salaryDate;
	}

	public void setSalaryDate(String salaryDate) {
		this.salaryDate = salaryDate;
	}

	public String getSalaryCreateTime() {
		return salaryCreateTime;
	}

	public void setSalaryCreateTime(String salaryCreateTime) {
		this.salaryCreateTime = salaryCreateTime;
	}

	public String getApproveIds() {
		return approveIds;
	}

	public void setApproveIds(String approveIds) {
		this.approveIds = approveIds;
	}

	public String getApproveNames() {
		return approveNames;
	}

	public void setApproveNames(String approveNames) {
		this.approveNames = approveNames;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<SalaryDetail> getDetails() {
		return details;
	}

	public void setDetails(List<SalaryDetail> details) {
		this.details = details;
	}

}
