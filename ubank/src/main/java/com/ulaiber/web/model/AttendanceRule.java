package com.ulaiber.web.model;

/** 
 * 考勤规则
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月15日 下午6:34:59
 * @version 1.0 
 * @since 
 */
public class AttendanceRule {
	
	/**
	 * 考勤规则id
	 */
	private long rid;
	
	/**
	 * 上班打卡时间
	 */
	private String clockOnTime;
	
	/**
	 * 下班打卡时间
	 */
	private String clockOffTime;
	
	/**
	 * 上班打卡提前小时数
	 */
	private int clockOnAdvanceHours;
	
	/**
	 * 最早上班打卡时间
	 */
	private String clockOnStartTime;
	
	/**
	 * 下班打卡延迟小时数
	 */
	private int clockOffDelayHours;
	
	/**
	 * 最晚下班打卡时间
	 */
	private String clockOffEndTime;
	
	/**
	 * 休息开始时间
	 */
	private String restStartTime;
	
	/**
	 * 休息结束时间
	 */
	private String restEndTime;
	
	/**
	 * 公司经纬度
	 */
	private String longit_latit;
	
	/**
	 * 公司地址
	 */
	private String clockLocation;
	
	/**
	 * 打卡范围，离公司多少m
	 */
	private int clockBounds;

	public long getRid() {
		return rid;
	}

	public void setRid(long rid) {
		this.rid = rid;
	}

	public String getClockOnTime() {
		return clockOnTime;
	}

	public void setClockOnTime(String clockOnTime) {
		this.clockOnTime = clockOnTime;
	}

	public String getClockOffTime() {
		return clockOffTime;
	}

	public void setClockOffTime(String clockOffTime) {
		this.clockOffTime = clockOffTime;
	}

	public int getClockOnAdvanceHours() {
		return clockOnAdvanceHours;
	}

	public void setClockOnAdvanceHours(int clockOnAdvanceHours) {
		this.clockOnAdvanceHours = clockOnAdvanceHours;
	}

	public String getClockOnStartTime() {
		return clockOnStartTime;
	}

	public void setClockOnStartTime(String clockOnStartTime) {
		this.clockOnStartTime = clockOnStartTime;
	}

	public int getClockOffDelayHours() {
		return clockOffDelayHours;
	}

	public void setClockOffDelayHours(int clockOffDelayHours) {
		this.clockOffDelayHours = clockOffDelayHours;
	}

	public String getClockOffEndTime() {
		return clockOffEndTime;
	}

	public void setClockOffEndTime(String clockOffEndTime) {
		this.clockOffEndTime = clockOffEndTime;
	}

	public String getRestStartTime() {
		return restStartTime;
	}

	public void setRestStartTime(String restStartTime) {
		this.restStartTime = restStartTime;
	}

	public String getRestEndTime() {
		return restEndTime;
	}

	public void setRestEndTime(String restEndTime) {
		this.restEndTime = restEndTime;
	}

	public String getLongit_latit() {
		return longit_latit;
	}

	public void setLongit_latit(String longit_latit) {
		this.longit_latit = longit_latit;
	}

	public String getClockLocation() {
		return clockLocation;
	}

	public void setClockLocation(String clockLocation) {
		this.clockLocation = clockLocation;
	}

	public int getClockBounds() {
		return clockBounds;
	}

	public void setClockBounds(int clockBounds) {
		this.clockBounds = clockBounds;
	}
	
}
