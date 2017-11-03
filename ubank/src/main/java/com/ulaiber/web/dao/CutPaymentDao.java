package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ulaiber.web.model.attendance.CutPayment;

/** 
 * 扣款数据库接口
 *
 * @author  huangguoqing
 * @date 创建时间：2017年11月3日 上午10:27:53
 * @version 1.0 
 * @since 
 */
public interface CutPaymentDao {
	
	/**
	 * 插入
	 * @param cut
	 * @return
	 */
	int save(CutPayment cut);
	
	/**
	 * 批量插入
	 * @param list
	 * @return
	 */
	int batchSave(List<CutPayment> list);
	
	/**
	 * 根据用户id和月份查询扣款记录
	 * @return
	 */
	List<CutPayment> getCutPaymentByMonthAndUserId(Map<String ,Object> params);
	
	/**
	 * 批量删除
	 * @param list
	 * @return
	 */
	int batchDelete(@Param("month") String month, @Param("list") List<CutPayment> list);
	
	/**
	 * 分页搜索
	 * @param params
	 * @return
	 */
	List<CutPayment> getCutPaymentList(Map<String, Object> params);
	
	/**
	 * 搜索记录的总条数
	 * @param params
	 * @return
	 */
	int getTotalNum(Map<String, Object> params);
	
}
