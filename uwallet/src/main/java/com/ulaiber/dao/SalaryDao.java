package com.ulaiber.dao;

import java.util.List;

import com.ulaiber.model.Salary;

/**
 * 工资流水数据库接口
 * 
 * @author huangguoqing
 *
 */
public interface SalaryDao {
	
	/**
	 * 新增
	 * @param sa
	 * @return
	 */
	int save(Salary sa);
	
	/**
	 * 查询所有的工资流水
	 * @return
	 */
	List<Salary> getAllSalaries(); 

}
