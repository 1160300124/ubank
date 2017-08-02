package com.ulaiber.web.model;

/**
 * 第三方URL实体类
 * @author huangguoqing
 *
 */
public class ThirdUrl {

	/**
	 * URL编号
	 */
	private long uid;
	
	/**
	 * URL名称
	 */
	private String urlName;
	
	/**
	 * URL链接
	 */
	private String url;
	
	/**
	 * 图片路径
	 */
	private String picPath;
	
	/**
	 * URL属于哪一类别
	 */
	private Category category;
	
	/**
	 * URL模块
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
	 * 创建时间
	 */
	private String updateTime;
	
	/**
	 * 备注
	 */
	private String remark;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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
