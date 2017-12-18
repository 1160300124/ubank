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
	private String operateUserId;
	
	/**
	 * 操作人姓名
	 */
	private String oprateUserName;
	
	/**
	 * 操作时间
	 */
	private String operateDate;
	
	/**
	 * 用户ID
	 */
	private long userId;
	
	/**
	 * 当前工资
	 */
	private double currentSalary;
	
	
	/**
	 * 调之后工资
	 */
	private double afterChangeSalary;
	
	/**
	 * 调整幅度
	 */
	private String changeRange;
	
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

	public String getOperateUserId() {
		return operateUserId;
	}

	public void setOperateUserId(String operateUserId) {
		this.operateUserId = operateUserId;
	}

	public String getOprateUserName() {
		return oprateUserName;
	}

	public void setOprateUserName(String oprateUserName) {
		this.oprateUserName = oprateUserName;
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

	public double getCurrentSalary() {
		return currentSalary;
	}

	public void setCurrentSalary(double currentSalary) {
		this.currentSalary = currentSalary;
	}

	public double getAfterChangeSalary() {
		return afterChangeSalary;
	}

	public void setAfterChangeSalary(double afterChangeSalary) {
		this.afterChangeSalary = afterChangeSalary;
	}

	public String getChangeRange() {
		return changeRange;
	}

	public void setChangeRange(String changeRange) {
		this.changeRange = changeRange;
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
