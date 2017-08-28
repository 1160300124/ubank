package com.ulaiber.web.controller.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.Attendance;
import com.ulaiber.web.service.AttendanceService;

/** 
 * 考勤记录后台控制器
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月9日 下午5:23:15
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/backend/")
public class AttendanceController extends BaseController {
	
	private static Logger logger = Logger.getLogger(AttendanceController.class);
	
	/**
	 * 打卡类型 0：签到  1：签退
	 */
	private static Map<String, String> CLOCK_TYPE = new HashMap<String, String>(); 
	
	/**
	 * 打卡状态 0：正常  1：迟到  2：早退
	 */
	private static Map<String, String> CLOCK_STATUS = new HashMap<String, String>(); 
	
	static {
		CLOCK_TYPE.put("0", "签到");
		CLOCK_TYPE.put("1", "签退");
		CLOCK_STATUS.put("0", "正常 ");
		CLOCK_STATUS.put("1", "迟到 ");
		CLOCK_STATUS.put("2", "早退 ");
	}
	
	@Autowired
	private AttendanceService service;
	
	@RequestMapping(value = "attendance", method = RequestMethod.GET)
	public String attendance(HttpServletRequest request, HttpServletResponse response){
		return "attendance";
	}
	
	@RequestMapping(value = "getRecords", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getRecords(Attendance att, String startDate, String endDate, int limit, int offset, String order, 
			HttpServletRequest request, HttpServletResponse response){
		logger.info("getRecords start...");
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limit", limit);
		params.put("offset", offset);
		params.put("order", order);
		params.put("company_num", att.getCompany().getCompanyNumber());
		params.put("dept_num", att.getDept().getDept_number());
		params.put("clock_type", att.getClockType());
		params.put("clock_status", att.getClockStatus());
		params.put("user_name", att.getUserName());
		params.put("start_date", startDate);
		params.put("end_date", endDate);
		
		int total = service.getCountBycond(params);
		List<Attendance> list = service.getRecordsByCond(params);
		for (Attendance attend : list){
			attend.setClockType(CLOCK_TYPE.get(attend.getClockType()));
			attend.setClockStatus(CLOCK_STATUS.get(attend.getClockStatus()));
		}
		data.put("total", total);
		data.put("rows", list);
		
		logger.info("getRecords end...");
		return data;
	}

}
