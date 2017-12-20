package com.ulaiber.web.service;

import java.util.List;

import com.ulaiber.web.model.salary.SalaryChange;

/** 
 * 工资调整业务逻辑接口
 *
 * @author  huangguoqing
 * @date 创建时间：2017年12月20日 上午11:22:43
 * @version 1.0 
 * @since 
 */
public interface SalaryChangeService {
	
	/**
	 * 保存工资调整记录
	 * @param salary
	 * @return
	 */
	boolean save(SalaryChange salary);

	/**
	 * 查询用户的工资调整记录
	 * @param userId
	 * @return
	 */
	List<SalaryChange> getSalaryChangeByUserId(long userId);
}
