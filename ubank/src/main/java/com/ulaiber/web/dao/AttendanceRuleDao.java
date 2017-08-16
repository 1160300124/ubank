package com.ulaiber.web.dao;

import com.ulaiber.web.model.AttendanceRule;

/** 
 * 考勤规则数据接口
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月15日 下午7:04:56
 * @version 1.0 
 * @since 
 */
public interface AttendanceRuleDao {

	/**
	 * 新增规则
	 * @param rule
	 * @return
	 */
	int save(AttendanceRule rule);
	
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
