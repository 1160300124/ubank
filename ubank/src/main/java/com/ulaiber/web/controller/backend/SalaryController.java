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
import com.ulaiber.web.model.Salary;
import com.ulaiber.web.model.SalaryDetail;
import com.ulaiber.web.service.SalaryDetailService;
import com.ulaiber.web.service.SalaryService;

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
	public Map<String, Object> getSalaryDetails(int limit, int offset, String order, String search, HttpServletRequest request, HttpServletResponse response){
		logger.debug(",,,,,,");
		Map<String, Object> data = new HashMap<String, Object>();
		List<SalaryDetail> rules = detailService.getDetailsBySid(1);
		int total = detailService.getTotalBySid(1);
		data.put("rows", rules);
		data.put("total", total);
		return data;
	}
	
}
