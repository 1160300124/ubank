package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.Attendance;

/** 
 * 考勤记录数据接口
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月11日 上午10:33:21
 * @version 1.0 
 * @since 
 */
public interface AttendanceDao {
	
	/**
	 * 新增考勤记录
	 * @param attend
	 * @return
	 */
	int save(Attendance attend);
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	List<Attendance> getRecordsByCond(Map<String, Object> params);
	
	/**
	 * 根据条件获取记录数
	 */
	int getCountBycond(Map<String, Object> params);
	
	/**
	 * 根据用户id获取考勤记录
	 * @param userId
	 * @return
	 */
	Attendance getRecordByCond(Attendance attend);
	
	/**
	 * 根据date和手机号查询考勤记录
	 * @param params
	 * @return
	 */
	List<Attendance> getRecordsByDateAndMobile(Map<String, Object> params);
	
	/**
	 * 根据手机号获取用户的最近一次打卡记录
	 * @param userId
	 * @return
	 */
	Attendance getLatestRecordByUserId(long userId);

}
