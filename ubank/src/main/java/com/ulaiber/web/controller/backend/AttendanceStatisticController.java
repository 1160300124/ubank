package com.ulaiber.web.controller.backend;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.attendance.AttendanceStatistic;
import com.ulaiber.web.service.AttendanceStatisticService;
import com.ulaiber.web.utils.DateTimeUtil;
import com.ulaiber.web.utils.ExportExcel;

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
		if (DateTimeUtil.getNumFromdate(startDate, endDate) > 31){
			return data;
		}
		
		int total = service.getCountBycond(params);
		List<AttendanceStatistic> list = service.getStatisticsByCond(params);
		data.put("total", total);
		data.put("rows", list);
		
		logger.info("getStatistics end...");
		return data;
	}
	
	@RequestMapping(value = "exprotStatistics", method = RequestMethod.GET)
	public void exprotStatistics(String companyNumber, String deptNumber, String userName, String startDate, String endDate, 
			HttpServletRequest request, HttpServletResponse response){
		logger.info("exprotStatistics start...");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("company_num", companyNumber);
		params.put("dept_num", deptNumber);
		params.put("user_name", userName);
		params.put("start_date", startDate);
		params.put("end_date", endDate);
		if (DateTimeUtil.getNumFromdate(startDate, endDate) > 31){
			return;
		}
		
		OutputStream out = null;
		try {
			List<List<String>> list = new ArrayList<List<String>>();
			List<AttendanceStatistic> statistics = service.getStatisticsByCond(params);
			for (AttendanceStatistic sta : statistics){
				List<String> staList = new ArrayList<String>();
				staList.add(sta.getUserName());
				staList.add(sta.getCompany().getName());
				staList.add(sta.getDept().getDeptName());
				staList.add(sta.getWorkdaysCount() + "");
				staList.add(sta.getNormalClockOnCount() + "");
				staList.add(sta.getLaterCount() + "");
				staList.add(sta.getNoClockOnCount() + "");
				staList.add(sta.getNormalClockOffCount() + "");
				staList.add(sta.getLeaveEarlyCount() + "");
				staList.add(sta.getNoClockOffCount() + "");
				staList.add(StringUtils.isEmpty(sta.getRemark()) ? "" : sta.getRemark());
				list.add(staList);
			}

			ExportExcel exportExcel = new ExportExcel();
			String title = "考勤记录";
			String[] headers = {"姓名","公司","部门","应出勤天数","正常上班打卡(次)","迟到(次)","上班未打卡(次)","正常下班打卡(次)","早退(次)","下班未打卡(次)","备注"};
			out = new FileOutputStream("/var/text001.xls");
			exportExcel.exportExcel(title, headers, list, out);
			out.close();
			exportExcel.download("/var/text001.xls", response);
		} catch (IOException e) {
			logger.error("exprotStatistics exception: ", e);
		} finally {
			try {
				if (null != out){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("exprotStatistics end...");
	}
	
}
