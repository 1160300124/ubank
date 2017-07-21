package com.ulaiber.web.model;

/**
 * APP端的模块
 * 
 * @author huangguoqing
 *
 */
public class Module {
	
	/**
	 * id
	 */
	private int mid;
	
	/**
	 * 模块名称
	 */
	private String moduleName;
	
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

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
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
