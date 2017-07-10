package com.ulaiber.service;

import java.util.List;

import com.ulaiber.model.Salary;

/**
 * 工资流水服务接口
 * 
 * @author huangguoqing
 *
 */
public interface SalaryService {
	
	/**
	 * 新增
	 * @param sa
	 * @return
	 */
	boolean save(Salary sa);
	
	/**
	 * 查询所有的工资流水
	 * @return
	 */
	List<Salary> getAllSalaries(); 

}
