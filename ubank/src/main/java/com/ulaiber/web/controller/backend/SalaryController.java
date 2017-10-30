package com.ulaiber.web.controller.backend;

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
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.Salary;
import com.ulaiber.web.model.SalaryDetail;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.SalaryDetailService;
import com.ulaiber.web.service.SalaryService;
import com.ulaiber.web.service.UserService;

/** 
 * 工资控制位
 *
 * @author  huangguoqing
 * @date 创建时间：2017年10月25日 下午5:47:09
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/backend/")
public class SalaryController extends BaseController {
	
	private static Logger logger = Logger.getLogger(SalaryController.class);
	
	@Autowired
	private SalaryService service;
	
	@Autowired
	private SalaryDetailService detailService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "salary", method = RequestMethod.GET)
	public String salary(HttpServletRequest request, HttpServletResponse response){
		return "salary";
	}
	
	@RequestMapping(value = "salaryConfig", method = RequestMethod.GET)
	public String salaryConfig(HttpServletRequest request, HttpServletResponse response){
		return "salaryConfig";
	}
	
	@RequestMapping(value = "salaryDetail", method = RequestMethod.GET)
	public String salaryDetail(HttpServletRequest request, HttpServletResponse response){
		return "salaryDetail";
	}
	
	@RequestMapping(value = "getSalaries", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSalaries(int limit, int offset, String order, String search, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> data = new HashMap<String, Object>();
		List<Salary> rules = service.getSalaries(limit, offset, search);
		int total = service.getTotalNum();
		data.put("rows", rules);
		data.put("total", total);
		return data;
	}
	
	@RequestMapping(value = "getSalaryDetails", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSalaryDetails(Long sid, int limit, int offset, String order, String search, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> data = new HashMap<String, Object>();
		if (sid == null){
			return data;
		}
		List<SalaryDetail> rules = detailService.getDetailsBySid(sid, limit, offset, order, search);
		int total = detailService.getTotalBySid(sid);
		data.put("rows", rules);
		data.put("total", total);
		return data;
	}
	
	@RequestMapping(value = "saveSalary", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo saveSalary(@RequestBody Salary salary, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		try {
			User user = getUserFromSession(request);
			salary.setOperateName(user.getUserName());
			boolean flag = service.save(salary);
			if (flag){
				logger.info("保存成功。");
				info.setCode(IConstants.QT_CODE_OK);
				info.setMessage("保存成功。");
			} else {
				logger.info("保存失败。");
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("保存失败。");
			}
		} catch (Exception e) {
			logger.info("保存失败。" , e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("系统内部异常。");
		}
		 
		return info;
	}
	
	@RequestMapping(value = "importUserInfo", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo importUserInfo(String comNum, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if(StringUtils.isEmpty(comNum)){
			logger.info("公司不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("请选择公司。");
			return info;
		}
		
		try {
			List<User> users = userService.getUsersByComNum(comNum, "");
			info.setCode(IConstants.QT_CODE_OK);
			info.setData(users);
		} catch (Exception e) {
			logger.info("获取用户信息失败。" , e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("系统内部异常。");
		}
		 
		return info;
	}
	
	@RequestMapping(value = "importLatestSalary", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo importLatestSalary(String comNum, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if(StringUtils.isEmpty(comNum)){
			logger.info("公司不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("请选择公司。");
			return info;
		}
		
		try {
			List<SalaryDetail> details = detailService.getLatestSalaryDetail();
			info.setCode(IConstants.QT_CODE_OK);
			info.setData(details);
		} catch (Exception e) {
			logger.info("获取最近失败工资列表失败。" , e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("系统内部异常。");
		}
		 
		return info;
	}
	
}
