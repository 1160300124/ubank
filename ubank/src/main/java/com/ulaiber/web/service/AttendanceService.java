package com.ulaiber.web.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ulaiber.web.model.Attendance;
import com.ulaiber.web.model.ResultInfo;

/** 
 * 考勤记业务逻辑接口
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
	boolean save(Attendance attend);
	
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
	ResultInfo refreshLocation(String longitude, String latitude, HttpServletRequest request);

}
