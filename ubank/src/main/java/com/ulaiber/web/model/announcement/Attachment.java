package com.ulaiber.web.model.announcement;

/** 
 * 公告附件
 *
 * @author  huangguoqing
 * @date 创建时间：2017年12月5日 上午11:44:07
 * @version 1.0 
 * @since 
 */
public class Attachment {
	
	/**
	 * 附件id
	 */
	private long att_id;
	
	/**
	 * 公告id
	 */
	private long aid;
	
	/**
	 * 附件名称
	 */
	private String attachment_name;
	
	/**
	 * 附件类型
	 */
	private String attachment_type;
	
	/**
	 * 附件地址
	 */
	private String attachment_path;
	
	/**
	 * 附件大小
	 */
	private String attachment_size;

	public long getAtt_id() {
		return att_id;
	}

	public void setAtt_id(long att_id) {
		this.att_id = att_id;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public String getAttachment_name() {
		return attachment_name;
	}

	public void setAttachment_name(String attachment_name) {
		this.attachment_name = attachment_name;
	}

	public String getAttachment_type() {
		return attachment_type;
	}

	public void setAttachment_type(String attachment_type) {
		this.attachment_type = attachment_type;
	}

	public String getAttachment_path() {
		return attachment_path;
	}

	public void setAttachment_path(String attachment_path) {
		this.attachment_path = attachment_path;
	}

	public String getAttachment_size() {
		return attachment_size;
	}

	public void setAttachment_size(String attachment_size) {
		this.attachment_size = attachment_size;
	}

}
