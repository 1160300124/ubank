package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.SalaryDetail;

/**
 * 工资流水详细数据库接口
 *
 * @author huangguoqing
 */
public interface SalaryDetailDao {

    /**
     * 批量插入
     *
     * @param details
     * @return
     */
    int batchSave(List<SalaryDetail> details);
    
    /**
     * 批量更新
     * @param details
     * @return
     */
    int batchUpdate(List<SalaryDetail> details);

    /**
     * 根据工资流水id查询流水详情
     *
     * @param params Map<String, Object> 
     * @return
     */
    List<SalaryDetail> getDetailsBySid2(Map<String, Object> params);
    
	/**
	 * 根据工资流水id查询流水详情
	 * @param sid
	 * @return
	 */
	List<SalaryDetail> getDetailsBySid(long sid);
    
    /**
     * 根据工资流水id查询记录条数
     * @param sid
     * @return
     */
    int getTotalBySid(long sid);

    /**
     * 根据id批量删除
     *
     * @param sids
     * @return
     */
    int batchDeleteSalaryDetails(List<Long> sids);
    
    /**
     * 根据id和月份获取用户的工资详细
     * @param userId
     * @return
     */
    SalaryDetail getSalaryDetailByUserIdAndMonth(Map<String, Object> params);
    
    /**
     * 获取最近一个月的工资表详细
     * @return
     */
    List<SalaryDetail> getLatestSalaryDetail(String companyId);

}