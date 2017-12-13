package com.ulaiber.web.model.announcement;

/** 
 * 公告用户关系类
 *
 * @author  huangguoqing
 * @date 创建时间：2017年12月5日 上午11:51:29
 * @version 1.0 
 * @since 
 */
public class UserOfAnnouncement {
	
	/**
	 * 用户id
	 */
	private long userId;
	
	/**
	 * 公告id
	 */
	private long aid;
	
	/**
	 * 部门id
	 */
	private int deptId;
	
	/**
	 * 公司id
	 */
	private int companyId;
	
	/**
	 * 0：未读  1：已读
	 */
	private int type;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
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
