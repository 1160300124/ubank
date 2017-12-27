package com.ulaiber.web.controller.backend;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.Company;
import com.ulaiber.web.model.Departments;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.User;
import com.ulaiber.web.model.attendance.AttendanceRule;
import com.ulaiber.web.model.attendance.UserOfRule;
import com.ulaiber.web.service.AttendanceRuleService;
import com.ulaiber.web.service.PermissionService;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.DateTimeUtil;
import com.ulaiber.web.utils.ObjUtil;

/** 
 * 考勤规则后台控制器
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月18日 下午4:38:42
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/backend/")
public class AttendanceRuleController extends BaseController {
	
	private static Logger logger = Logger.getLogger(AttendanceRuleController.class);
	
	@Autowired
	private AttendanceRuleService service;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PermissionService permissionService; 

	@RequestMapping(value = "rule", method = RequestMethod.GET)
	public String attendance(HttpServletRequest request, HttpServletResponse response){

		return "attendanceRule";
	}
	
	@RequestMapping(value = "getRules", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> rules(int limit, int offset, String order, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> data = new HashMap<String, Object>();
		List<AttendanceRule> list = service.getRules(offset, limit, order);
		int total = service.getCount();
		data.put("total", total);
		data.put("rows", list);
		return data;
	}
	
	@RequestMapping(value = "addRule", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo addRule(AttendanceRule rule, String ruleData, String noRuleData, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		logger.debug("addRule start...");
		if (!ObjUtil.notEmpty(rule) || StringUtils.isEmpty(ruleData)){
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("参数为空！");
			return info;
		}
		String daytime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_DAYTIME);
		String clockOnStartTime = DateTimeUtil.getfutureTime(daytime + " " + rule.getClockOnTime(), 0, -rule.getClockOnAdvanceHours(), 0).split(" ")[1];
		String clockOffEndTime = DateTimeUtil.getfutureTime(daytime + " " + rule.getClockOffTime(), 0, rule.getClockOffDelayHours(), 0).split(" ")[1];
		rule.setClockOnStartTime(clockOnStartTime);
		rule.setClockOffEndTime(clockOffEndTime);
		
		try {
			
			if (service.save(rule, ruleData, noRuleData)){
				logger.info("新增考勤规则成功。");
				info.setCode(IConstants.QT_CODE_OK);
				info.setMessage("新增考勤规则成功。");
			} else {
				logger.info("新增考勤规则失败。");
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("新增考勤规则失败。");
			}
		} catch (Exception e) {
			logger.error("addRule exception: " , e);
		}
		
		
		return info;
	}
	
	/**
	 * 查询公司，部门，用户用来渲染树
	 * @param rid  规则id
	 * @param search  搜索关键字
	 * @param type  0：需要考勤规则  1：不需要考勤规则  2：既需要也不需要规则(二者都满足时以不需要为准)
	 * @param request   request
	 * @param response  response
	 * @return
	 */
	@RequestMapping(value = "getDeptsAndUsers", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getDeptsAndUsers(String rid, String search, String type, HttpServletRequest request, HttpServletResponse response){
		
		User conuser = getUserFromSession(request);
		List<Map<String, Object>> companyTree = new ArrayList<Map<String, Object>>();
		try {
			List<Company> companys = null;
			if (StringUtils.equals(conuser.getUserName(), "admin")){
				companys = permissionService.getAllCompany("0", "", "");
			} else {
				String[] companyNums = conuser.getCompanyNumber().split(",");
				companys = permissionService.getCompanysByNums(companyNums);
			}
			for (Company company : companys){
				List<UserOfRule> uofs = service.getUserIdsByComId(company.getCompanyNumber());
				//不能勾选的用户集合
				List<Long> chkDisabledUserIds = new ArrayList<Long>();
				//已勾选的用户集合
				List<Long> checkNodeUserIds = new ArrayList<Long>();
				//可勾选的用户集合
				List<Long> userIds = new ArrayList<Long>();
				for (UserOfRule uof : uofs){
					chkDisabledUserIds.add(uof.getUserId());
					if (StringUtils.equals(uof.getRid() + "", rid)){
						userIds.add(uof.getUserId());
						if (StringUtils.equals(uof.getType() + "", type) || StringUtils.equals(uof.getType() + "", "2")){
							checkNodeUserIds.add(uof.getUserId());
						}
					}
				}
				List<Departments> depts = permissionService.getDeptByCom(company.getCompanyNumber() + "");
				List<User> users = userService.getUsersByComNum(company.getCompanyNumber() + "", search);
				
				Map<String, Object> companyMap = new HashMap<String, Object>();
				List<Map<String, Object>> deptTree = new ArrayList<Map<String, Object>>();
				for (Departments dept : depts){
					Map<String, Object> deptMap = new HashMap<String, Object>();
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					for (User user : users){
						if (StringUtils.equals(dept.getDept_number(), user.getDept_number())){
							Map<String, Object> userMap = new HashMap<String, Object>();
							userMap.put("id", user.getId());
							userMap.put("name", user.getUserName());
							if (chkDisabledUserIds.contains(user.getId())){
								userMap.put("chkDisabled", true);
							}
							if (userIds.contains(user.getId())){
								userMap.put("chkDisabled", false);
							}
							if (checkNodeUserIds.contains(user.getId())){
								userMap.put("checked", true);
								//部门和公司下面有已选用户时才勾选
								deptMap.put("checked", true);
								companyMap.put("checked", true);
							}
							list.add(userMap);
						}
					}
					
					deptMap.put("id", dept.getDept_number());
					deptMap.put("name", dept.getName());
					deptMap.put("children" , list);
					deptMap.put("isParent", true);//设置根节点为父节点
					deptMap.put("open", true); //根节点展开
					deptTree.add(deptMap);
				}
				
				companyMap.put("id", company.getCompanyNumber());
				companyMap.put("name", company.getName());
				companyMap.put("children" , deptTree);
				companyMap.put("isParent", true);//设置根节点为父节点
				companyMap.put("open", true); //根节点展开
				companyTree.add(companyMap);
			}
		} catch (Exception e) {
			logger.error("getDeptsAndUsers exception:", e);
		}
		
		return companyTree;
	}
	
	@RequestMapping(value = "deleteRules", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo deleteRules(@RequestBody List<Long> rids, HttpServletRequest request, HttpServletResponse response){
		
		logger.debug("deleteRules start...");
		ResultInfo info = new ResultInfo();
		if (!ObjUtil.notEmpty(rids)){
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("参数为空！");
			return info;
		}
		boolean flag = service.deleteRulesByRids(rids);
		 if (flag){
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("删除成功");;
		} else {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("删除失败");
		}
		 logger.debug("deleteRules end...");
		return info;
	}
	
	@RequestMapping(value = "updateRule", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo updateRule(AttendanceRule rule, String ruleData, String noRuleData, HttpServletRequest request, HttpServletResponse response){
		
		logger.debug("updateRule start...");
		ResultInfo info = new ResultInfo();
		if (!ObjUtil.notEmpty(rule) || StringUtils.isEmpty(ruleData)){
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("参数为空！");
			return info;
		}
		
		String daytime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_DAYTIME);
		String clockOnStartTime = DateTimeUtil.getfutureTime(daytime + " " + rule.getClockOnTime(), 0, -rule.getClockOnAdvanceHours(), 0).split(" ")[1];
		String clockOffEndTime = DateTimeUtil.getfutureTime(daytime + " " + rule.getClockOffTime(), 0, rule.getClockOffDelayHours(), 0).split(" ")[1];
		rule.setClockOnStartTime(clockOnStartTime);
		rule.setClockOffEndTime(clockOffEndTime);
		
		try {
			boolean flag = service.update(rule, ruleData, noRuleData);
			if (flag){
				info.setCode(IConstants.QT_CODE_OK);
				info.setMessage("修改成功");;
			} else {
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("修改失败");
			}
		} catch (Exception e) {
			logger.error("updateRule exception: ", e);
		}
		logger.debug("updateRule end...");
		return info;
	}
	
	@RequestMapping(value = "getUserIdsByRid", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getUserIdsByRid(long rid, HttpServletRequest request, HttpServletResponse response){
		
		logger.debug("getUserIdsByRid start...");
		ResultInfo info = new ResultInfo();
		if (!ObjUtil.notEmpty(rid)){
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("参数为空！");
			return info;
		}
		List<UserOfRule> ids = service.getUserIdsByRid(rid);
		info.setCode(IConstants.QT_CODE_OK);
		info.setData(ids);
		logger.debug("getUserIdsByRid end...");
		return info;
	}
	
}
