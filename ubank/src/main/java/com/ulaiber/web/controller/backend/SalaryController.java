package com.ulaiber.web.controller.backend;

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
import com.ulaiber.web.model.Departments;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.Salary;
import com.ulaiber.web.model.SalaryDetail;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.PermissionService;
import com.ulaiber.web.service.SalaryDetailService;
import com.ulaiber.web.service.SalaryService;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.DateTimeUtil;
import com.ulaiber.web.utils.SPDBUtil;

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
	
	@Autowired
	private PermissionService permissionService; 
	
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
		int total = service.getTotalNum();
		List<Salary> list = service.getSalaries(limit, offset, search);	
		for (Salary sa : list){
			if (StringUtils.isNotEmpty(sa.getEntrustSeqNo())){
				String oldStatus = sa.getStatus();
				String newStatus = "";
				if (!StringUtils.equals(oldStatus, "5")){
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("seqNo", sa.getEntrustSeqNo());
					params.put("beginDate", sa.getSalaryDate().replaceAll("-", ""));
					params.put("endDate", DateTimeUtil.getMonthEnd(sa.getSalaryDate()).replaceAll("-", ""));
				    newStatus = SPDBUtil.getPayResult(params);
//				    newStatus = "5";
					sa.setStatus(newStatus);
					//if new status != old status 更新工资流水状态
					if (!StringUtils.equals(oldStatus, newStatus)){
						service.updateStatusBySeqNo(sa);
					}
				}
				//状态转换
				String status = StringUtils.isEmpty(newStatus) ? oldStatus : newStatus;
				sa.setStatus(IConstants.TRANS_STATUS.get(status));
			}
		}
		
		data.put("rows", list);
		data.put("total", total);
		return data;
	}
	
	@RequestMapping(value = "getSalaryDetails", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSalaryDetails(String sid, int limit, int offset, String order, String search, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> data = new HashMap<String, Object>();
		if (sid == null){
			return data;
		}
		List<SalaryDetail> details = detailService.getDetailsBySid(sid, limit, offset, order, search);
		int total = detailService.getTotalBySid(sid);
		data.put("rows", details);
		data.put("total", total);
		return data;
	}
	
	@RequestMapping(value = "getSalaryDetailsBySid", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getSalaryDetailsBySid(String sid, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (sid == null){
			return info;
		}
		List<SalaryDetail> details = detailService.getDetailsBySid(sid);
		info.setCode(IConstants.QT_CODE_OK);
		info.setData(details);;
		return info;
	}
	
	@RequestMapping(value = "saveSalary", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo saveSalary(@RequestBody Salary salary, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		try {
			User user = getUserFromSession(request);
			salary.setOperateUserId(user.getId());
			salary.setOperateUserName(user.getUserName());
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
	
	@RequestMapping(value = "updateSalary", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo updateSalary(@RequestBody Salary salary, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		try {
			User user = getUserFromSession(request);
			salary.setOperateUserId(user.getId());
			salary.setOperateUserName(user.getUserName());
			boolean flag = service.update(salary);
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
	public ResultInfo importUserInfo(String companyNum, String salaryMonth, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if(StringUtils.isEmpty(companyNum)){
			logger.info("公司不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("请选择公司。");
			return info;
		}
		
		try {
			List<SalaryDetail> details = service.importUserInfo(companyNum, salaryMonth);
			info.setCode(IConstants.QT_CODE_OK);
			info.setData(details);
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
			List<SalaryDetail> details = detailService.getLatestSalaryDetail(comNum);
			info.setCode(IConstants.QT_CODE_OK);
			info.setData(details);
		} catch (Exception e) {
			logger.info("获取最近失败工资列表失败。" , e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("系统内部异常。");
		}
		 
		return info;
	}
	
    @RequestMapping(value = "batchDelete", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo batchDelete(@RequestBody List<String> sids, HttpServletRequest request, HttpServletResponse response){
    	
    	ResultInfo info = new ResultInfo();
    	if (null == sids || sids.size() == 0){
    		return info;
    	}
    	
    	try {
    		boolean flag = service.batchDeleteSalaries(sids);
    		if (flag){
    			info.setCode(IConstants.QT_CODE_OK);
    			info.setMessage("删除工资记录成功。");
    			logger.info("删除工资记录成功。");
    		} else {
    			info.setCode(IConstants.QT_CODE_ERROR);
    			info.setMessage("删除工资记录失败。");
    			logger.error("删除工资记录失败。");
    		}
		} catch (Exception e) {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("删除工资记录失败。");
			logger.error("删除工资记录失败。", e);
		}
    	
    	return info;
    }
    
    @RequestMapping(value = "getDeptsAndUsersFromCompany", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getDeptsAndUsers(String companyId, String companyName, String search, HttpServletRequest request, HttpServletResponse response){
    	Map<String, Object> tree = new HashMap<String, Object>();
    	try {
    		List<Departments> depts = permissionService.getDeptByCom(companyId);
    		List<User> users = userService.getUsersByComNum(companyId, search);

    		List<Map<String, Object>> deptTree = new ArrayList<Map<String, Object>>();
    		for (Departments dept : depts){
    			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    			for (User user : users){
    				if (StringUtils.equals(dept.getDept_number(), user.getDept_number())){
    					Map<String, Object> userMap = new HashMap<String, Object>();
    					userMap.put("id", user.getId());
    					userMap.put("name", user.getUserName());
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

    		tree.put("id", companyId);
    		tree.put("name", companyName);
    		tree.put("children" , deptTree);
    		tree.put("isParent", true);//设置根节点为父节点
    		tree.put("open", true); //根节点展开
    	} catch (Exception e) {
    		logger.error("getDeptsAndUsers exception:", e);
    	}

    	return tree;
    }
	
}
