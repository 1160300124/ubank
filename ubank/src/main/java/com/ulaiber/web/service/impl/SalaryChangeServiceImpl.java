package com.ulaiber.web.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.SalaryChangeDao;
import com.ulaiber.web.dao.UserDao;
import com.ulaiber.web.model.salary.SalaryChange;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.SalaryChangeService;
import com.ulaiber.web.utils.DateTimeUtil;

/** 
 * 工资调整业务实现类
 *
 * @author  huangguoqing
 * @date 创建时间：2017年12月20日 上午11:24:27
 * @version 1.0 
 * @since 
 */
@Service
public class SalaryChangeServiceImpl extends BaseService implements SalaryChangeService {
	
	@Resource
	private SalaryChangeDao dao;
	
	@Resource
	private UserDao userDao;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(SalaryChange salary) {
		salary.setOperateDate(DateTimeUtil.date2Str(new Date()));
		if (userDao.updateSalaryByUserId(salary.getUserId(), salary.getCurrentSalary()) > 0){
			return dao.save(salary) > 0;
		}
		return false;
	}

	@Override
	public List<SalaryChange> getSalaryChangeByUserId(long userId) {
		return dao.getSalaryChangeByUserId(userId);
	}

}
