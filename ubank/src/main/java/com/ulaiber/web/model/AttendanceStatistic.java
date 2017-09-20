package com.ulaiber.web.model;

/** 
 * 考勤统计
 *
 * @author  huangguoqing
 * @date 创建时间：2017年9月7日 上午11:22:16
 * @version 1.0 
 * @since 
 */
public class AttendanceStatistic {

	/**
	 * 流水号
	 */
	private long sid;
	
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
	 * 应工作天数
	 */
	private int workdaysCount;
	
	/**
	 *  正常上班打卡次数
	 */
	private int normalClockOnCount;
	
	/**
	 * 迟到次数
	 */
	private int laterCount;
	
	/**
	 * 未打卡次数(上班)
	 */
	private int noClockOnCount;
	
	/**
	 * 正常下班打卡次数
	 */
	private int normalClockOffCount;
	
	/**
	 * 早退次数
	 */
	private int leaveEarlyCount;
	
	/**
	 * 未打卡次数(下班)
	 */
	private int noClockOffCount;
	
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

	public int getWorkdaysCount() {
		return workdaysCount;
	}

	public void setWorkdaysCount(int workdaysCount) {
		this.workdaysCount = workdaysCount;
	}

	public int getNormalClockOnCount() {
		return normalClockOnCount;
	}

	public void setNormalClockOnCount(int normalClockOnCount) {
		this.normalClockOnCount = normalClockOnCount;
	}

	public int getLaterCount() {
		return laterCount;
	}

	public void setLaterCount(int laterCount) {
		this.laterCount = laterCount;
	}

	public int getNoClockOnCount() {
		return noClockOnCount;
	}

	public void setNoClockOnCount(int noClockOnCount) {
		this.noClockOnCount = noClockOnCount;
	}

	public int getNormalClockOffCount() {
		return normalClockOffCount;
	}

	public void setNormalClockOffCount(int normalClockOffCount) {
		this.normalClockOffCount = normalClockOffCount;
	}

	public int getLeaveEarlyCount() {
		return leaveEarlyCount;
	}

	public void setLeaveEarlyCount(int leaveEarlyCount) {
		this.leaveEarlyCount = leaveEarlyCount;
	}

	public int getNoClockOffCount() {
		return noClockOffCount;
	}

	public void setNoClockOffCount(int noClockOffCount) {
		this.noClockOffCount = noClockOffCount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
