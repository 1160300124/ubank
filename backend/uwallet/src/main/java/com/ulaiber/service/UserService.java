package com.ulaiber.service;

import java.util.List;

import com.ulaiber.model.User;

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

}
