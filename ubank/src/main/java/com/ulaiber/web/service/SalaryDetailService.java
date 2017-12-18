package com.ulaiber.web.service;

import java.util.List;

import com.ulaiber.web.model.salary.SalaryDetail;

/**
 * 工资流水详情服务接口
 * 
 * @author huangguoqing
 *
 */
public interface SalaryDetailService {
	
	/**
	 * 批量插入
	 * @param details
	 * @return
	 */
	boolean saveBatch(List<SalaryDetail> details);
	
	/**
	 * 根据工资流水id查询流水详情,带分页
	 * @param sid
	 * @return
	 */
	List<SalaryDetail> getDetailsBySid(String sid, int limit, int offset, String orderby, String search);
	
	/**
	 * 根据工资流水id查询流水详情
	 * @param sid
	 * @return
	 */
	List<SalaryDetail> getDetailsBySid(String sid);
	
    /**
     * 根据工资流水id查询记录条数
     * @param sid
     * @return
     */
    int getTotalBySid(String sid);
	
	/**
	 * 根据id批量删除
	 * @param sids
	 * @return
	 */
	boolean batchDeleteSalaryDetails(List<String> sids);
	
    /**
     * 根据id和月份获取用户的工资详细
     * @param userId
     * @param month
     * @return
     */
    SalaryDetail getSalaryDetailByUserIdAndMonth(long userId, String month);
    
    /**
     * 获取最近一个月的工资表详细
     * @param companyId
     * @return
     */
    List<SalaryDetail> getLatestSalaryDetail(String companyId);
}
