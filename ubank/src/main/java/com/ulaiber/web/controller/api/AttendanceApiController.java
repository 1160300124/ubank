package com.ulaiber.web.controller.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.Attendance;
import com.ulaiber.web.model.AttendanceRule;
import com.ulaiber.web.model.Company;
import com.ulaiber.web.model.Departments;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.AttendanceRuleService;
import com.ulaiber.web.service.AttendanceService;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.DateTimeUtil;

/** 
 * 考勤记录api控制器
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月14日 下午12:23:46
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/api/v1/")
public class AttendanceApiController extends BaseController {
	
	private static Logger logger = Logger.getLogger(AttendanceApiController.class);
	
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
	
	@Autowired
	private AttendanceRuleService ruleService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "getClockInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getClockInfo(String mobile, HttpServletRequest request, HttpServletResponse response){
		AttendanceRule rule = ruleService.getRuleByMobile(mobile);
		Map<String, Object> data = new HashMap<String, Object>(); 
		data.put("isRestDay", false);
		data.put("clockOnTime", rule.getClockOnTime());
		data.put("clockOffTime", rule.getClockOffTime());
		
//		String datetime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_DAYTIME) + " " + rule.getClockOffTime();
//		//最晚打卡时间大于打卡时间为当天打卡，否则为次日凌晨打卡
//		if (rule.getClockOffEndTime().compareTo(rule.getClockOffTime()) > 0){
//			String day = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_DAYTIME);
//			List<Attendance> records = service.getRecordsByDateAndMobile(day, mobile);
//		} else {
//			
//			//获取当天最晚打卡时间
//			String clockOffEndTime = DateTimeUtil.getfutureTime(datetime, 0, rule.getClockOffDelayHours(), 0);
//		}
		String day = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_DAYTIME);
		List<Attendance> list = new ArrayList<Attendance>();
		List<Attendance> records = service.getRecordsByDateAndMobile(day, mobile);
		for (Attendance record : records){
			Attendance att = new Attendance();
			att.setRid(record.getRid());
			att.setUserId(record.getUserId());
			att.setUserName(record.getUserName());
			att.setClockDate(record.getClockDate());
			att.setClockTime(record.getClockTime());
			att.setClockType(record.getClockType());
			att.setClockStatus(record.getClockStatus());
			att.setClockLocation(record.getClockLocation());
			att.setClockDevice(record.getClockDevice());
			list.add(att);
		}
		data.put("records", list);
		return data;
	}
	
	@RequestMapping(value = "clock", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo clock(String mobile, String longitude, String latitude, String device, String location,
			HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)){
			logger.error("手机号或经纬度不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("手机号或经纬度不能为空。");
			return info;
		}
		
		//检查打卡的位置是否在设置范围内
		ResultInfo result = service.refreshLocation(longitude, latitude, request);
		if (result.getCode() == IConstants.QT_CODE_ERROR){
			logger.error("不在设置的打卡范围之内，请刷新位置。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("不在设置的打卡范围之内，请刷新位置。");
			return info;
		}
		User user = userService.findByMobile(mobile);
		if (null == user){
			logger.error("用户  " + mobile + " 不存在，可能此手机号还没注册。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("用户  " + mobile + " 不存在，可能此手机号还没注册。");
			return info;
		}
		Attendance att = new Attendance();
		att.setUserId(user.getId());
		att.setUserName(user.getUserName());
		
		Company com = new Company();
		com.setCompanyNumber(Integer.parseInt(user.getCompanyNumber()));
		att.setCompany(com);
		
		Departments dept = new Departments();
		dept.setDept_number(user.getDept_number());
		att.setDept(dept);
		
		att.setClockDevice(device);
		att.setClockLocation(location);
		
		info = service.save(att);
		if (info.getCode() == IConstants.QT_CODE_OK){
			logger.info("用户  " + mobile + " 打卡成功。");
			info.setMessage("用户  " + mobile + " 打卡成功。");
		} 
		
		return info;
	}

	@RequestMapping(value = "refreshLocation", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo refreshLocation(String longitude, String latitude, HttpServletRequest request, HttpServletResponse response){
		logger.debug("refreshLocation start...");
		ResultInfo info = new ResultInfo();
		if (StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)){
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("经度或纬度不能为空。");
			return info;
		}
		info = service.refreshLocation(longitude, latitude, request);
		logger.debug("refreshLocation end...");
		return info;
	}
	
}
