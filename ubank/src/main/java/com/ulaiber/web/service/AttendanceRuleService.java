package com.ulaiber.web.service;

import java.util.List;

import com.ulaiber.web.model.Holiday;
import com.ulaiber.web.model.attendance.AttendanceRule;
import com.ulaiber.web.model.attendance.UserOfRule;

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
	boolean save(AttendanceRule rule, String ruleData, String noRuleData);
	
	/**
	 * 更新规则
	 * @param rule
	 * @return
	 */
	boolean update(AttendanceRule rule, String ruleData, String noRuleData);
	
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
	
	/**
	 * 获取全部考勤规则
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<AttendanceRule> getRules(int offset, int limit, String orderby);
	
	/**
	 * 获取总记录数
	 * @return
	 */
	int getCount();
	
	/**
	 * 根据指定年获取节假日期
	 * @param year
	 * @return
	 */
	Holiday getHolidaysByYear(String year);
	
	/**
	 * 批量删除规则
	 * @param rids
	 * @return
	 */
	boolean deleteRulesByRids(List<Long> rids);
	
	/**
	 * 根据规则id获取用户id
	 * @param rid
	 * @return
	 */
	List<UserOfRule> getUserIdsByRid(Long rid);
	
	/**
	 * 查询公司下的打卡人员
	 * @param companyId
	 * @return
	 */
	List<UserOfRule> getUserIdsByComId(int companyId);
	
	/**
	 * 根据公司id获取考勤规则
	 * @param companyId
	 * @return
	 */
	List<AttendanceRule> getRulesByCompanyId(int companyId);
	
}
