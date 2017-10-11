package com.ulaiber.web.controller.backend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ulaiber.web.controller.BaseController;

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
public class SalaryController extends BaseController {
	
	private static Logger logger = Logger.getLogger(SalaryController.class);
	
	@RequestMapping(value = "salary", method = RequestMethod.GET)
	public String salary(HttpServletRequest request, HttpServletResponse response){
		return "salary";
	}
	
	@RequestMapping(value = "salaryDetail", method = RequestMethod.GET)
	public String salaryDetail(HttpServletRequest request, HttpServletResponse response){
		return "salaryDetail";
	}

}
