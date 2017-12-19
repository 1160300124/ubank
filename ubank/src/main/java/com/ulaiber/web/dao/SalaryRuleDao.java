package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.salary.SalaryRule;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年10月19日 下午4:53:08
 * @version 1.0 
 * @since 
 */
public interface SalaryRuleDao {
	
	/**
	 * 新增工资规则配置
	 */
	int save(SalaryRule salaryRule);
	
	/**
	 * 查询所有工资配置 带分页
	 * @return List<SalaryRule>
	 */
	List<SalaryRule> getSalaryRules(Map<String, Object> params);
	
	/**
	 * 获取总记录数
	 * @return
	 */
	int getTotalNum();

	/**
	 * 根据rid批量删除工资规则
	 * @return
	 */
	int deleteByRids(List<Long> rids);
	
	/**
	 * 根据rid修改工资规则
	 * @param salaryRule
	 * @return
	 */
	int updateSalaryRuleByRid(SalaryRule salaryRule);
	
	/**
	 * 根据公司id获取工资规则
	 * @param companyId
	 * @return
	 */
	SalaryRule getSalaryRuleByCompanyId(String companyId);

}
