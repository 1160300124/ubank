package com.ulaiber.web.service;

import com.ulaiber.web.model.AttendanceRule;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月16日 下午12:29:14
 * @version 1.0 
 * @since 
 */
public interface AttendanceRuleService {

	/**
	 * 新增规则
	 * @param rule
	 * @return
	 */
	boolean save(AttendanceRule rule);
	
	/**
	 * 更新规则
	 * @param rule
	 * @return
	 */
	boolean update(AttendanceRule rule);
	
	/**
	 * 根据userId获取考勤规则
	 * @param userId
	 * @return
	 */
	AttendanceRule getRuleByUserId(long userId);
	
	/**
	 * 根据手机号获取考勤规则
	 * @param mobile
	 * @return
	 */
	AttendanceRule getRuleByMobile(String mobile);
	
}
