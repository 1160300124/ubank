package com.ulaiber.web.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.ThirdUrl;
import com.ulaiber.web.service.ThirdUrlService;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年7月24日 上午9:31:27
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/api/v1/")
public class ThirdUrlController extends BaseController {
	
	private static Logger logger = Logger.getLogger(ThirdUrlController.class);
	
	@Autowired
	private ThirdUrlService service;
	
	@RequestMapping(value = "getThirdUrls", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getThirdUrls(Integer mid, Integer cid, HttpServletRequest request, HttpServletResponse response){
		logger.debug("getThirdUrls start...");
		ResultInfo info = new ResultInfo();
		if (null == mid || mid < 0){
			mid = 0;
		}
		if (null == cid || cid < 0){
			cid = 0;
		}
		List<ThirdUrl> urls = service.getUrlsByMidAndCid(mid, cid);
		
		info.setCode(IConstants.QT_CODE_OK);;
		info.setData(urls);
		logger.debug("getThirdUrls end...");
		return info;
	} 

}
