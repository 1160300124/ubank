package com.ulaiber.web.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.AttendancePatchClock;
import com.ulaiber.web.model.ResultInfo;

/** 
 * 考勤补卡API接口
 *
 * @author  huangguoqing
 * @date 创建时间：2017年9月25日 下午12:17:27
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/api/v1/")
public class AttendancePatchClockApiController extends BaseController{
	
	private static Logger logger = Logger.getLogger(AttendancePatchClockApiController.class);
	
	public ResultInfo patchClock(AttendancePatchClock patch, HttpServletRequest request, HttpServletResponse response){
		logger.debug("patchClock start...");
		ResultInfo info = new ResultInfo();
		
		logger.debug("patchClock end...");
		return info;
	}

}
