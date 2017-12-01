package com.ulaiber.web.model;

/**
 * 收款人对象实体
 * 
 * @author huangguoqing
 *
 */
public class Payee {
	
	/**
	 * 收款人账号 VarString(32)
	 */
	private String payeeAcctNo;
	
	/**
	 * 收款人姓名 VarString(62)
	 */
	private String payeeName;
	
	/**
	 * 收款金额 VarNumber(15,2)
	 */
	private double amount;
	
	/**
	 * 备注 VarString(220)
	 */
	private String note;
	
	/**
	 * 处理信息 VarString(50)
	 */
	private String message;

	public String getPayeeAcctNo() {
		return payeeAcctNo;
	}

	public void setPayeeAcctNo(String payeeAcctNo) {
		this.payeeAcctNo = payeeAcctNo;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
