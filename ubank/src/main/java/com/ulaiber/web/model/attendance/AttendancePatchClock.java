package com.ulaiber.web.model.attendance;

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
	 * 补卡日期  yyyy-MM-dd
	 */
	private String patchClockDate;
	
	/**
	 * 补卡类型 0：全天补卡  1：上班补卡  2：下班补卡
	 *
	 */
	private int patchClockType;
	
	/**
	 * 补卡审批状态 0：审批中  1：已通过  2：未通过  3：已取消
	 */
	private String patchClockStatus;
	
	/**
	 * 上班补卡时间 yyyy-MM-dd HH:mm
	 */
	private String patchClockOnTime;
	
	/**
	 * 下班补卡时间 yyyy-MM-dd HH:mm
	 */
	private String patchClockOffTime;

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

	public String getPatchClockStatus() {
		return patchClockStatus;
	}

	public void setPatchClockStatus(String patchClockStatus) {
		this.patchClockStatus = patchClockStatus;
	}

	public String getPatchClockOnTime() {
		return patchClockOnTime;
	}

	public void setPatchClockOnTime(String patchClockOnTime) {
		this.patchClockOnTime = patchClockOnTime;
	}

	public String getPatchClockOffTime() {
		return patchClockOffTime;
	}

	public void setPatchClockOffTime(String patchClockOffTime) {
		this.patchClockOffTime = patchClockOffTime;
	}
	
}
