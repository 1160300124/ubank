package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.attendance.AttendanceStatistic;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年9月7日 下午3:09:40
 * @version 1.0 
 * @since 
 */
public interface AttendanceStatisticDao {
	
	/**
	 * 新增考勤记录
	 * @param attend
	 * @return
	 */
	int save(AttendanceStatistic statistic);
	
	/**
	 * 更新考勤记录
	 * @param attend
	 * @return
	 */
	boolean update(AttendanceStatistic statistic);
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	List<AttendanceStatistic> getStatisticsByCond(Map<String, Object> params);
	
	/**
	 * 根据条件获取记录数
	 */
	int getCountBycond(Map<String, Object> params);

}
