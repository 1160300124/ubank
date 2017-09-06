package com.ulaiber.web.model;

import java.io.Serializable;

public class User implements Serializable {
	
	//用户编号
	private long id;

	//用户名称
	private String userName;

	//登录密码
	private String login_password;

	//支付密码
	private String pay_password;

	//登录凭据
	private String login_ticket;

	//访问令牌
	private String access_token;

	//手机号码
	private String mobile;

	//银行预留手机号码
	private String reserve_mobile;
	
	//用户所属公司ID
	private int companyId;

	//集团编号
	private String groupNumber;

	//公司编号
	private String companyNumber;

	//公司名称
	private String com_name;

	//部门编号
	private String dept_number;

	//部门名称
	private String dept_name;

	//证件类型 1:身份证;2:护照;3:港澳通行证;21:工作证;99:其它
	private String cardType;

	//证件号码 (目前只支持身份证)
	private String cardNo;

	//绑定银行
	private Bank bank;

	//银行名称
	private String bankName;

	//银行编号
	private String bankNo;

	//绑定银行卡号
	private String bankCardNo;

	//二类户账号
	private String secondBankCardNo;

	//账户余额
	private double balance;

	//邮箱
	private String email;

	//状态 0:启用;1:停用;2:已注销
	private int status;

	//0:管理员;1:普通用户
	private int role_id;

	//角色名称
	private String role_name;

	//添加时间
	private String createTime;

	//过期时间
	private String expiredTime;

	//登录时间
	private String loginTime;

	//备注
	private String remark;

	//连续登陆失败次数,登陆成功后请归零
	private int login_fail_num;

	//最近登陆失败时间
	private String login_fail_time;

	//角色标识
	private String sysflag;

	//员工头像
	private String image;

	//是否作废
	private String disabled;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLogin_password() {
		return login_password;
	}

	public void setLogin_password(String login_password) {
		this.login_password = login_password;
	}

	public String getPay_password() {
		return pay_password;
	}

	public void setPay_password(String pay_password) {
		this.pay_password = pay_password;
	}

	public String getLogin_ticket() {
		return login_ticket;
	}

	public void setLogin_ticket(String login_ticket) {
		this.login_ticket = login_ticket;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getReserve_mobile() {
		return reserve_mobile;
	}

	public void setReserve_mobile(String reserve_mobile) {
		this.reserve_mobile = reserve_mobile;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getCompanyNumber() {
		return companyNumber;
	}

	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}

	public String getCom_name() {
		return com_name;
	}

	public void setCom_name(String com_name) {
		this.com_name = com_name;
	}

	public String getDept_number() {
		return dept_number;
	}

	public void setDept_number(String dept_number) {
		this.dept_number = dept_number;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(String expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getLogin_fail_num() {
		return login_fail_num;
	}

	public void setLogin_fail_num(int login_fail_num) {
		this.login_fail_num = login_fail_num;
	}

	public String getLogin_fail_time() {
		return login_fail_time;
	}

	public void setLogin_fail_time(String login_fail_time) {
		this.login_fail_time = login_fail_time;
	}

	public String getSecondBankCardNo() {
		return secondBankCardNo;
	}

	public void setSecondBankCardNo(String secondBankCardNo) {
		this.secondBankCardNo = secondBankCardNo;
	}

	public String getSysflag() {
		return sysflag;
	}

	public void setSysflag(String sysflag) {
		this.sysflag = sysflag;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
}
