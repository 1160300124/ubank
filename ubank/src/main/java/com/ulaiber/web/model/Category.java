package com.ulaiber.web.model;

/**
 * APP端的页面
 * 
 * @author huangguoqing
 *
 */
public class Category {
	
	/**
	 * id
	 */
	private int cid;
	
	/**
	 * 类别名称
	 */
	private String categoryName;
	
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

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
