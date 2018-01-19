package com.ulaiber.web.model.attendance;

import com.ulaiber.web.model.Company;
import com.ulaiber.web.model.Departments;

/** 
 * 扣款实体类
 *
 * @author  huangguoqing
 * @date 创建时间：2017年11月1日 下午12:11:32
 * @version 1.0 
 * @since 
 */
public class CutPayment {
	
	/**
	 * 扣款id
	 */
	private long cid;
	
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
	 * 扣款时间
	 */
	private String cutDate;
	
	/**
	 * 扣款类型 0：迟到  1：早退  2：未打卡  3：旷1天工  4：旷半天工 5：请假
	 */
	private String cutType;
	
	/**
	 * 扣款原因
	 */
	private String cutReason;
	
	/**
	 * 扣款金额
	 */
	private double cutMoney;

	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
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

	public String getCutDate() {
		return cutDate;
	}

	public void setCutDate(String cutDate) {
		this.cutDate = cutDate;
	}

	public String getCutType() {
		return cutType;
	}

	public void setCutType(String cutType) {
		this.cutType = cutType;
	}

	public String getCutReason() {
		return cutReason;
	}

	public void setCutReason(String cutReason) {
		this.cutReason = cutReason;
	}

	public double getCutMoney() {
		return cutMoney;
	}

	public void setCutMoney(double cutMoney) {
		this.cutMoney = cutMoney;
	}
	
}
