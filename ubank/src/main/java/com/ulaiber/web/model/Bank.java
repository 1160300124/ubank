package com.ulaiber.web.model;

import java.io.Serializable;

public class Bank implements Serializable{
	
	//银行编号
	private String bankNo;
	
	//银行名称
	private String bankName;

	//银行类型
	private int type;

	//银行连号
	private String number;

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
