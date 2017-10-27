package com.ulaiber.web.service;

import java.util.List;

import com.ulaiber.web.model.*;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;

public interface UserService {
	
	/**
	 * 新增用户
	 * 
	 * @param user User
	 * @return boolean true/flase
	 */
	boolean save(User user);
	
	/**
	 * 更新ticket和token
	 * 
	 * @param user User
	 * @return boolean true/flase
	 */
	boolean update(User user);
	
	/**
	 * 删除用户
	 * 
	 * @param mobile 手机号码
	 * @return boolean true/flase
	 */
	boolean delete(String mobile);
	
	/**
	 * 根据手机号获取用户详情
	 * 
	 * @param mobile 手机号码
	 * @return User User
	 */
	User findByMobile(String mobile);
	
	/**
	 * 获取所有用户
	 * 
	 * @return List<User> List<User>
	 */
	List<User> findAll();
	
	/**
	 * 根据ticket和token获取用户
	 * 
	 * @param ticket ticket
	 * @param token token
	 * @return User User
	 */
	User getUserByTicketAndToken(String ticket, String token);
	
	/**
	 * 更新登录密码
	 * 
	 * @param mobile 手机号
	 * @param password 密码
	 * @return boolean
	 */
	boolean updateLoginPwd(String mobile, String password);
	
	/**
	 * 更新支付密码
	 * 
	 * @param mobile 手机号
	 * @param password 密码
	 * @return boolean
	 */
	boolean updatePayPwd(String mobile, String password);
	
	/**
	 * 更改用户银行卡信息
	 * 
	 * @param user
	 * @return boolean
	 */
	boolean updateForBankCard(User user);
	
	/**
	 * 根据用户名获取用户信息
	 * @param uesrName
	 * @return User
	 */
	User getUserByName(String uesrName);

	/**
	 * 银行开户接口
	 * @param user
	 * @return Message
	 */
	Message sendInfo(User user);

	/**
	 * 根据用户名获取系统所有菜单
	 * @return
	 */
	List<Menu> getAllMenuByUser( String userName,String sysflag);

	/**
	 * 校验支付密码是否正确
	 * @param mobile
	 * @param password
	 * @return
	 */
	boolean validatePayPwd(String mobile, String password);

	/**
	 * 获取所有系统菜单 
	 * @return
	 */
	List<Menu> getAllMenu(String roleid,String sysflag);
	
	/**
	 * 获取一个公司的各个部门下的所有用户
	 * @param comNum
	 * @return
	 */
	List<User> getUsersByComNum(String comNum, String search);

	/**
	 * 银行后台用户登录
	 * @param mobile 手机号码
	 * @return BankUsers
	 */
	BankUsers bankUserLogin(String mobile);

	/**
	 * 新增二类账户信息
	 * @param sa
	 * @return int
	 */
    int insertSecondAccount(SecondAcount sa);

	/**
	 * 验证邀请码
	 * @param code 邀请码
	 * @return Company
	 */
	Company validateCode(String code);

	/**
	 * 根据公司编号查询绑定的银行
	 * @param companyNumber 公司编号
	 * @return Bank
	 */
	Bank queryBankByCompay(int companyNumber);

	/**
	 * 获取二类帐户信息
	 * @param userid 用户id
	 * @return SecondAcount
	 */
	SecondAcount getSecondAccountByUserId(int userid);

	/**
	 * 新增用户绑定银行卡信息
	 * @param userid 用户id
	 * @param bankNo 银行编号
	 * @param bankCardNo 银行卡号
	 * @return int
	 */
	int insertUserToBank(int userid, int bankNo, String bankCardNo);
}
