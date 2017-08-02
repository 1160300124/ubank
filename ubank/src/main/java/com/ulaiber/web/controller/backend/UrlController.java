package com.ulaiber.web.controller.backend;

import java.io.File;
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
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.ThirdUrl;
import com.ulaiber.web.service.ThirdUrlService;
import com.ulaiber.web.utils.FileUtil;

/**
 * 第三方URL管理控制器
 * 
 * @author huangguoqing
 *
 */
@Controller
@RequestMapping("/backend/")
public class UrlController extends BaseController {
	
	/**
	 * 日志对象
	 */
	private static Logger logger = Logger.getLogger(UrlController.class);
	
	@Autowired
	private ThirdUrlService service;
	
	@RequestMapping(value = "thirdUrl", method = RequestMethod.GET)
	public String url(HttpServletRequest request, HttpServletResponse response){
		return "url";
	}
	
	@RequestMapping(value = "thirdUrls", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryThirdUrls(int limit, int offset, String order, String search, HttpServletRequest request, HttpServletResponse response){
		
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
		List<ThirdUrl> urls = service.getAllUrls(limit, offset, order, search);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("total", total);
		data.put("rows", urls);
		return data;
	}
	
	@RequestMapping(value = "uploadUrlPic", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo picupload(MultipartHttpServletRequest multiRequest, HttpServletRequest request, HttpServletResponse response){
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
        				
        				String path = upload(request, IConstants.ICON_TEMP_UPLOAD_PATH, file.getBytes(), fileName);
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
	
	@RequestMapping(value= "saveUrl", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo saveUrl(@RequestBody ThirdUrl url, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		
		if (StringUtils.isEmpty(url.getPicPath())){
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("请上传图标！");
			return info;
		}
		
		String tempPath = url.getPicPath();
		if (tempPath.contains(IConstants.TEMP_PATH)){
			//copy临时路径的图标到正式目录
			String path = tempPath.replace(IConstants.TEMP_PATH, "");
			File tempFile = new File(getProjectAbsoluteURL(request) + tempPath);
			File file = new File(getProjectAbsoluteURL(request) + IConstants.ICON_UPLOAD_PATH);
			if (!FileUtil.copy(tempFile, file)){
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("图标复制异常！");
				return info;
			}
			url.setPicPath(IConstants.PICTURE_SERVER + path);
		}

		boolean flag = service.save(url);
		if (flag){
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("新增第三方URL成功！");
			if (tempPath.contains(IConstants.TEMP_PATH)){
				//成功后删除temp下的临时图标，以后会加入定时任务，定时清理临时目录，防止异常情况下，临时目录图标删不掉导致目录越来越大
				FileUtil.delFile(getProjectAbsoluteURL(request) + tempPath);
			}
		} else {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("新增第三方URL失败！");
		}
		
		return info;
	}
	
	@RequestMapping(value= "deleteUrl", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo deleteUrl(@RequestBody List<Long> uids, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		
		boolean flag = service.deleteByUids(uids);
		
		if (flag){
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("删除成功！");
		} else {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("删除失败！");
		}
		
		return info;
		
	}
	
	@RequestMapping(value= "editUrl", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo editUrl(@RequestBody ThirdUrl url, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		
		if (url.getUid() == 0)
		{
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("无效的数据！");
			return info;
		}
		
		String tempPath = url.getPicPath();
		if (tempPath.contains(IConstants.TEMP_PATH)){
			//copy临时路径的图标到正式目录
			String path = tempPath.replace(IConstants.TEMP_PATH, "");
			File tempFile = new File(getProjectAbsoluteURL(request) + tempPath);
			File file = new File(getProjectAbsoluteURL(request) + IConstants.ICON_UPLOAD_PATH);
			if (!FileUtil.copy(tempFile, file)){
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("图标复制异常！");
				return info;
			}
			url.setPicPath(IConstants.PICTURE_SERVER + path);
		}
		
		boolean flag = service.updateByUid(url);
		
		if (flag){
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("修改成功！");
			if (tempPath.contains(IConstants.TEMP_PATH)){
				//成功后删除temp下的临时图标
				FileUtil.delFile(getProjectAbsoluteURL(request) + tempPath);
			}
		} else {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("修改失败！");
		}
		
		return info;
		
	}

}
