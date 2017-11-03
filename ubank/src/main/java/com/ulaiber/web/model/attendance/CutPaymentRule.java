package com.ulaiber.web.model.attendance;

/** 
 * 迟早，早退扣款实体类
 *
 * @author  huangguoqing
 * @date 创建时间：2017年11月2日 上午9:48:44
 * @version 1.0 
 * @since 
 */
public class CutPaymentRule {
	
	/**
	 * 分钟数
	 */
	private int minute;
	
	/**
	 * 扣款
	 */
	private double money;
	
	/**
	 * 扣款单位 0：元  1：天
	 */
	private int unit;

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}
}
