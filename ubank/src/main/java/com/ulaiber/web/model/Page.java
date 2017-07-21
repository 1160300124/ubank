package com.ulaiber.web.model;

/**
 * APP端的页面
 * 
 * @author huangguoqing
 *
 */
public class Page {
	
	/**
	 * id
	 */
	private int pid;
	
	/**
	 * 页面名称
	 */
	private String pageName;
	
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

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
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
