package com.ulaiber.dao;

import java.util.List;

import com.ulaiber.model.SalaryDetail;

/**
 * 工资流水详细数据库接口
 * 
 * @author huangguoqing
 *
 */
public interface SalaryDetailDao { 
	
	/**
	 * 批量插入
	 * @param details
	 * @return
	 */
	int saveBatch(List<SalaryDetail> details);
	
	/**
	 * 根据工资流水id查询流水详情
	 * @param sid
	 * @return
	 */
	List<SalaryDetail> getDetailsBySid(long sid);
	
}