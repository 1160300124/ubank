package com.ulaiber.web.service;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.attendance.CutPayment;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年11月1日 上午11:51:28
 * @version 1.0 
 * @since 
 */
public interface CutPaymentService {

	/**
	 * 获取扣款详情和扣款总金额
	 * @param companyId
	 * @param salaryMonth
	 * @return
	 */
	Map<String, Object> getCutPaymentMessage(String companyId, String salaryMonth);
	
	/**
	 * 插入
	 * @param cut
	 * @return
	 */
	boolean save(CutPayment cut);
	
	/**
	 * 批量插入
	 * @param list
	 * @return
	 */
	boolean batchSave(List<CutPayment> list);
	
	/**
	 * 根据用户id和月份查询扣款记录
	 * @return
	 */
	List<CutPayment> getCutPaymentByMonthAndUserId(long userId, String month);
	
	/**
	 * 批量删除
	 * @param list
	 * @return
	 */
	boolean batchDelete(String month, List<CutPayment> list);
	
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
