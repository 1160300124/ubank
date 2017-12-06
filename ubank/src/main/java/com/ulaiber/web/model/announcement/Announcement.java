package com.ulaiber.web.model.announcement;

import java.util.List;

/** 
 * 公告实体类
 *
 * @author  huangguoqing
 * @date 创建时间：2017年12月5日 上午10:52:05
 * @version 1.0 
 * @since 
 */
public class Announcement {
	
	/**
	 * 公告流水号
	 */
	private long aid;
	
	/**
	 * 公告公司id
	 */
	private int companyId;
	
	/**
	 * 公告标题
	 */
	private String announceTitle;
	
	/**
	 * 公告正文
	 */
	private String announceBody;
	
	/**
	 * 公告创建时间
	 */
	private String createTime;
	
	/*
	 * 操作人id
	 */
	private long operateUserId;
	
	/**
	 * 操作人名称
	 */
	private String operateUserName;
	
	/**
	 * 公告发送人数
	 */
	private int announceCount;
	
	/**
	 * 要发送公告的用户id
	 */
	private List<Long> userIds;
	
	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getAnnounceTitle() {
		return announceTitle;
	}

	public void setAnnounceTitle(String announceTitle) {
		this.announceTitle = announceTitle;
	}

	public String getAnnounceBody() {
		return announceBody;
	}

	public void setAnnounceBody(String announceBody) {
		this.announceBody = announceBody;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	public int getAnnounceCount() {
		return announceCount;
	}

	public void setAnnounceCount(int announceCount) {
		this.announceCount = announceCount;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
	
}
