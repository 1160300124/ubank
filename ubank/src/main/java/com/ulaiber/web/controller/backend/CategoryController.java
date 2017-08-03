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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.Category;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.CategoryService;
import com.ulaiber.web.service.ThirdUrlService;
import com.ulaiber.web.utils.ObjUtil;

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
public class CategoryController extends BaseController {
	
	private static Logger logger = Logger.getLogger(CategoryController.class);
	
	@Autowired
	private CategoryService service;
	
	@Autowired
	private ThirdUrlService urlService;
	
	@RequestMapping(value = "category", method = RequestMethod.GET)
	public String toPage(HttpServletRequest request, HttpServletResponse response){
		return "category";
	}
	
	@RequestMapping(value = "categories", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> categories(int limit, int offset, String order, String search, HttpServletRequest request, HttpServletResponse response){
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
		List<Category> categories = service.getAllCategories(limit, offset, order, search);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("total", total);
		data.put("rows", categories);
		return data;
	}
	
	@RequestMapping(value = "getAllCategories", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getAllCategories( HttpServletRequest request, HttpServletResponse response){
		logger.debug("getAllCategories start...");
		ResultInfo info = new ResultInfo();
		List<Category> categories = service.getAllCategoriesforApi();
		info.setCode(IConstants.QT_CODE_OK);
		info.setData(categories);
		logger.debug("getAllCategories end...");
		return info;
	}
	
	@RequestMapping(value = "saveCategory", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo saveCategory(@RequestBody Category category, HttpServletRequest request, HttpServletResponse response){
		logger.debug("saveCategory start...");
		ResultInfo info = new ResultInfo();
		boolean flag = service.save(category);
		if (flag){
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("新增类别成功！");
		} else {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("新增类别失败！");
		}
		logger.debug("saveCategory end...");
		return info;
	}
	
	@RequestMapping(value = "editCategory", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo editCategory(@RequestBody Category category, HttpServletRequest request, HttpServletResponse response){
		logger.debug("editCategory start...");
		ResultInfo info = new ResultInfo();
		boolean flag = service.updateById(category);
		if (flag){
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("修改成功");;
		} else {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("修改失败");
		}
		logger.debug("editCategory end...");
		return info;
	}

	@RequestMapping(value = "deleteCategories", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo deleteCategories(@RequestBody List<Integer> cids, HttpServletRequest request, HttpServletResponse response){
		logger.debug("deleteCategories start...");
		ResultInfo info = new ResultInfo();
		if (!ObjUtil.notEmpty(cids)){
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("参数为空！");
			return info;
		}
		if (urlService.getCountByCids(cids) > 0){
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("删除失败，您选中的类别有URL引用，请先删除引用的URL！");
			return info;
		}
		boolean flag = service.deleteByIds(cids);
		 if (flag){
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("删除成功");;
		} else {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("删除失败");
		}
		 logger.debug("deleteCategories end...");
		return info;
	}
}
