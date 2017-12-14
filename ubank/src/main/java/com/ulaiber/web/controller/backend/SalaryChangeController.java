package com.ulaiber.web.controller.backend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ulaiber.web.controller.BaseController;

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
	
	@RequestMapping("salaryChange")
	public String salaryChange(HttpServletRequest request, HttpServletResponse response){
		return "salaryChange";
	}
}
