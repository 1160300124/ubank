package com.ulaiber.web.model.announcement;

/** 
 * 公告附件
 *
 * @author  huangguoqing
 * @date 创建时间：2017年12月5日 上午11:44:07
 * @version 1.0 
 * @since 
 */
public class AnnouncementAttachment {
	
	/**
	 * 附件id
	 */
	private long attId;
	
	/**
	 * 公告id
	 */
	private long aid;
	
	/**
	 * 附件类型
	 */
	private String attType;
	
	/**
	 * 附件地址
	 */
	private String attPath;

	public long getAttId() {
		return attId;
	}

	public void setAttId(long attId) {
		this.attId = attId;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public String getAttType() {
		return attType;
	}

	public void setAttType(String attType) {
		this.attType = attType;
	}

	public String getAttPath() {
		return attPath;
	}

	public void setAttPath(String attPath) {
		this.attPath = attPath;
	}
	
}
