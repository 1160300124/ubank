package com.ulaiber.web.model;

/** 
 * 考勤记录实体类
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月10日 下午3:31:47
 * @version 1.0 
 * @since 
 */
public class Attendance {
	/**
	 * 流水号
	 */
	private long rid;
	
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
	 * 打卡日期yyyy-mm-dd
	 */
	private String clockDate;
	
	/**
	 * 打卡时间 HH:mm
	 */
	private String clockTime;
	
	/**
	 * 打卡类型 0：签到 1：签退
	 */
	private String clockType;
	
	/**
	 * 打卡状态 0：正常 1：迟到 2：早退
	 */
	private String clockStatus;
	
	/**
	 * 打卡位置
	 */
	private String clockLocation;
	
	/**
	 * 打卡设备号
	 */
	private String clockDevice;

	public long getRid() {
		return rid;
	}

	public void setRid(long rid) {
		this.rid = rid;
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

	public String getClockDate() {
		return clockDate;
	}

	public void setClockDate(String clockDate) {
		this.clockDate = clockDate;
	}

	public String getClockTime() {
		return clockTime;
	}

	public void setClockTime(String clockTime) {
		this.clockTime = clockTime;
	}

	public String getClockType() {
		return clockType;
	}

	public void setClockType(String clockType) {
		this.clockType = clockType;
	}

	public String getClockStatus() {
		return clockStatus;
	}

	public void setClockStatus(String clockStatus) {
		this.clockStatus = clockStatus;
	}

	public String getClockLocation() {
		return clockLocation;
	}

	public void setClockLocation(String clockLocation) {
		this.clockLocation = clockLocation;
	}

	public String getClockDevice() {
		return clockDevice;
	}

	public void setClockDevice(String clockDevice) {
		this.clockDevice = clockDevice;
	}
	
}
