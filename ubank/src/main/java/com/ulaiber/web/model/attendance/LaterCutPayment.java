package com.ulaiber.web.model.attendance;

/** 
 * 考勤扣款规则
 *
 * @author  huangguoqing
 * @date 创建时间：2017年10月19日 上午9:53:41
 * @version 1.0 
 * @since 
 */
public class LaterCutPayment {
	
	/**
	 * 考勤扣款规则号
	 */
	private long laid;
	
	/**
	 * 迟到分钟数
	 */
	private int laterMinutes;
	
	/**
	 * 迟到扣款
	 */
	private double laterCutPayment;
	
	/**
	 * 迟到扣款单位 0：元  1：天
	 */
	private int laterCutUnit;
	
	public long getLaid() {
		return laid;
	}

	public void setLaid(long laid) {
		this.laid = laid;
	}

	public int getLaterMinutes() {
		return laterMinutes;
	}

	public void setLaterMinutes(int laterMinutes) {
		this.laterMinutes = laterMinutes;
	}

	public double getLaterCutPayment() {
		return laterCutPayment;
	}

	public void setLaterCutPayment(double laterCutPayment) {
		this.laterCutPayment = laterCutPayment;
	}

	public int getLaterCutUnit() {
		return laterCutUnit;
	}

	public void setLaterCutUnit(int laterCutUnit) {
		this.laterCutUnit = laterCutUnit;
	}

}
