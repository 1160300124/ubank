package com.ulaiber.web.model;

/** 
 * 用户-考勤规则
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月29日 下午8:32:40
 * @version 1.0 
 * @since 
 */
public class UserOfRule {
	
	/**
	 * 用户id
	 */
	private long userId;
	
	/**
	 * 规则id
	 */
	private long rid;
	
	/**
	 * 部门id
	 */
	private int deptId;
	
	/**
	 * 公司id
	 */
	private int companyId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getRid() {
		return rid;
	}

	public void setRid(long rid) {
		this.rid = rid;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	
}
