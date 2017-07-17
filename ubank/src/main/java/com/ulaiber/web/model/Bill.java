package com.ulaiber.web.model;

public class Bill {
	
	//单号
	private String bill_num;
	
	//账单的操作类型，如工资转入，提现等
	private String bill_type;
	
	//每次转入或提现的钱
	private String bill_money;
	
	//账单时间
	private String bill_date;

	public String getBill_num() {
		return bill_num;
	}

	public void setBill_num(String bill_num) {
		this.bill_num = bill_num;
	}

	public String getBill_type() {
		return bill_type;
	}

	public void setBill_type(String bill_type) {
		this.bill_type = bill_type;
	}

	public String getBill_date() {
		return bill_date;
	}

	public void setBill_date(String bill_date) {
		this.bill_date = bill_date;
	}

	public String getBill_money() {
		return bill_money;
	}

	public void setBill_money(String bill_money) {
		this.bill_money = bill_money;
	}

	
}
