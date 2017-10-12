package com.ulaiber.web.model;

/**
 * 工资详细
 * @author huangguoqing
 *
 */
public class SalaryDetail {
	
	/**
	 * 流水编号
	 */
	private long did;
	
	/**
	 * 工资流水号  对应salary的sid
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
	 * 税前工资
	 */
	private double pre_tax_salaries;
	
	/**
	 * 奖金
	 */
	private double bonuses;
	
	/**
	 * 补贴
	 */
	private double subsidies;
	
	/**
	 * 考勤扣款
	 */
	private double attendance_cut_payment;
	
	/**
	 * 请假扣款
	 */
	private double askForLeave_cut_payment;
	
	/**
	 * 加班费
	 */
	private double overtime_payment;
	
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
	private double else_cut_payment;
	
	/**
	 * 应发工资
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

	public double getPre_tax_salaries() {
		return pre_tax_salaries;
	}

	public void setPre_tax_salaries(double pre_tax_salaries) {
		this.pre_tax_salaries = pre_tax_salaries;
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

	public double getAttendance_cut_payment() {
		return attendance_cut_payment;
	}

	public void setAttendance_cut_payment(double attendance_cut_payment) {
		this.attendance_cut_payment = attendance_cut_payment;
	}

	public double getAskForLeave_cut_payment() {
		return askForLeave_cut_payment;
	}

	public void setAskForLeave_cut_payment(double askForLeave_cut_payment) {
		this.askForLeave_cut_payment = askForLeave_cut_payment;
	}

	public double getOvertime_payment() {
		return overtime_payment;
	}

	public void setOvertime_payment(double overtime_payment) {
		this.overtime_payment = overtime_payment;
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

	public double getElse_cut_payment() {
		return else_cut_payment;
	}

	public void setElse_cut_payment(double else_cut_payment) {
		this.else_cut_payment = else_cut_payment;
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
