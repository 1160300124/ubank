package com.ulaiber.web.model.attendance;

/** 
 * 早退
 *
 * @author  huangguoqing
 * @date 创建时间：2017年10月20日 下午2:32:32
 * @version 1.0 
 * @since 
 */
public class LeaveEarlyCutPayment {
	
	/**
	 * 早退扣款规则号
	 */
	private long leid;
	
	/**
	 * 早退分钟数
	 */
	private int leaveEarlyMinuters;
	
	/**
	 * 早退扣款
	 */
	private double leaveEarlyCutPayment;
	
	/**
	 * 早退扣款单位 0：元  1：天
	 */
	private int leaveEarlyCutUnit;

	public long getLeid() {
		return leid;
	}

	public void setLeid(long leid) {
		this.leid = leid;
	}

	public int getLeaveEarlyMinuters() {
		return leaveEarlyMinuters;
	}

	public void setLeaveEarlyMinuters(int leaveEarlyMinuters) {
		this.leaveEarlyMinuters = leaveEarlyMinuters;
	}

	public double getLeaveEarlyCutPayment() {
		return leaveEarlyCutPayment;
	}

	public void setLeaveEarlyCutPayment(double leaveEarlyCutPayment) {
		this.leaveEarlyCutPayment = leaveEarlyCutPayment;
	}

	public int getLeaveEarlyCutUnit() {
		return leaveEarlyCutUnit;
	}

	public void setLeaveEarlyCutUnit(int leaveEarlyCutUnit) {
		this.leaveEarlyCutUnit = leaveEarlyCutUnit;
	}
	
}
