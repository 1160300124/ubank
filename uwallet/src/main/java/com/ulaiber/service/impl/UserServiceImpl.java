package com.ulaiber.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.dao.UserDao;
import com.ulaiber.model.User;
import com.ulaiber.service.BaseService;
import com.ulaiber.service.UserService;
import com.ulaiber.utils.DateTimeUtil;
import com.ulaiber.utils.MD5Util;

@Service
public class UserServiceImpl extends BaseService implements UserService {
	
	@Resource
	private UserDao mapper;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean delete(String mobile) {
		
		return mapper.delete(mobile);
	}

	@Override
	public List<User> findAll() {
		List<User> findAllList = mapper.findAll();
		return findAllList;
	}

	@Override
	public User findByMobile(String mobile) {

		User user = mapper.getUserByMobile(mobile);
		
		return user;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean save(User user) {
	
		user.setRole_id(2);
		user.setCreateTime(DateTimeUtil.date2Str(new Date()));
		user.setLogin_password(MD5Util.getEncryptedPwd(user.getLogin_password()));
		user.setPay_password(MD5Util.getEncryptedPwd(user.getPay_password()));

		return mapper.save(user) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean update(User user) {

		user.setLoginTime(DateTimeUtil.date2Str(new Date()));
		return mapper.update(user);
	}

	@Override
	public User getUserByTicketAndToken(String ticket, String token) {
		
		return mapper.getUserByTicketAndToken(ticket, token);
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean updateLoginPwd(String mobile, String password) {
		
		User user = new User();
		user.setMobile(mobile);
		user.setLogin_password(MD5Util.getEncryptedPwd(password));
		return mapper.updateLoginPwd(user);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean updatePayPwd(String mobile, String password) {
		
		User user = new User();
		user.setMobile(mobile);
		user.setPay_password(MD5Util.getEncryptedPwd(password));
		return mapper.updatePayPwd(user);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean updateForBankCard(User user) {
		
		return mapper.updateForBankCard(user);
	}

	@Override
	public User getUserByName(String uesrName) {

		return mapper.getUserByName(uesrName);
	}
	
	

}
