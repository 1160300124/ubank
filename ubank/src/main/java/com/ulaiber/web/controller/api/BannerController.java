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
import com.ulaiber.web.model.Banner;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.BannerService;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年7月24日 下午2:50:42
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/api/v1/")
public class BannerController extends BaseController {
	
	private static Logger logger = Logger.getLogger(BannerController.class);
	
	@Autowired
	private BannerService service;
	
	@RequestMapping(value = "getAllBanners", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getAllBanners(Integer mid, HttpServletRequest request, HttpServletResponse response){
		logger.debug("getAllBanners start...");
		ResultInfo info = new ResultInfo();
		if (null == mid || mid < 0){
			mid = 0;
		}
		List<Banner> banners = service.getBannersByMid(mid);
		info.setCode(IConstants.QT_CODE_OK);
		info.setData(banners);
		logger.debug("getAllBanners end...");
		return info;
	}

}
