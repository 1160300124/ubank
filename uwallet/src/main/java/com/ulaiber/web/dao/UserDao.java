package com.ulaiber.web.dao;

import java.util.List;

import com.ulaiber.web.model.Menu;
import com.ulaiber.web.model.User;

/**
 * user数据库接口
 * 
 * @author huangguoqing
 *
 */
public interface UserDao {

	/**
	 * 新增用户
	 * 
	 * @param user User
	 * @return int 
	 */
	int save(User user);
	
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
	User getUserByMobile(String mobile);
	
	/**
	 * 获取所有用户
	 * 
	 * @return List<User> List<User>
	 */
	List<User> findAll();
	
	/**
	 * 根据ticket和token获取用户
	 * 
	 * @param user user
	 * @return User User
	 */
	User getUserByTicketAndToken(User user);
	
	/**
	 * 更新登录密码
	 * 
	 * @param user User
	 * @return boolean 
	 */
	boolean updateLoginPwd(User user);
	
	/**
	 * 更新支付密码
	 * 
	 * @param user
	 * @return boolean
	 */
	boolean updatePayPwd(User user);
	
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
	 * 获取系统所有菜单
	 * @return
	 */
	List<Menu> getAllMenu();
	
}
