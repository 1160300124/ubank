package com.ulaiber.web.service;

import java.util.List;

import com.ulaiber.web.model.Salary;

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
	
	/**
	 * 分页模糊查询
	 * @return
	 */
	List<Salary> getSalaries(int limit, int offset, String search); 
	
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
	
	/**
	 * 根据sids批量删除
	 * @param sids
	 * @return
	 */
	boolean batchDeleteSalaries(List<Long> sids);

}
