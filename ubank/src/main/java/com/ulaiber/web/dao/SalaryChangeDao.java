package com.ulaiber.web.dao;

import java.util.List;

import com.ulaiber.web.model.salary.SalaryChange;

/** 
 * 工资调整数据库接口
 *
 * @author  huangguoqing
 * @date 创建时间：2017年12月20日 上午11:14:14
 * @version 1.0 
 * @since 
 */
public interface SalaryChangeDao {
	
	/**
	 * 保存工资调整记录
	 * @param change
	 * @return
	 */
	int save(SalaryChange change);

	/**
	 * 查询用户的工资调整记录
	 * @param userId
	 * @return
	 */
	List<SalaryChange> getSalaryChangeByUserId(long userId);
}
