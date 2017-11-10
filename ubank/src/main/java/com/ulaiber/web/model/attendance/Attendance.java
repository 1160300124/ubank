package com.ulaiber.web.model.attendance;

import com.ulaiber.web.model.Company;
import com.ulaiber.web.model.Departments;

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
	 * 上班打卡时间
	 */
	private String clockOnDateTime;
	
	
	/**
	 * 打卡状态 0：正常 1：迟到
	 */
	private String clockOnStatus;
	
	/**
	 * 上班打卡位置
	 */
	private String clockOnLocation;
	
	/**
	 * 上班打卡设备号
	 */
	private String clockOnDevice;
	
	/**
	 * 上班外勤卡备注
	 */
	private String clockOnRemark;
	
	/**
	 * 下班打卡时间
	 */
	private String clockOffDateTime;
	
	/**
	 * 打卡状态 0：正常 1：早退
	 */
	private String clockOffStatus;
	
	/**
	 * 下班打卡位置
	 */
	private String clockOffLocation;
	
	/**
	 * 下班打卡设备号
	 */
	private String clockOffDevice;
	
	/**
	 * 下班外勤卡备注
	 */
	private String clockOffRemark;
	
	/**
	 * 补卡类型 0：全天补卡 1：上班补卡 2：下班补卡
	 *
	 */
	private String patchClockType;
	
	/**
	 * 补卡审批状态 0：审批中  1：已通过  2：未通过  3:已取消
	 */
	private String patchClockStatus;
	
	/**
	 * 销假打卡 0：销假打卡  1：不销假打卡
	 */
	private int revokeType;
	
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

	public String getClockOnDateTime() {
		return clockOnDateTime;
	}

	public void setClockOnDateTime(String clockOnDateTime) {
		this.clockOnDateTime = clockOnDateTime;
	}

	public String getClockOnStatus() {
		return clockOnStatus;
	}

	public void setClockOnStatus(String clockOnStatus) {
		this.clockOnStatus = clockOnStatus;
	}

	public String getClockOnLocation() {
		return clockOnLocation;
	}

	public void setClockOnLocation(String clockOnLocation) {
		this.clockOnLocation = clockOnLocation;
	}

	public String getClockOnDevice() {
		return clockOnDevice;
	}

	public void setClockOnDevice(String clockOnDevice) {
		this.clockOnDevice = clockOnDevice;
	}

	public String getClockOffDateTime() {
		return clockOffDateTime;
	}

	public void setClockOffDateTime(String clockOffDateTime) {
		this.clockOffDateTime = clockOffDateTime;
	}

	public String getClockOffStatus() {
		return clockOffStatus;
	}

	public void setClockOffStatus(String clockOffStatus) {
		this.clockOffStatus = clockOffStatus;
	}

	public String getClockOffLocation() {
		return clockOffLocation;
	}

	public void setClockOffLocation(String clockOffLocation) {
		this.clockOffLocation = clockOffLocation;
	}

	public String getClockOffDevice() {
		return clockOffDevice;
	}

	public void setClockOffDevice(String clockOffDevice) {
		this.clockOffDevice = clockOffDevice;
	}

	public String getPatchClockType() {
		return patchClockType;
	}

	public void setPatchClockType(String patchClockType) {
		this.patchClockType = patchClockType;
	}

	public String getPatchClockStatus() {
		return patchClockStatus;
	}

	public void setPatchClockStatus(String patchClockStatus) {
		this.patchClockStatus = patchClockStatus;
	}

	public String getClockOnRemark() {
		return clockOnRemark;
	}

	public void setClockOnRemark(String clockOnRemark) {
		this.clockOnRemark = clockOnRemark;
	}

	public String getClockOffRemark() {
		return clockOffRemark;
	}

	public void setClockOffRemark(String clockOffRemark) {
		this.clockOffRemark = clockOffRemark;
	}

	public int getRevokeType() {
		return revokeType;
	}

	public void setRevokeType(int revokeType) {
		this.revokeType = revokeType;
	}

}
