package com.ulaiber.web.model.attendance;

/** 
 * 请假扣款
 *
 * @author  huangguoqing
 * @date 创建时间：2017年10月19日 上午10:08:09
 * @version 1.0 
 * @since 
 */
public class LeaveCutPayment {

	/**
	 * 请假扣款规则号
	 */
	private long lrid;
	
	/**
	 * 事假
	 */
	private String personalLeave;
	
	/**
	 * 病假
	 */
	private String sickLeave;
	
	/**
	 * 年假
	 */
	private String annualLeave;
	
	/**
	 * 调休
	 */
	private String daysOff;
	
	/**
	 * 婚假
	 */
	private String wdddingLeave;
	
	/**
	 * 产假
	 */
	private String materNityLeave;
	
	/**
	 * 其他
	 */
	private String elseLeave;

	public long getLrid() {
		return lrid;
	}

	public void setLrid(long lrid) {
		this.lrid = lrid;
	}

	public String getPersonalLeave() {
		return personalLeave;
	}

	public void setPersonalLeave(String personalLeave) {
		this.personalLeave = personalLeave;
	}

	public String getSickLeave() {
		return sickLeave;
	}

	public void setSickLeave(String sickLeave) {
		this.sickLeave = sickLeave;
	}

	public String getAnnualLeave() {
		return annualLeave;
	}

	public void setAnnualLeave(String annualLeave) {
		this.annualLeave = annualLeave;
	}

	public String getDaysOff() {
		return daysOff;
	}

	public void setDaysOff(String daysOff) {
		this.daysOff = daysOff;
	}

	public String getWdddingLeave() {
		return wdddingLeave;
	}

	public void setWdddingLeave(String wdddingLeave) {
		this.wdddingLeave = wdddingLeave;
	}

	public String getMaterNityLeave() {
		return materNityLeave;
	}

	public void setMaterNityLeave(String materNityLeave) {
		this.materNityLeave = materNityLeave;
	}

	public String getElseLeave() {
		return elseLeave;
	}

	public void setElseLeave(String elseLeave) {
		this.elseLeave = elseLeave;
	}
	
}
