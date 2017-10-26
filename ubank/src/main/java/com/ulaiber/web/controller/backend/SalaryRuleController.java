package com.ulaiber.web.controller.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.ulaiber.web.model.User;
import com.ulaiber.web.model.attendance.SalaryRule;
import com.ulaiber.web.service.SalaryRuleService;

/** 
 * 工资后台控制器
 *
 * @author  huangguoqing
 * @date 创建时间：2017年10月9日 上午10:41:32
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/backend/")
public class SalaryRuleController extends BaseController {
	
	private static Logger logger = Logger.getLogger(SalaryRuleController.class);
	
	@Autowired
	private SalaryRuleService service;
	
	@RequestMapping(value = "salaryRule", method = RequestMethod.GET)
	public String salaryConfig(HttpServletRequest request, HttpServletResponse response){
		return "salaryRule";
	}
	
	@RequestMapping(value = "salaryRules", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> salaryRules(int limit, int offset, String order, String search, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> data = new HashMap<String, Object>();
		List<SalaryRule> rules = service.getSalaryRules(limit, offset, order, search);
		int total = service.getTotal();
		data.put("rows", rules);
		data.put("total", total);
		return data;
	}
	
	@RequestMapping(value = "saveSalaryRule", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo saveSalaryRule(@RequestBody SalaryRule salaryRule, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo(); 
		try {
			User user = (User)getUserFromSession(request);
			salaryRule.setOperateName(user.getUserName());
			boolean flag = service.save(salaryRule);
			if (flag){
				info.setCode(IConstants.QT_CODE_OK);
				info.setMessage("新增工资规则成功。");
				logger.info("新增工资规则成功。");
			} else {
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("新增工资规则失败。");
				logger.info("新增工资规则失败。");
			}
			
			
		} catch (Exception e) {
			logger.error("saveSalaryRule exception", e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("新增工资规则失败。");
		}
		
		return info;
	}
	
	@RequestMapping(value = "updateSalaryRule", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo updateSalaryRule(@RequestBody SalaryRule salaryRule, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo(); 
		try {
			User user = (User)getUserFromSession(request);
			salaryRule.setOperateName(user.getUserName());
			boolean flag = service.updateSalaryRuleByRid(salaryRule);
			if (flag){
				info.setCode(IConstants.QT_CODE_OK);
				info.setMessage("修改工资规则成功。");
				logger.info("修改工资规则成功。");
			} else {
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("修改工资规则失败。");
				logger.info("修改工资规则失败。");
			}
			
		} catch (Exception e) {
			logger.error("deleteSalaryRules exception", e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("修改工资规则失败。");
		}
		
		return info;
	} 

	@RequestMapping(value = "deleteSalaryRules", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo deleteSalaryRules(@RequestBody List<Long> rids, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo(); 
		try {
			boolean flag = service.deleteByRids(rids);
			if (flag){
				info.setCode(IConstants.QT_CODE_OK);
				info.setMessage("删除工资规则成功。");
				logger.info("删除工资规则成功。");
			} else {
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("删除工资规则失败。");
				logger.info("删除工资规则失败。");
			}
			
		} catch (Exception e) {
			logger.error("deleteSalaryRules exception", e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("删除工资规则失败。");
		}
		
		return info;
	}
	
	
}
