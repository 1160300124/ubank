package com.ulaiber.web.controller.backend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ulaiber.web.controller.BaseController;

/** 
 * 考勤统计控制器
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月10日 上午10:46:22
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/backend/")
public class AttendanceStatisticController extends BaseController {

	private static Logger logger = Logger.getLogger(AttendanceStatisticController.class);
	
	@RequestMapping(value = "statistics", method = RequestMethod.GET)
	public String statistic(HttpServletRequest request, HttpServletResponse response){
		logger.debug("----------------------------");
		return "attendance_statistics";
	}
}
