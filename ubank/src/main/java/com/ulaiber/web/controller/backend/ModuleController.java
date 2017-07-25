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
import com.ulaiber.web.model.Module;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.ModuleService;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年7月19日 下午7:50:01
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/backend/")
public class ModuleController extends BaseController {
	
	private static Logger logger = Logger.getLogger(ModuleController.class);
	
	@Autowired
	private ModuleService service;
	
	@RequestMapping(value = "module", method = RequestMethod.GET)
	public String toModule(HttpServletRequest request, HttpServletResponse response){
		return "module";
	}
	
	@RequestMapping(value = "modules", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> modules(int limit, int offset, String order, String search, HttpServletRequest request, HttpServletResponse response){
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
		List<Module> modules = service.getAllModules(limit, offset, order, search);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("total", total);
		data.put("rows", modules);
		return data;
	}
	
	@RequestMapping(value = "getAllModules", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getAllModules( HttpServletRequest request, HttpServletResponse response){
		logger.debug("");
		ResultInfo info = new ResultInfo();
		List<Module> modules = service.getAllModulesforApi();
		info.setCode(IConstants.QT_CODE_OK);
		info.setData(modules);
		
		return info;
	}
	
	@RequestMapping(value = "saveModule", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo saveModule(@RequestBody Module module, HttpServletRequest request, HttpServletResponse response){
		logger.debug("saveModule start...");
		ResultInfo info = new ResultInfo();
		boolean flag = service.save(module);
		if (flag){
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("新增成功！");
		} else {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("新增失败！");
		}
		logger.debug("saveModule end...");
		return info;
	}
	
	@RequestMapping(value = "editModule", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo editModule(@RequestBody Module module, HttpServletRequest request, HttpServletResponse response){
		logger.debug("editModule start...");
		ResultInfo info = new ResultInfo();
		boolean flag = service.updateById(module);
		if (flag){
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("修改成功");;
		} else {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("修改失败");
		}
		logger.debug("editModule end...");
		return info;
	}

	@RequestMapping(value = "deleteModules", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo deleteModules(@RequestBody List<Integer> mids, HttpServletRequest request, HttpServletResponse response){
		logger.debug("deleteModules start...");
		ResultInfo info = new ResultInfo();
		boolean flag = service.deleteByIds(mids);
		 if (flag){
				info.setCode(IConstants.QT_CODE_OK);
				info.setMessage("删除成功");;
			} else {
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("删除失败");
			}
		 logger.debug("deleteModules end...");
		return info;
	}

}
