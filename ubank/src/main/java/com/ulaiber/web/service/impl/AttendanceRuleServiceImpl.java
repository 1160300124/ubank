package com.ulaiber.web.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.AttendanceRuleDao;
import com.ulaiber.web.model.AttendanceRule;
import com.ulaiber.web.service.AttendanceRuleService;
import com.ulaiber.web.service.BaseService;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月16日 下午12:32:21
 * @version 1.0 
 * @since 
 */
@Service
public class AttendanceRuleServiceImpl extends BaseService implements AttendanceRuleService {
	
	@Resource
	private AttendanceRuleDao dao;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(AttendanceRule rule) {

		return dao.save(rule) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean update(AttendanceRule rule) {

		return dao.update(rule);
	}

	@Override
	public AttendanceRule getRuleByUserId(long userId) {
		
		return dao.getRuleByUserId(userId);
	}

	@Override
	public AttendanceRule getRuleByMobile(String mobile) {
		
		return dao.getRuleByMobile(mobile);
	}

}
