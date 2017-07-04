package com.ulaiber.model;

/**
 * 用户操作记录表
 * 
 * @author huangguoqing
 *
 */
public class UserOperaDetail {
	
	/** 流水ID */
	private Long opera_id;
	
	/** 用户ID */
	private Long user_id;
	
	/** 用户操作类型 */
	private Integer opera_type;
	
	/** 流水发生时间 */
	private String opera_date;
	
	/** 操作IP */
	private String opera_ip;
	
	/** 各种不同操作类型对应的关联码 */
	private String opera_typevalue;

	public Long getOpera_id() {
		return opera_id;
	}

	public void setOpera_id(Long opera_id) {
		this.opera_id = opera_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Integer getOpera_type() {
		return opera_type;
	}

	public void setOpera_type(Integer opera_type) {
		this.opera_type = opera_type;
	}

	public String getOpera_date() {
		return opera_date;
	}

	public void setOpera_date(String opera_date) {
		this.opera_date = opera_date;
	}

	public String getOpera_ip() {
		return opera_ip;
	}

	public void setOpera_ip(String opera_ip) {
		this.opera_ip = opera_ip;
	}

	public String getOpera_typevalue() {
		return opera_typevalue;
	}

	public void setOpera_typevalue(String opera_typevalue) {
		this.opera_typevalue = opera_typevalue;
	}
	
}
