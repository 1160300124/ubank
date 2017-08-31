package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.AttendanceRule;
import com.ulaiber.web.model.Holiday;
import com.ulaiber.web.model.UserOfRule;

/**
 * 考勤规则数据接口
 *
 * @author huangguoqing
 * @version 1.0
 * @date 创建时间：2017年8月15日 下午7:04:56
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
	
	/**
	 * 获取全部考勤规则
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<AttendanceRule> getRules(Map<String, Object> params);
	
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
	 * 批量插入关系表
	 * @param ids
	 * @return
	 */
	int batchInsertUserOfRule(List<Map<String, Object>> ids);
	
	/**
	 * 批量删除规则
	 * @param rids
	 * @return
	 */
	int deleteRulesByRids(List<Long> rids);
	
	
	/**
	 * 批量删除规则和用户关系
	 * @param rids
	 * @return
	 */
	int deleteUserOfRulesByRids(List<Long> rids);
	
	/**
	 * 根据规则id获取用户id
	 * @param rid
	 * @return
	 */
	List<UserOfRule> getUserIdsByRid(Long rid);

}
