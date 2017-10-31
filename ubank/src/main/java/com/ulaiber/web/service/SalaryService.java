package com.ulaiber.web.service;

import java.util.List;
import java.util.Map;

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
	boolean save(Salary salary);
	
	/**
	 * 修改
	 * @param salary
	 * @return
	 */
	boolean update(Salary salary);
	
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
	
    /**
     * 获取用户的工资流水
     * @param userId
     * @return
     */
	List<Map<String, Object>> getSalariesByUserId(long userId, int pageSize, int pageNum);
}
