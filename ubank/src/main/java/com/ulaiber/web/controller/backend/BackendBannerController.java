package com.ulaiber.web.controller.backend;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.Banner;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.BannerService;
import com.ulaiber.web.utils.FileUtil;
import com.ulaiber.web.utils.ObjUtil;
import com.ulaiber.web.utils.SysConf;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年7月24日 下午7:52:47
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/backend/")
public class BackendBannerController extends BaseController {
	
	private static Logger logger = Logger.getLogger(BackendBannerController.class);
	
	@Autowired
	private BannerService service;
	
	@RequestMapping(value = "banner", method = RequestMethod.GET)
	public String banner(HttpServletRequest request, HttpServletResponse response){
		return "banner";
	}
	
	@RequestMapping(value = "banners", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> banners(int limit, int offset, String order, String search, HttpServletRequest request, HttpServletResponse response){
		logger.debug("banners start...");
		if (limit <= 0){
			limit = 10;
		}
		if (offset < 0){
			offset = 0;
		}
		if(StringUtils.isEmpty(order)){
			order = "desc";
		}
		
		int totalNUm = service.getTotalNum();
		List<Banner> banners = service.getBanners(limit, offset, order, search);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("total", totalNUm);
		data.put("rows", banners);
		logger.debug("banners end...");
		return data;
	}
	
	@RequestMapping(value = "uploadBannerPic", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo uploadBannerPic(MultipartHttpServletRequest multiRequest, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		try {
    		// 取得request中的所有文件名
        	Iterator<String> iter = multiRequest.getFileNames();
        	while (iter.hasNext()) {
        		// 取得上传文件
        		MultipartFile file = multiRequest.getFile(iter.next());
        		if (file != null) {
        			// 取得当前上传文件的文件名称
        			String fileName = file.getOriginalFilename();
        			if (!fileName.endsWith("jpg") && !fileName.endsWith("png")){
        				info.setCode(IConstants.QT_CODE_ERROR);
        				info.setMessage("请上传jpg或png文件！");
        				return info;
        			}
        			// 如果名称不为"",说明该文件存在，否则说明该文件不存在
        			if (fileName.trim() != "") {
        				logger.info("upload file name: " + fileName.trim());
        				
        				String path = upload(request, IConstants.BANNER_TEMP_UPLOAD_PATH, file.getBytes(), fileName);
        				logger.debug("upload temp path----" + path);
        				if (StringUtils.isEmpty(path)){
        					info.setCode(IConstants.QT_CODE_ERROR);
            				info.setMessage("上传图片失败：IO异常！" );
            				return info;
        				}
        				info.setCode(IConstants.QT_CODE_OK);
        				info.setData(path);
//        				file.transferTo(arg0);
        				
        			}
        		}
        	}
    	} catch (Exception e) {
			logger.error("upload file failed:" , e);
			info.setCode(IConstants.QT_CODE_ERROR);
		}
		
		return info;
	}
	
	@RequestMapping(value= "saveBanner", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo saveBanner(@RequestBody Banner banner, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		
		if (StringUtils.isEmpty(banner.getPicPath())){
			logger.error("请上传图片！");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("请上传图片！");
			return info;
		}
		
		String tempPath = banner.getPicPath();
		if (tempPath.contains(IConstants.TEMP_PATH)){
			//copy临时路径的图标到正式目录
			String path = tempPath.replace(IConstants.TEMP_PATH, "");
			File tempFile = new File(getProjectAbsoluteURL(request) + tempPath);
			File file = new File(getProjectAbsoluteURL(request) + IConstants.BANNER_UPLOAD_PATH);
			if (!FileUtil.copy(tempFile, file)){
				logger.error("图片复制异常！");
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("图片复制异常！");
				return info;
			}
			banner.setPicPath(SysConf.getProperty("SYS_DOMAIN_NAME", "http://ubankapp.cn") + path);
		}

		boolean flag = service.save(banner);
		if (flag){
			logger.info("新增banner成功！");
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("新增banner成功！");
			if (tempPath.contains(IConstants.TEMP_PATH)){
				//成功后删除temp下的临时图标，以后会加入定时任务，定时清理临时目录，防止异常情况下，临时目录图标删不掉导致目录越来越大
				FileUtil.deleteAllFilesOfDir(getProjectAbsoluteURL(request) + tempPath);
			}
		} else {
			logger.error("新增banner失败！");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("新增banner失败！");
		}
		
		return info;
	}
	
	@RequestMapping(value= "deleteBanners", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo deleteBanners(@RequestBody List<Banner> data, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (!ObjUtil.notEmpty(data)){
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("参数不能为空");
			return info;
		}
		List<Long> bids = new ArrayList<Long>();
		List<String> paths = new ArrayList<String>();
		String server = SysConf.getProperty("SYS_DOMAIN_NAME", "http://ubankapp.cn");
		for (Banner ban : data){
			bids.add(ban.getBid());
			String path = ban.getPicPath().replace(server, getProjectAbsoluteURL(request));
			paths.add(path);
		}
		boolean flag = service.deleteByIds(bids);
		
		if (flag){
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("删除成功！");
			for (String path : paths){
				FileUtil.deleteAllFilesOfDir(path);
			}
		} else {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("删除失败！");
		}
		
		return info;
		
	}
	
	@RequestMapping(value= "editBanner", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo editBanner(@RequestBody Banner banner, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		
		if (banner.getBid() == 0)
		{
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("无效的数据！");
			return info;
		}
		
		String tempPath = banner.getPicPath();
		if (tempPath.contains(IConstants.TEMP_PATH)){
			//copy临时路径的图标到正式目录
			String path = tempPath.replace(IConstants.TEMP_PATH, "");
			File tempFile = new File(getProjectAbsoluteURL(request) + tempPath);
			File file = new File(getProjectAbsoluteURL(request) + IConstants.BANNER_UPLOAD_PATH);
			if (!FileUtil.copy(tempFile, file)){
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("图片复制异常！");
				return info;
			}
			banner.setPicPath(SysConf.getProperty("SYS_DOMAIN_NAME", "http://ubankapp.cn") + path);
		}
		
		boolean flag = service.updateById(banner);
		
		if (flag){
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("修改成功！");
			if (tempPath.contains(IConstants.TEMP_PATH)){
				//成功后删除temp下的临时图标
				FileUtil.deleteAllFilesOfDir(getProjectAbsoluteURL(request) + tempPath);
			}
		} else {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("修改失败！");
		}
		
		return info;
		
	}

}
