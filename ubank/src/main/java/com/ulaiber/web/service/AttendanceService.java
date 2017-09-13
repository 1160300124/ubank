package com.ulaiber.web.service;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.Attendance;
import com.ulaiber.web.model.AttendanceRule;
import com.ulaiber.web.model.ResultInfo;

/** 
 * 考勤记录业务逻辑接口
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月11日 上午11:07:03
 * @version 1.0 
 * @since 
 */
public interface AttendanceService {
	
	/**
	 * 新增考勤记录
	 * @param attend
	 * @return
	 */
	ResultInfo save(Attendance attend, AttendanceRule rule);
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	List<Attendance> getRecordsByCond(Map<String, Object> params);
	
	/**
	 * 刷新地理位置坐标
	 * @return
	 */
	ResultInfo refreshLocation(String mobile, String longitude, String latitude, AttendanceRule rule);
	
	/**
	 * 根据条件获取记录数
	 * @param params
	 * @return
	 */
	int getCountBycond(Map<String, Object> params);
	
	/**
	 * 根据用户id获取考勤记录
	 * @param params
	 * @return
	 */
	List<Attendance> getRecordsByDateAndUserId(Map<String, Object> params);
	
	/**
	 * 根据date和手机号查询考勤记录
	 * @param params
	 * @return
	 */
	List<Attendance> getRecordsByDateAndMobile(String dateBegin, String dateEnd, String mobile, AttendanceRule rule);
	
	/**
	 * 根据userId获取用户的最近一次打卡记录
	 * @param userId
	 * @return
	 */
	Attendance getLatestRecordByUserId(long userId);

	/**
	 * 更新打卡记录
	 * @param record
	 * @return
	 */
	boolean updateClockOffInfo(Attendance record);
	
	/**
	 * 批量删除记录
	 * @param rids
	 * @return
	 */
	boolean deleteRecordsByRids(List<Long> rids);
	
	/**
	 * 根据月份查询考勤记录
	 * @param month
	 */
	Map<String, Object> getRecordsByMonthAndMobile(String month, String mobile, AttendanceRule rule);
}
