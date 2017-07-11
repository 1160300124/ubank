package com.ulaiber.model;

import java.util.Date;
import java.util.List;

/**
 * 调用银行注册接口后返回的信息
 * @author daiqingwen
 * @date 2017-7-6
 */
public class Message {
	private String transId;  	//交易代码
	private String returnCode;	//返回代码
	private String returnMsg;	//返回说明
	private String timeStamp;		//时间戳
	private List<MSGContent> msgContent; //接口返回中body数据
	
	//get set 
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
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
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public List<MSGContent> getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(List<MSGContent> msgContent) {
		this.msgContent = msgContent;
	}

	@Override
	public String toString() {
		return "Message [transId=" + transId + ", returnCode=" + returnCode + ", returnMsg=" + returnMsg
				+ ", timeStamp=" + timeStamp + ", msgContent=" + msgContent + "]";
	}
	
	
	
}
