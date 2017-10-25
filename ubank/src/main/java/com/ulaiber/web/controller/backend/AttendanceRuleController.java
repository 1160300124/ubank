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
import com.ulaiber.web.model.UserOfRule;
import com.ulaiber.web.model.attendance.AttendanceRule;
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
	
	@RequestMapping(value = "addRules", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo addRules(AttendanceRule rule, String data, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		
		String daytime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_DAYTIME);
		String clockOnStartTime = DateTimeUtil.getfutureTime(daytime + " " + rule.getClockOnTime(), 0, -rule.getClockOnAdvanceHours(), 0).split(" ")[1];
		String clockOffEndTime = DateTimeUtil.getfutureTime(daytime + " " + rule.getClockOffTime(), 0, rule.getClockOffDelayHours(), 0).split(" ")[1];
		rule.setClockOnStartTime(clockOnStartTime);
		rule.setClockOffEndTime(clockOffEndTime);
		
		try {
			
			if (service.save(rule, data)){
				logger.info("新增考勤规则成功。");
				info.setCode(IConstants.QT_CODE_OK);
				info.setMessage("新增考勤规则成功。");
			} else {
				logger.info("新增考勤规则失败。");
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("新增考勤规则失败。");
			}
		} catch (Exception e) {
			logger.error("addRules exception: " , e);
		}
		
		
		return info;
	}
	
	@RequestMapping(value = "getDeptsAndUsers", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getDeptsAndUsers(String search, HttpServletRequest request, HttpServletResponse response){
		
		User conuser = getUserFromSession(request);
		List<Map<String, Object>> companyTree = new ArrayList<Map<String, Object>>();
		try {
			String[] companyNums = conuser.getCompanyNumber().split(",");
			List<Company> companys = permissionService.getCompanysByNums(companyNums);
			Map<String, String> comMap = new HashMap<String, String>();
			for (Company com : companys){
				comMap.put(com.getCompanyNumber() + "", com.getName());
			}
			for (String companyNum : companyNums){
				List<UserOfRule> uofs = service.getUserIdsByComId(Integer.parseInt(companyNum));
				List<Long> userIds = new ArrayList<Long>();
				for (UserOfRule uof : uofs){
					userIds.add(uof.getUserId());
				}
				List<Departments> depts = permissionService.getDeptByCom(companyNum);
				List<User> users = userService.getUsersByComNum(companyNum, search);
				
				String companyName = comMap.get(companyNum);
				List<Map<String, Object>> deptTree = new ArrayList<Map<String, Object>>();
				for (Departments dept : depts){
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					for (User user : users){
						if (StringUtils.equals(dept.getDept_number(), user.getDept_number())){
							Map<String, Object> userMap = new HashMap<String, Object>();
							userMap.put("id", user.getId());
							userMap.put("name", user.getUserName());
							if (userIds.contains(user.getId())){
								userMap.put("chkDisabled", true);
							}
							list.add(userMap);
						}
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", dept.getDept_number());
					map.put("name", dept.getName());
					map.put("children" , list);
					map.put("isParent", true);//设置根节点为父节点
					map.put("open", true); //根节点展开
					deptTree.add(map);
				}
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", companyNum);
				map.put("name", companyName);
				map.put("children" , deptTree);
				map.put("isParent", true);//设置根节点为父节点
				map.put("open", true); //根节点展开
				companyTree.add(map);
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
	public ResultInfo updateRule(AttendanceRule rule, String data, HttpServletRequest request, HttpServletResponse response){
		
		logger.debug("updateRule start...");
		ResultInfo info = new ResultInfo();
		if (!ObjUtil.notEmpty(rule) || StringUtils.isEmpty(data)){
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

			boolean flag = service.update(rule, data);
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
