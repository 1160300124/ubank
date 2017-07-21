package com.ulaiber.web.controller.backend;

import java.util.HashMap;
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
import com.ulaiber.web.model.Page;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.PageService;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年7月19日 下午7:34:40
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/backend/")
public class PageController extends BaseController {
	
	private static Logger logger = Logger.getLogger(PageController.class);
	
	@Autowired
	private PageService service;
	
	@RequestMapping(value = "page", method = RequestMethod.GET)
	public String toPage(HttpServletRequest request, HttpServletResponse response){
		return "page";
	}
	
	@RequestMapping(value = "pages", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> pages(int limit, int offset, String order, String search, HttpServletRequest request, HttpServletResponse response){
		logger.debug("");
		if (limit <= 0){
			limit = 10;
		}
		if (offset < 0){
			offset = 0;
		}
		if(StringUtils.isEmpty(order)){
			order = "desc";
		}
		
		int total = service.getTotalNum();
		List<Page> pages = service.getAllPages(limit, offset, order, search);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("total", total);
		data.put("rows", pages);
		return data;
	}
	
	@RequestMapping(value = "getAllpages", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getAllpages( HttpServletRequest request, HttpServletResponse response){
		logger.debug("");
		ResultInfo info = new ResultInfo();
		List<Page> pages = service.getAllPagesforApi();
		info.setCode(IConstants.QT_CODE_OK);
		info.setData(pages);
	
		return info;
	}

}
