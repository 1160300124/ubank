package com.ulaiber.web.model.salary;

/** 
 * 工资规则
 *
 * @author  huangguoqing
 * @date 创建时间：2017年10月19日 上午10:20:13
 * @version 1.0 
 * @since 
 */
public class SalaryRule {
	
	/**
	 * 工资规则号
	 */
	private long rid;
	
	/**
	 * 规则名称
	 */
	private String salaryRuleName;
	
	/**
	 * 操作人
	 */
	private String operateName;
	
	/**
	 * 适配公司id
	 */
	private String companyId;
	
	/**
	 * 适配公司名称
	 */
	private String companyName;
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * 修改时间
	 */
	private String updateTime;
	
	/**
	 * 迟到扣款
	 */
	private String laterCutPayment;
	
	/**
	 * 早退扣款
	 */
	private String leaveEarlyCutPayment;
	
	/**
	 * 请假扣款
	 */
	private String leaveCutPayment;
	
	/**
	 * 0：加班换调休  1：加班换加班费
	 */
	private int overtimeFlag;
	
	/**
	 * 社保
	 */
	private double socialInsurance;
	
	/**
	 * 公积金
	 */
	private double publicAccumulationFunds;
	
	/**
	 * 个税起征点
	 */
	private double taxThreshold;
	
	/**
	 * 允许忘打卡次数
	 */
	private int allowForgetClockCount;
	
	/**
	 * 忘打卡扣款
	 */
	private double forgetClockCutPayment;
	
	/**
	 * 忘打卡扣款单位 0：元  1：天
	 */
	private int forgetClockCutUnit;
	
	/**
	 * 旷工/天扣款
	 */
	private double noClockCutPayment;
	
	/**
	 * 旷工/天扣款单位 0：元  1：天
	 */
	private int noClockCutUnit;
	
	/**
	 * 旷工/半天扣款
	 */
	private double half_noClockCutPayment;
	
	/**
	 * 旷工/半天扣款单位 0：元  1：天
	 */
	private int half_noClockCutUnit;

	public long getRid() {
		return rid;
	}

	public void setRid(long rid) {
		this.rid = rid;
	}

	public String getSalaryRuleName() {
		return salaryRuleName;
	}

	public void setSalaryRuleName(String salaryRuleName) {
		this.salaryRuleName = salaryRuleName;
	}

	public String getOperateName() {
		return operateName;
	}

	public void setOperateName(String operateName) {
		this.operateName = operateName;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getLaterCutPayment() {
		return laterCutPayment;
	}

	public void setLaterCutPayment(String laterCutPayment) {
		this.laterCutPayment = laterCutPayment;
	}

	public String getLeaveEarlyCutPayment() {
		return leaveEarlyCutPayment;
	}

	public void setLeaveEarlyCutPayment(String leaveEarlyCutPayment) {
		this.leaveEarlyCutPayment = leaveEarlyCutPayment;
	}

	public String getLeaveCutPayment() {
		return leaveCutPayment;
	}

	public void setLeaveCutPayment(String leaveCutPayment) {
		this.leaveCutPayment = leaveCutPayment;
	}

	public int getOvertimeFlag() {
		return overtimeFlag;
	}

	public void setOvertimeFlag(int overtimeFlag) {
		this.overtimeFlag = overtimeFlag;
	}

	public double getSocialInsurance() {
		return socialInsurance;
	}

	public void setSocialInsurance(double socialInsurance) {
		this.socialInsurance = socialInsurance;
	}

	public double getPublicAccumulationFunds() {
		return publicAccumulationFunds;
	}

	public void setPublicAccumulationFunds(double publicAccumulationFunds) {
		this.publicAccumulationFunds = publicAccumulationFunds;
	}

	public double getTaxThreshold() {
		return taxThreshold;
	}

	public void setTaxThreshold(double taxThreshold) {
		this.taxThreshold = taxThreshold;
	}

	public int getAllowForgetClockCount() {
		return allowForgetClockCount;
	}

	public void setAllowForgetClockCount(int allowForgetClockCount) {
		this.allowForgetClockCount = allowForgetClockCount;
	}

	public double getForgetClockCutPayment() {
		return forgetClockCutPayment;
	}

	public void setForgetClockCutPayment(double forgetClockCutPayment) {
		this.forgetClockCutPayment = forgetClockCutPayment;
	}

	public int getForgetClockCutUnit() {
		return forgetClockCutUnit;
	}

	public void setForgetClockCutUnit(int forgetClockCutUnit) {
		this.forgetClockCutUnit = forgetClockCutUnit;
	}

	public double getNoClockCutPayment() {
		return noClockCutPayment;
	}

	public void setNoClockCutPayment(double noClockCutPayment) {
		this.noClockCutPayment = noClockCutPayment;
	}

	public int getNoClockCutUnit() {
		return noClockCutUnit;
	}

	public void setNoClockCutUnit(int noClockCutUnit) {
		this.noClockCutUnit = noClockCutUnit;
	}

	public double getHalf_noClockCutPayment() {
		return half_noClockCutPayment;
	}

	public void setHalf_noClockCutPayment(double half_noClockCutPayment) {
		this.half_noClockCutPayment = half_noClockCutPayment;
	}

	public int getHalf_noClockCutUnit() {
		return half_noClockCutUnit;
	}

	public void setHalf_noClockCutUnit(int half_noClockCutUnit) {
		this.half_noClockCutUnit = half_noClockCutUnit;
	}
	
}
