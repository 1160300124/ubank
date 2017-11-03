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
import com.ulaiber.web.model.attendance.CutPayment;
import com.ulaiber.web.service.CutPaymentService;
import com.ulaiber.web.utils.DateTimeUtil;

/** 
 * 扣款控制器
 *
 * @author  huangguoqing
 * @date 创建时间：2017年11月1日 上午10:01:38
 * @version 1.0 
 * @since 
 */
@RequestMapping("/backend/")
@Controller
public class CutPaymentController extends BaseController {
	
	private static Logger logger = Logger.getLogger(CutPaymentController.class);
	
	@Autowired
	private CutPaymentService service;
	
	@RequestMapping(value = "cutPayment", method = RequestMethod.GET)
	public String cutPayment(HttpServletRequest request, HttpServletResponse response){
		logger.info(">>>>>>>>>>>>>>");
		return "cutPayment";
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getCutPaymentList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getCutPaymentList(CutPayment cut, String startDate, String endDate, int limit, int offset, String order, 
			HttpServletRequest request, HttpServletResponse response){
		logger.debug("getCutPaymentList start...");
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limit", limit);
		params.put("offset", offset);
		params.put("order", order);
		params.put("company_num", cut.getCompany().getCompanyNumber());
		params.put("dept_num", cut.getDept().getDept_number());
		params.put("cut_type", cut.getCutType());
		params.put("user_name", cut.getUserName());
		params.put("start_date", startDate);
		params.put("end_date", endDate);
		if (DateTimeUtil.getNumFromdate(startDate, endDate) > 31){
			return data;
		}
		
		int total = service.getTotalNum(params);
		List<CutPayment> list = service.getCutPaymentList(params);
		data.put("total", total);
		data.put("rows", list);
		logger.debug("getCutPaymentList end...");
		return data;
	}
}
