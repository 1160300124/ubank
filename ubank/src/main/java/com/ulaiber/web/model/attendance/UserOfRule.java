package com.ulaiber.web.model.attendance;

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
	
	/**
	 * 0：需要考勤规则  1：不需要考勤规则  2：既需要也不需要规则(二者都满足时以不需要为准)
	 */
	private int type;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
