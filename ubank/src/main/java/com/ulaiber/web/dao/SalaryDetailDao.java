package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ulaiber.web.model.salary.SalaryDetail;

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
     * 批量更新状态
     * @return
     */
    int batchUpdateStatusBySid(List<SalaryDetail> details);

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
	List<SalaryDetail> getDetailsBySid(String sid);
    
    /**
     * 根据工资流水id查询记录条数
     * @param sid
     * @return
     */
    int getTotalBySid(String sid);

    /**
     * 根据id批量删除
     *
     * @param sids
     * @return
     */
    int batchDeleteSalaryDetails(List<String> sids);
    
    /**
     * 根据id和月份获取用户的工资详细
     * @param userId
     * @param month
     * @return
     */
    SalaryDetail getSalaryDetailByUserIdAndMonth(@Param("userId") long userId, @Param("salaryMonth") String month);
    
    /**
     * 获取用户最近的一条工资纤细
     * @param userId
     * @return
     */
    SalaryDetail getLatestDetailByUserId(long userId);
    
    /**
     * 获取最近一个月的工资表详细
     * @return
     */
    List<SalaryDetail> getLatestSalaryDetail(String companyId);

}