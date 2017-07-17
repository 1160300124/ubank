package com.ulaiber.web.model;
/**
 * 调用银行注册接口后返回的body中的数据
 * @author daiqingwen
 * @date 2017-7-6
 */
public class MSGContent {
	private String userId;		//会员号
	private String idType;		//证件类型
	private String idNo;		//证件号码
	private String status;		//交易状态
	private String openId;		//用户编号
	private String returnCode;	//返回代码
	private String returnMsg;	//返回说明
	
	//get set
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	
	
	
}