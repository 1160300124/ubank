package com.ulaiber.web.service;

import java.util.List;

import com.ulaiber.web.model.attendance.SalaryRule;

/** 
 * 工资规则配置
 *
 * @author  huangguoqing
 * @date 创建时间：2017年10月20日 下午5:04:59
 * @version 1.0 
 * @since 
 */
public interface SalaryRuleService {
	
	/**
	 * 新增工资规则配置
	 */
	boolean save(SalaryRule salaryRule);
	
	/**
	 * 查询所有工资配置 带分页
	 * @return List<SalaryRule>
	 */
	List<SalaryRule> getSalaryRules(int limit, int offset, String order, String search);
	
	/**
	 * 获取总记录数
	 * @return
	 */
	int getTotal();
	
	/**
	 * 根据rid批量删除工资规则
	 * @return
	 */
	boolean deleteByRids(List<Long> rids);
	
	/**
	 * 根据rid修改工资规则
	 * @param salaryRule
	 * @return
	 */
	boolean updateSalaryRuleByRid(SalaryRule salaryRule);
	
}
