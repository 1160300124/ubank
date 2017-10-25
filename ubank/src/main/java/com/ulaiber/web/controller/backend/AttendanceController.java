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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.Company;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.User;
import com.ulaiber.web.model.attendance.Attendance;
import com.ulaiber.web.service.AttendanceService;
import com.ulaiber.web.service.PermissionService;
import com.ulaiber.web.utils.DateTimeUtil;
import com.ulaiber.web.utils.ExportExcel;
import com.ulaiber.web.utils.ObjUtil;

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
	
	@Autowired
	private AttendanceService service;
	
	@Autowired
	private PermissionService permissionService;
	
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
		params.put("clock_on_status", att.getClockOnStatus());
		params.put("clock_off_status", att.getClockOffStatus());
		params.put("user_name", att.getUserName());
		params.put("start_date", startDate);
		params.put("end_date", endDate);
		if (DateTimeUtil.getNumFromdate(startDate, endDate) > 31){
			return data;
		}
		
		int total = service.getCountBycond(params);
		List<Attendance> list = service.getRecordsByCond(params);
		data.put("total", total);
		data.put("rows", list);
		
		logger.info("getRecords end...");
		return data;
	}
	
	@RequestMapping(value = "deleteRecords", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo deleteRecords(@RequestBody List<Long> rids, HttpServletRequest request, HttpServletResponse response){
		
		logger.debug("deleteRecords start...");
		ResultInfo info = new ResultInfo();
		if (!ObjUtil.notEmpty(rids)){
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("参数为空！");
			return info;
		}
		boolean flag = service.deleteRecordsByRids(rids);
		 if (flag){
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("删除成功");;
		} else {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("删除失败");
		}
		 logger.debug("deleteRecords end...");
		return info;
	}

	@RequestMapping(value = "getCompanys", method = RequestMethod.GET)
	@ResponseBody
	public List<Company> getCompanys(HttpServletRequest request, HttpServletResponse response){
		logger.debug("getCompanys start...");
		User user = getUserFromSession(request);
		String[] companyNums = user.getCompanyNumber().split(",");
		List<Company> companys = permissionService.getCompanysByNums(companyNums);
		logger.debug("getCompanys end...");
		return companys;
	}
	
	@RequestMapping(value = "exportRecords", method = RequestMethod.GET)
	public void exportRecords(String companyNumber, String deptNumber, String clockOnStatus, String clockOffStatus, String userName, String startDate, String endDate,
			HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("company_num", companyNumber);
		params.put("dept_num", deptNumber);
		params.put("clock_on_status", clockOnStatus);
		params.put("clock_off_status", clockOffStatus);
		params.put("user_name", userName);
		params.put("start_date", startDate);
		params.put("end_date", endDate);
		if (DateTimeUtil.getNumFromdate(startDate, endDate) > 31){
			return;
		}
		
		OutputStream out = null;
		try {
			List<List<String>> list = new ArrayList<List<String>>();
			List<Attendance> records = service.getRecordsByCond(params);
			for (Attendance attd : records){
				List<String> recordList = new ArrayList<String>();
				recordList.add(attd.getUserName());
				recordList.add(attd.getCompany().getName());
				recordList.add(attd.getDept().getDeptName());
				recordList.add(attd.getClockDate());
				recordList.add(StringUtils.isEmpty(attd.getClockOnDateTime()) ? "" : attd.getClockOnDateTime());
				recordList.add(StringUtils.isEmpty(attd.getClockOnStatus()) ? "" : StringUtils.equals(attd.getClockOnStatus(), "0") ? "正常" : "迟到");
				recordList.add(StringUtils.isEmpty(attd.getClockOnLocation()) ? "" : attd.getClockOnLocation());
				recordList.add(StringUtils.isEmpty(attd.getClockOnDevice()) ? "" : attd.getClockOnDevice());
				recordList.add(StringUtils.isEmpty(attd.getClockOffDateTime()) ? "" : attd.getClockOffDateTime());
				recordList.add(StringUtils.isEmpty(attd.getClockOffStatus()) ? "" : StringUtils.equals(attd.getClockOffStatus(), "0") ? "正常" : "早退");
				recordList.add(StringUtils.isEmpty(attd.getClockOffLocation()) ? "" : attd.getClockOffLocation());
				recordList.add(StringUtils.isEmpty(attd.getClockOffDevice()) ? "" : attd.getClockOffDevice());
				list.add(recordList);
			}
			ExportExcel exportExcel = new ExportExcel();
			String title = "考勤记录";
			String[] headers = {"姓名","公司","部门","打卡日期","上班时间","上班状态","上班打卡位置","上班打卡设备号","下班时间","下班状态","下班打卡位置","下班打卡设备号"};
			out = new FileOutputStream("/var/text.xls");
			exportExcel.exportExcel(title, headers, list, out);
			out.close();
			exportExcel.download("/var/text.xls", response);
		} catch (IOException e) {
			logger.error("exportRecords exception: ", e);
		} finally {
			try {
				if (null != out){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
