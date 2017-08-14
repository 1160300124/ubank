package com.ulaiber.web.controller.api;

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
import com.ulaiber.web.model.Company;
import com.ulaiber.web.model.Departments;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.AttendanceService;
import com.ulaiber.web.service.UserService;

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
	private UserService userService;
	
	@RequestMapping(value = "clock", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo clock(String mobile, String longitude, String latitude, String type, String device, String location,
			HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude) || StringUtils.isEmpty(type)
				|| StringUtils.isEmpty(device) || StringUtils.isEmpty(location)){
			logger.error("参数不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("参数不能为空。");
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
			logger.error("用户 " + mobile + " 不存在，可能此手机号还没注册。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("用户" + mobile + " 不存在，可能此手机号还没注册。");
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
		
		att.setClockType(type);
		att.setClockDevice(device);
		att.setClockLocation(location);
		
		boolean flag = service.save(att);
		if (flag){
			logger.info("手机号 " + mobile + " 打卡成功。");
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("手机号 " + mobile + " 打卡成功。");
		} else {
			logger.error("手机号 " + mobile + " 打卡失败，系统内部错误。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("手机号 " + mobile + " 打卡失败，系统内部错误。");
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
