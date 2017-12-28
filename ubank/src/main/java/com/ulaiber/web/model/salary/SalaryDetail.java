package com.ulaiber.web.model.salary;

/**
 * 工资详细
 * @author huangguoqing
 *
 */
public class SalaryDetail {
	
	/**
	 * 流水编号
	 */
	private String did;
	
	/**
	 * 工资流水号  对应salary的sid
	 */
	private String sid;
	
	/**
	 * excel序号
	 */
	private int eid;
	
	/**
	 * 收款人ID
	 */
	private long userId;
	
	/**
	 * 收款人姓名
	 */
	private String userName;
	
	/**
	 * 身份证号
	 */
	private String cardNo;
	
	/**
	 * 平台理财专属子账户
	 */
	private String subAcctNo;
	
	/**
	 * 税前工资
	 */
	private double preTaxSalaries;
	
	/**
	 * 奖金
	 */
	private double bonuses;
	
	/**
	 * 补贴
	 */
	private double subsidies;
	
	/**
	 * 考勤扣款总金额
	 */
	private double totalCutPayment;
	
	/**
	 * 迟到扣款
	 */
	private double laterCutPayment;
	
	/**
	 * 早退扣款
	 */
	private double leaveEarlyCutPayment;
	
	/**
	 * 忘打卡扣款
	 */
	private double forgetClockCutPayment;
	
	/**
	 * 旷工
	 */
	private double noClockCutPayment;
	
	/**
	 * 请假扣款
	 */
	private double askForLeaveCutPayment;
	
	/**
	 * 加班费
	 */
	private double overtimePayment;
	
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
	 * 个人所得税
	 */
	private double personalIncomeTax;
	
	/**
	 * 其他扣款
	 */
	private double elseCutPayment;
	
	/**
	 * 应发工资
	 */
	private double salaries;
	
	/**
	 * 发工资日期
	 */
	private String salaryMonth;
	
	/**
	 * 创建时间
	 */
	private String createDate;
	
	/**
	 * 更新时间
	 */
	private String updateTime;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 状态 0待发放  1成功  2失败
	 */
	private String status;

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public String getSubAcctNo() {
		return subAcctNo;
	}

	public void setSubAcctNo(String subAcctNo) {
		this.subAcctNo = subAcctNo;
	}

	public double getPreTaxSalaries() {
		return preTaxSalaries;
	}

	public void setPreTaxSalaries(double preTaxSalaries) {
		this.preTaxSalaries = preTaxSalaries;
	}

	public double getBonuses() {
		return bonuses;
	}

	public void setBonuses(double bonuses) {
		this.bonuses = bonuses;
	}

	public double getSubsidies() {
		return subsidies;
	}

	public void setSubsidies(double subsidies) {
		this.subsidies = subsidies;
	}

	public double getTotalCutPayment() {
		return totalCutPayment;
	}

	public void setTotalCutPayment(double totalCutPayment) {
		this.totalCutPayment = totalCutPayment;
	}

	public double getLaterCutPayment() {
		return laterCutPayment;
	}

	public void setLaterCutPayment(double laterCutPayment) {
		this.laterCutPayment = laterCutPayment;
	}

	public double getLeaveEarlyCutPayment() {
		return leaveEarlyCutPayment;
	}

	public void setLeaveEarlyCutPayment(double leaveEarlyCutPayment) {
		this.leaveEarlyCutPayment = leaveEarlyCutPayment;
	}

	public double getForgetClockCutPayment() {
		return forgetClockCutPayment;
	}

	public void setForgetClockCutPayment(double forgetClockCutPayment) {
		this.forgetClockCutPayment = forgetClockCutPayment;
	}

	public double getNoClockCutPayment() {
		return noClockCutPayment;
	}

	public void setNoClockCutPayment(double noClockCutPayment) {
		this.noClockCutPayment = noClockCutPayment;
	}

	public double getAskForLeaveCutPayment() {
		return askForLeaveCutPayment;
	}

	public void setAskForLeaveCutPayment(double askForLeaveCutPayment) {
		this.askForLeaveCutPayment = askForLeaveCutPayment;
	}

	public double getOvertimePayment() {
		return overtimePayment;
	}

	public void setOvertimePayment(double overtimePayment) {
		this.overtimePayment = overtimePayment;
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

	public double getPersonalIncomeTax() {
		return personalIncomeTax;
	}

	public void setPersonalIncomeTax(double personalIncomeTax) {
		this.personalIncomeTax = personalIncomeTax;
	}

	public double getElseCutPayment() {
		return elseCutPayment;
	}

	public void setElseCutPayment(double elseCutPayment) {
		this.elseCutPayment = elseCutPayment;
	}

	public double getSalaries() {
		return salaries;
	}

	public String getSalaryMonth() {
		return salaryMonth;
	}

	public void setSalaryMonth(String salaryMonth) {
		this.salaryMonth = salaryMonth;
	}

	public void setSalaries(double salaries) {
		this.salaries = salaries;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
