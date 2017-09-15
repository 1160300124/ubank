package com.ulaiber.web.controller.api;

import java.util.Arrays;
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
import com.ulaiber.web.model.Holiday;
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
	
	@Autowired
	private AttendanceService service;
	
	@Autowired
	private AttendanceRuleService ruleService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "getRecordsForMonth", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getRecordsForMonth(String mobile, String month, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(month)){
			logger.error("手机号或月份不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("手机号或月份不能为空。");
			return info;
		}
		AttendanceRule rule = ruleService.getRuleByMobile(mobile);
		if (null == rule){
			logger.error("用户 {" + mobile + "}没有设置考勤规则，请先设置。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("用户 {" + mobile + "}没有设置考勤规则，请先设置。");
			return info;
		}

		Map<String, Object> data = service.getRecordsByMonthAndMobile(month, mobile, rule);
		info.setCode(IConstants.QT_CODE_OK);
		info.setMessage("用户 {" + mobile + "}获取考勤记录成功。");
		info.setData(data);
		return info;
	}
	
	@RequestMapping(value = "getClockInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getClockInfo(String mobile, String date,HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> data = new HashMap<String, Object>(); 
		if (StringUtils.isEmpty(mobile)){
			logger.error("手机号不能为空。");
			data.put("message", "手机号不能为空。");
			return data;
		}
		AttendanceRule rule = ruleService.getRuleByMobile(mobile);
		if (null == rule){
			logger.error("用户 {" + mobile + "}没有设置考勤规则，请先设置。");
			data.put("type", 5);
			data.put("message", "用户 {" + mobile + "}没有设置考勤规则，请先设置。");
			return data;
		}

		String datetime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MINUTETIME);
//		String datetime = "2017-09-13 00:20";
		String today = datetime.split(" ")[0];
		String time = datetime.split(" ")[1];
		
		if (StringUtils.equals(today, date)){
			date = "";
		}
		
		String dateBegin = today + " " + rule.getClockOnStartTime();
		String dateEnd = today + " " + rule.getClockOffEndTime();
		
		data.put("clockOnTime", rule.getClockOnTime());
		data.put("clockOffTime", rule.getClockOffTime());
		
		if (StringUtils.isNotEmpty(date)){
			today = date;
			dateBegin = today + " " + rule.getClockOnStartTime();
			dateEnd = today + " " + rule.getClockOffEndTime();
			if (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0){
				dateEnd = DateTimeUtil.getfutureTime(dateEnd, 1, 0, 0);
			}
		} else {
//			//if最晚打卡时间<最早打卡时间，说明最晚打卡时间为转钟后的凌晨时间，否则为当天时间
//			if (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0 && time.compareTo(rule.getClockOffEndTime()) <= 0){
//				//转钟后的前一天日期 yyyy-MM-dd 
//				today = DateTimeUtil.getfutureTime(datetime, -1, 0, 0).split(" ")[0];
//			}
			if (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0){
				if (time.compareTo(rule.getClockOffEndTime()) <= 0){
					today = DateTimeUtil.getfutureTime(datetime, -1, 0, 0).split(" ")[0];
					dateBegin = today + " " + rule.getClockOnStartTime();
				} else {
					dateEnd = DateTimeUtil.getfutureTime(dateEnd, 1, 0, 0);
				}
			}
		}
		
		data.put("clockDate", today);
		boolean isRestDay = false;
		//节假日
		Holiday holiday = ruleService.getHolidaysByYear(today.split("-")[0]);
		if (holiday != null){
			List<String> holidays = Arrays.asList(holiday.getHoliday().split(","));
			List<String> workdays = Arrays.asList(holiday.getWorkday().split(","));
			if (holidays.contains(today) && (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) > 0
					|| time.compareTo(rule.getClockOffEndTime()) > 0 && rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0)){
				isRestDay = true;
			} else {
				String workday = IConstants.WORK_DAY.get(DateTimeUtil.getWeekday(today) + "");
				if (rule.getWorkday().contains(workday) || workdays.contains(today)){
					isRestDay = false;
				} else {
					isRestDay = true;
				}
			}
		}
		
		if (isRestDay){
			data.put("isRestDay", isRestDay);
			return data;
		}
		
		//date不为空, 查询指定日期的打卡信息
		if (StringUtils.isNotEmpty(date)){
			List<Attendance> records = service.getRecordsByDateAndMobile(date, date, mobile, rule);
			data.put("records", records);
			return data;
		}
		
		String clockOffTime = today + " " + rule.getClockOffTime();
		//是否开启弹性时间
		if (rule.getFlexibleFlag() == 0){
			//下班是否顺延
			if (rule.getPostponeFlag() == 0){
				clockOffTime = DateTimeUtil.getfutureTime(clockOffTime, 0, 0, rule.getFlexibleTime()).split(" ")[1];
			}
		}
		
		//0上班卡  1下班卡  2更新 打卡  3不能打上班卡  4不能打下班卡  5不能打卡
		int type = 0;
		//按rid顺序查出考勤记录，<=2条记录
		List<Attendance> records = service.getRecordsByDateAndMobile(today, today, mobile, rule);
		if (null == records || records.size() == 0){
			//如果加班到凌晨打卡，当天是没有打卡记录的，需判断当前打卡时间是否在最晚下班打卡时间之内，如在则为昨天的下班卡，否则为今天的上班卡
			if (datetime.compareTo(clockOffTime) >= 0 && datetime.compareTo(dateEnd) <= 0){
				type = 1;
			}
		} else {
			if (records.size() == 1){
				if (StringUtils.equals(records.get(0).getClockType(), "0")){
					type = 1;
				} else if (StringUtils.equals(records.get(0).getClockType(), "1")){
					type = 2;
				}
			} else if (records.size() >= 2){
				type = 2;
			}
		}
		
		//当前时间<最早上班打卡时间&&最晚下班打卡时间>最早上班打卡时间 || 当前时间<最早上班打卡时间&&最晚下班打卡时间<最早上班打卡时间&&当前时间>最晚下班打卡时间
		if (datetime.compareTo(dateBegin) < 0){
//		if (time.compareTo(rule.getClockOnStartTime()) < 0 && (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) > 0
//				|| rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0 && time.compareTo(rule.getClockOffEndTime()) > 0)){
			type = 3;
		}
		//当前时间>最晚下班打卡时间&&最晚下班打卡时间>最早上班打卡时间 || 当前时间>最晚下班打卡时间&&最晚下班打卡时间<最早上班打卡时间&&当前时间<最早上班打卡时间
		if (datetime.compareTo(dateEnd) > 0){
//		else if (time.compareTo(rule.getClockOffEndTime()) > 0 && (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) > 0) 
//				|| rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0 && time.compareTo(rule.getClockOnStartTime()) < 0){ 
			type = 4;
		}
		
		data.put("type", type);
		data.put("records", records);
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
		
		AttendanceRule rule = ruleService.getRuleByMobile(mobile);
		if (null == rule){
			logger.error("用户 {" + mobile + "}没有设置考勤规则，请先设置。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("用户  " + mobile + " 没有设置考勤规则，请先设置。");
			return info;
		}
		
		//检查打卡的位置是否在设置范围内
		ResultInfo result = service.refreshLocation(mobile, longitude, latitude, rule);
		if (result.getCode() == IConstants.QT_CODE_ERROR){
			logger.error("不在设置的打卡范围之内，请刷新位置。");
			return result;
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
		com.setCompanyNumber(user.getCompanyId());
		att.setCompany(com);
		
		Departments dept = new Departments();
		dept.setDept_number(user.getDept_number());
		att.setDept(dept);
		
		att.setClockDevice(device);
		att.setClockLocation(location);
		
		info = service.save(att, rule);
		if (info.getCode() == IConstants.QT_CODE_OK){
			logger.info("用户  " + mobile + " 打卡成功。");
			info.setMessage("用户  " + mobile + " 打卡成功。");
		} 
		
		return info;
	}

	@RequestMapping(value = "refreshLocation", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo refreshLocation(String mobile, String longitude, String latitude, HttpServletRequest request, HttpServletResponse response){
		logger.debug("refreshLocation start...");
		ResultInfo info = new ResultInfo();
		if (StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)){
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("经度或纬度不能为空。");
			return info;
		}
		
		AttendanceRule rule = ruleService.getRuleByMobile(mobile);
		if (null == rule){
			logger.error("用户 {" + mobile + "}没有设置考勤规则，请先设置。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("用户  " + mobile + " 没有设置考勤规则，请先设置。");
			return info;
		}
		
		info = service.refreshLocation(mobile, longitude, latitude, rule);
		logger.debug("refreshLocation end...");
		return info;
	}
	
}
