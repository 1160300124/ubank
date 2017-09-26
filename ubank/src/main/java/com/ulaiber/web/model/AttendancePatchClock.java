package com.ulaiber.web.model;

/** 
 * 补卡
 *
 * @author  huangguoqing
 * @date 创建时间：2017年9月25日 下午12:06:14
 * @version 1.0 
 * @since 
 */
public class AttendancePatchClock {
	
	/**
	 * 补卡流水号
	 */
	private long pid;
	
	/**
	 * 用户id
	 */
	private long userId;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 部门
	 */
	private Departments dept;
	
	/**
	 * 公司
	 */
	private Company company;
	
	/**
	 * 补卡日期  yyyy-MM-dd
	 */
	private String patchClockDate;
	
	/**
	 * 补卡类型 0：全天补卡 1：上班补卡 2：下班补卡
	 */
	private int patchClockType;
	
	/**
	 * 补卡审批状态 0：已通过 1：未通过 2：审批中
	 */
	private int patchClockStatus;
	
	/**
	 * 上班补卡时间 yyyy-MM-dd HH:mm
	 */
	private String patchClockOnDateTime;
	
	/**
	 * 下班补卡时间 yyyy-MM-dd HH:mm
	 */
	private String patchClockOffDateTime;

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
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

	public Departments getDept() {
		return dept;
	}

	public void setDept(Departments dept) {
		this.dept = dept;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getPatchClockDate() {
		return patchClockDate;
	}

	public void setPatchClockDate(String patchClockDate) {
		this.patchClockDate = patchClockDate;
	}

	public int getPatchClockType() {
		return patchClockType;
	}

	public void setPatchClockType(int patchClockType) {
		this.patchClockType = patchClockType;
	}

	public int getPatchClockStatus() {
		return patchClockStatus;
	}

	public void setPatchClockStatus(int patchClockStatus) {
		this.patchClockStatus = patchClockStatus;
	}

	public String getPatchClockOnDateTime() {
		return patchClockOnDateTime;
	}

	public void setPatchClockOnDateTime(String patchClockOnDateTime) {
		this.patchClockOnDateTime = patchClockOnDateTime;
	}

	public String getPatchClockOffDateTime() {
		return patchClockOffDateTime;
	}

	public void setPatchClockOffDateTime(String patchClockOffDateTime) {
		this.patchClockOffDateTime = patchClockOffDateTime;
	}

}
