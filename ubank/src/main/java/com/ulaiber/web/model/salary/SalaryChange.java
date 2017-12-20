package com.ulaiber.web.model.salary;

/** 
 * 工资调整实体类
 *
 * @author  huangguoqing
 * @date 创建时间：2017年12月14日 下午5:59:25
 * @version 1.0 
 * @since 
 */
public class SalaryChange {
	
	/**
	 * id
	 */
	private long cid;
	
	/**
	 * 操作人ID
	 */
	private long operateUserId;
	
	/**
	 * 操作人姓名
	 */
	private String operateUserName;
	
	/**
	 * 操作时间
	 */
	private String operateDate;
	
	/**
	 * 用户ID
	 */
	private long userId;
	
	/**
	 * 调之前工资
	 */
	private double oldSalary;
	
	/**
	 * 当前工资
	 */
	private double currentSalary;
	
	/**
	 * 调整幅度
	 */
	private String changePercent;
	
	/**
	 * 工资调整时间
	 */
	private String changeDate;
	
	/**
	 * 调整原因
	 */
	private String changeReason;

	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	public long getOperateUserId() {
		return operateUserId;
	}

	public void setOperateUserId(long operateUserId) {
		this.operateUserId = operateUserId;
	}

	public String getOperateUserName() {
		return operateUserName;
	}

	public void setOperateUserName(String operateUserName) {
		this.operateUserName = operateUserName;
	}

	public String getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public double getOldSalary() {
		return oldSalary;
	}

	public void setOldSalary(double oldSalary) {
		this.oldSalary = oldSalary;
	}

	public double getCurrentSalary() {
		return currentSalary;
	}

	public void setCurrentSalary(double currentSalary) {
		this.currentSalary = currentSalary;
	}

	public String getChangePercent() {
		return changePercent;
	}

	public void setChangePercent(String changePercent) {
		this.changePercent = changePercent;
	}

	public String getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}

	public String getChangeReason() {
		return changeReason;
	}

	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}
	
}
