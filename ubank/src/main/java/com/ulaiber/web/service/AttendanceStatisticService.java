package com.ulaiber.web.service;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.AttendanceStatistic;

/** 
 * 考勤统计业务逻辑接口
 *
 * @author  huangguoqing
 * @date 创建时间：2017年9月7日 上午11:04:37
 * @version 1.0 
 * @since 
 */
public interface AttendanceStatisticService {
	
	/**
	 * 新增考勤统计记录
	 * @param attend
	 * @return
	 */
	boolean save(AttendanceStatistic statistic);
	
	/**
	 * 更新考勤统计记录
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
	 * @param params
	 * @return
	 */
	int getCountBycond(Map<String, Object> params);
	
	/**
	 * 获取指定月份的应工作天数  yyyy-MM
	 * @return
	 */
	int getWorkdayCountForMonth(long userId, String month);
	
}
