package com.ulaiber.dao;

import java.util.List;
import java.util.Map;

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
	
	/**
	 * 分页模糊查询
	 * @return
	 */
	List<Salary> getSalaries(Map<String, Object> params); 
	
	/**
	 * 查询总条数
	 * @return
	 */
	int getTotalNum();
	
	/**
	 * 根据业务委托编号更新工资流水状态
	 * 
	 * @return
	 */
	boolean updateStatusBySeqNo(Salary sa);
}
