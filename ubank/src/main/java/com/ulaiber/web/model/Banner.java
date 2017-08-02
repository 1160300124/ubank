package com.ulaiber.web.model;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年7月24日 下午3:03:30
 * @version 1.0 
 * @since 
 */
public class Banner {
	
	/**
	 * banner编号
	 */
	private long bid; 
	
	/**
	 * banner名称
	 */
	private String bannerName;
	
	/**
	 * banner图片地址
	 */
	private String picPath;
	
	/**
	 * banner链接
	 */
	private String url;
	
	/**
	 * banner模块
	 */
	private Module module;
	
	/**
	 * 排序
	 */
	private int orderby;
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * 最近修改时间
	 */
	private String updateTime;
	
	/**
	 * 备注
	 */
	private String remark;

	public long getBid() {
		return bid;
	}

	public void setBid(long bid) {
		this.bid = bid;
	}

	public String getBannerName() {
		return bannerName;
	}

	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public int getOrderby() {
		return orderby;
	}

	public void setOrderby(int orderby) {
		this.orderby = orderby;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
