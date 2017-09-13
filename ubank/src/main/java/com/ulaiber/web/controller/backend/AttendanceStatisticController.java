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
import com.ulaiber.web.model.AttendanceStatistic;
import com.ulaiber.web.service.AttendanceStatisticService;

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
	
	@Autowired
	private AttendanceStatisticService service;
	
	@RequestMapping(value = "statistics", method = RequestMethod.GET)
	public String statistic(HttpServletRequest request, HttpServletResponse response){
		return "attendanceStatistic";
	}
	
	@RequestMapping(value = "getStatistics", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getStatistics(AttendanceStatistic statistic, String startDate, String endDate, int limit, int offset, String order, 
			HttpServletRequest request, HttpServletResponse response){
		logger.info("getStatistics start...");
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limit", limit);
		params.put("offset", offset);
		params.put("order", order);
		params.put("company_num", statistic.getCompany().getCompanyNumber());
		params.put("dept_num", statistic.getDept().getDept_number());
		params.put("user_name", statistic.getUserName());
		params.put("start_date", startDate);
		params.put("end_date", endDate);
		
		int total = service.getCountBycond(params);
		List<AttendanceStatistic> list = service.getStatisticsByCond(params);
		data.put("total", total);
		data.put("rows", list);
		
		logger.info("getStatistics end...");
		return data;
	}
	
}
