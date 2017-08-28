package com.ulaiber.web.model;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月24日 下午4:45:32
 * @version 1.0 
 * @since 
 */
public class Holiday {

	/**
	 * id
	 */
	private long hid;
	
	/**
	 * 哪一年
	 */
	private String year;
	
	/**
	 * 节假日期
	 */
	private String holiday;
	
	/**
	 * 节假日调休日期
	 */
	private String workday;

	public long getHid() {
		return hid;
	}

	public void setHid(long hid) {
		this.hid = hid;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getHoliday() {
		return holiday;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	public String getWorkday() {
		return workday;
	}

	public void setWorkday(String workday) {
		this.workday = workday;
	}
	
}
