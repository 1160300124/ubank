package com.ulaiber.web.model;

import java.io.Serializable;

public class Bill implements Serializable{
	
	private String RqUID;		//交易流水号
	private String SubAcctNo;	//平台理财专属子账户
	private double amount;		//金额
	private int trading;		//交易类型 0 提现 1 工资转入
	private int tradingStatus;	//交易状态 0 处理中 1 成功 2 失败
	private String CreateDate;	//创建时间
	private String updateTime;	//更新时间
	private String bankCardNo;	//银行卡号
	private String bankName;	//银行名称

	public String getRqUID() {
		return RqUID;
	}

	public void setRqUID(String rqUID) {
		RqUID = rqUID;
	}

	public String getSubAcctNo() {
		return SubAcctNo;
	}

	public void setSubAcctNo(String subAcctNo) {
		SubAcctNo = subAcctNo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getTrading() {
		return trading;
	}

	public void setTrading(int trading) {
		this.trading = trading;
	}

	public int getTradingStatus() {
		return tradingStatus;
	}

	public void setTradingStatus(int tradingStatus) {
		this.tradingStatus = tradingStatus;
	}


	public String getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


}
