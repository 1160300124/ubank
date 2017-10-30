package com.ulaiber.web.service;

import java.util.List;

import com.ulaiber.web.model.SalaryDetail;

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
	 * 根据工资流水id查询流水详情
	 * @param sid
	 * @return
	 */
	List<SalaryDetail> getDetailsBySid(long sid, int limit, int offset, String orderby, String search);
	
    /**
     * 根据工资流水id查询记录条数
     * @param sid
     * @return
     */
    int getTotalBySid(long sid);
	
	/**
	 * 根据id批量删除
	 * @param sids
	 * @return
	 */
	boolean batchDeleteSalaryDetails(List<Long> sids);
	
    /**
     * 根据id和月份获取用户的工资详细
     * @param userId
     * @param month
     * @return
     */
    SalaryDetail getSalaryDetailByUserIdAndMonth(long userId, String month);
    
    /**
     * 获取最近一个月的工资表详细
     * @return
     */
    List<SalaryDetail> getLatestSalaryDetail();
}
