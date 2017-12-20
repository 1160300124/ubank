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
import com.ulaiber.web.model.salary.SalaryChange;
import com.ulaiber.web.service.SalaryChangeService;
import com.ulaiber.web.service.UserService;

/** 
 * 定薪，调薪控制器
 *
 * @author  huangguoqing
 * @date 创建时间：2017年12月13日 下午3:52:35
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/backend/")
public class SalaryChangeController extends BaseController {
	
	private static Logger logger = Logger.getLogger(SalaryChangeController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SalaryChangeService service;
	
	@RequestMapping("salaryChange")
	public String salaryChange(HttpServletRequest request, HttpServletResponse response){
		return "salaryChange";
	}
	
	/**
	 * 工资调整列表
	 * @param companyNum 公司号
	 * @param deptNum    部门号
	 * @param search     搜索关键字
	 * @param type       搜索类型 0：全部  1：在职  2：离职  3：未定薪  4：已定薪
	 * @param offset     从多少条数据开始
	 * @param limit      每页多少条
	 * @param request    request
	 * @param response   response
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "changeList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> changeList(String companyNum, String deptNum, String search, String type, int offset, int limit,
			HttpServletRequest request, HttpServletResponse response){
		logger.debug("");
		Map<String, Object> data = new HashMap<String, Object>();
		List<User> users = userService.getUsersForSalaryChange(search, offset, limit, companyNum, deptNum, type);
		int total = userService.getTotalForSalaryChange(search, companyNum, deptNum, type);
		data.put("total", total);
		data.put("rows", users);
		return data;
	}
	
	/**
	 * 工资调整
	 * @param salary 调整记录
	 * @param request request 
	 * @param response response
	 * @return ResultInfo
	 */
	@RequestMapping(value = "saveChange", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo saveChange(@RequestBody SalaryChange salary, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		try {
			User user = getUserFromSession(request);
			salary.setOperateUserId(user.getId());
			salary.setOperateUserName(user.getUserName());
			boolean flag = service.save(salary);
			if (flag){
				logger.info("工资调整成功。");
				info.setCode(IConstants.QT_CODE_OK);
				info.setMessage("工资调整成功。");
			} else {
				logger.info("工资调整失败。");
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("工资调整失败。");
			}
		} catch (Exception e) {
			logger.error("saveChange exception: ", e);
			logger.info("工资调整失败。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("工资调整失败。");
		}
		return info;
	}
	
	/**
	 * 获取个人的工资调整记录
	 * @param userId 用户ID
	 * @param request request 
	 * @param response response
	 * @return List<SalaryChange>
	 */
	@RequestMapping(value = "getSalaryChangeList", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getSalaryChangeList(Long userId, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (null == userId){
			logger.error("用户ID不能为空。");
			info.setCode(IConstants.INSER_DB_ERROR);
			info.setMessage("用户ID不能为空。");
			return info;
		}
		try {
			List<SalaryChange> records = service.getSalaryChangeByUserId(userId);
			info.setCode(IConstants.QT_CODE_OK);
			info.setData(records);
		} catch (Exception e) {
			logger.error("getSalaryChangeList exception: ", e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("获取调整记录失败。");;
		}
		return info;
	}
	
	/**
	 * 定薪
	 * @param userId 用户ID
	 * @param request request 
	 * @param response response
	 * @return List<SalaryChange>
	 */
	@RequestMapping(value = "setFirstSalary", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo setFirstSalary(Long userId, Long salary, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (null == userId || null == salary){
			logger.error("用户ID或初始薪资不能为空。");
			info.setCode(IConstants.INSER_DB_ERROR);
			info.setMessage("用户ID或初始薪资不能为空。");
			return info;
		}
		try {
			boolean flag = userService.updateSalaryByUserId(userId, salary);
			if (flag){
				logger.info("{"+ userId + "}定薪成功,初始工资：" + salary);
				info.setCode(IConstants.QT_CODE_OK);
				info.setMessage("定薪成功。");
			} else {
				logger.info("{"+ userId + "}定薪失败,初始工资：" + salary);
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("定薪成功。");;
			}
		} catch (Exception e) {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("定薪成功。");;
		}
		return info;
	}
	
}
