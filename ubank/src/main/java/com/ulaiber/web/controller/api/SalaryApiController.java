package com.ulaiber.web.controller.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.SalaryDetail;
import com.ulaiber.web.model.attendance.CutPayment;
import com.ulaiber.web.service.CutPaymentService;
import com.ulaiber.web.service.SalaryDetailService;
import com.ulaiber.web.service.SalaryService;

/** 
 * 工资API
 *
 * @author  huangguoqing
 * @date 创建时间：2017年10月27日 下午3:08:07
 * @version 1.0 
 * @since 
 */
@RequestMapping("/api/v1/")
@Controller
public class SalaryApiController extends BaseController {

	private static Logger logger = Logger.getLogger(SalaryApiController.class);
	
	@Autowired
	private SalaryService service;
	
	@Autowired
	private SalaryDetailService detailService;
	
	@Autowired
	private CutPaymentService cutService;
	
	@RequestMapping(value = "getSalaryList", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getSalaryList(long userId, int pageSize, int pageNum, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (pageSize <= 0){
			pageSize =20;
		}
		if(pageNum <= 0){
			pageNum = 1;
		}
		try {
			List<Map<String, Object>> data = service.getSalariesByUserId(userId, pageSize, pageNum);
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("获取用户工资流水成功。");
			info.setData(data);
		} catch (Exception e) {
			logger.error("getSalaryList exception: ", e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("系统内部错误。");
		}

		return info;
	}
	
	@RequestMapping(value = "getSalaryDetail", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getSalaryDetail(long userId, String month, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (StringUtils.isEmpty(month)){
			logger.error("月份不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("月份不能为空。");
			return info;
		}
		
		try {
			SalaryDetail detail = detailService.getSalaryDetailByUserIdAndMonth(userId, month);
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("获取用户工资详细流水成功。");
			info.setData(detail);
		} catch (Exception e) {
			logger.error("getSalaryDetail exception: ", e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("系统内部错误。");
		}

		return info;
	}
	
	@RequestMapping(value = "getCutPaymentMessage", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getCutPaymentMessage(long userId, String month, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (StringUtils.isEmpty(month)){
			logger.error("月份不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("月份不能为空。");
			return info;
		}
		
		try {
			List<CutPayment> list = cutService.getCutPaymentByMonthAndUserId(userId, month);
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("获取用户扣款详细成功。");
			info.setData(list);
		} catch (Exception e) {
			logger.error("getSalaryDetail exception: ", e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("系统内部错误。");
		}

		return info;
	}
}
