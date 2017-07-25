package com.ulaiber.web.model;

import java.io.Serializable;

public class Bank implements Serializable{
	
	//银行编号
	private String bankNo;
	
	//银行名称
	private String bankName;

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
	
}
