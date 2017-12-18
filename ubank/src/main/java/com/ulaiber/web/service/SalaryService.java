package com.ulaiber.web.service;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.salary.Salary;
import com.ulaiber.web.model.salary.SalaryDetail;

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
	 * @return boolean
	 */
	boolean save(Salary salary);
	
	/**
	 * 修改
	 * @param salary
	 * @return boolean
	 */
	boolean update(Salary salary);
	
	/**
	 * 查询所有的工资流水
	 * @return List<Salary> 
	 */
	List<Salary> getAllSalaries(); 
	
	/**
	 * 分页模糊查询
	 * @return List<Salary> 
	 */
	List<Salary> getSalaries(int limit, int offset, String search); 
	
	/**
	 * 查询总条数
	 * @return int
	 */
	int getTotalNum();
	
	/**
	 * 根据业务委托编号更新工资流水状态
	 * 
	 * @return boolean
	 */
	boolean updateStatusBySeqNo(Salary sa);
	
	/**
	 * 根据sids批量删除
	 * @param sids
	 * @return boolean
	 */
	boolean batchDeleteSalaries(List<String> sids);
	
    /**
     * 获取用户的工资流水
     * @param userId
     * @param pageSize
     * @param pageNum
     * @return List<Map<String, Object>>
     */
	List<Map<String, Object>> getSalariesByUserId(long userId, int pageSize, int pageNum);
	
	/**
	 * 导入用户信息
	 * @param companyNum
	 * @param salaryMonth
	 * @return List<SalaryDetail>
	 */
	List<SalaryDetail> importUserInfo(String companyNum,String salaryMonth);
	
	/**
	 * 代发工资
	 * @param salaryId 工资流水id
	 * @return ResultInfo ResultInfo
	 */
	ResultInfo pay(String salaryId);
	
	/**
	 * 更新状态
	 * @return boolean
	 */
	boolean updateStatusBySid(Salary salary);
}
