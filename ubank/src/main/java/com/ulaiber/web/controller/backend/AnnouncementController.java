package com.ulaiber.web.controller.backend;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ulaiber.web.utils.PushtoSingle;
import com.ulaiber.web.utils.StringUtil;
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
import com.ulaiber.web.controller.api.AttendanceApiController;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.User;
import com.ulaiber.web.model.announcement.Announcement;
import com.ulaiber.web.service.AnnouncementService;

/** 
 * 公告控制器
 *
 * @author  huangguoqing
 * @date 创建时间：2017年12月4日 上午11:35:29
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/backend/")
public class AnnouncementController extends BaseController {
	
	private static Logger logger = Logger.getLogger(AttendanceApiController.class);
	
	@Autowired
	private AnnouncementService service;
	
	/**
	 * 跳转公告列表页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("announcement")
	public String announcement(HttpServletRequest request, HttpServletResponse response){
		return "announcement";
	}
	
	/**
	 * 跳转新增公告列表页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("addAnnouncement")
	public String addAnnouncement(HttpServletRequest request, HttpServletResponse response){
		return "addAnnouncement";
	}
	
	@RequestMapping(value = "getAnnouncements", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> rules(int limit, int offset, String order, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> data = new HashMap<String, Object>();
		List<Announcement> list = service.getAnnouncements(limit, offset, "");
		int total = service.getTotalCount("");
		data.put("total", total);
		data.put("rows", list);
		return data;
	}
	
	
	/**
	 * 公告正文图片上传
	 * @param multiRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "uploadPicOfAnnounce", method = RequestMethod.POST)
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
        				info.setData("http://localhost:8080/ubank/" + path);
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
	
	@RequestMapping(value = "saveAnnouncement", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo saveAnnouncement(@RequestBody Announcement announcement, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		try {
			User user = getUserFromSession(request);
			announcement.setOperateUserId(user.getId());
			announcement.setOperateUserName(user.getUserName());
			boolean flag = service.save(announcement);
			if (flag){
				info.setCode(IConstants.QT_CODE_OK);
				info.setMessage("公告保存成功。");
			} else {
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("公告保存失败。");
			}
		} catch (Exception e) {
			logger.error("saveAnnouncement exception:", e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("公告保存失败。");
			return info;
		}

		return info;
	}

	/**
	 * 推送公告
	 * @param cid 个推CID
	 * @param content 内容
	 * @param title 标题
	 * @param id 详情id
	 * @return ResultInfo
	 */
	@RequestMapping(value = "sendNotice", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo notice(String cid,String content,String title,long id){
		ResultInfo resultInfo = new ResultInfo();
		try {
			int type = IConstants.NOTICE;
			String status = "";
			//暂时不需要内容
			content = "";
			String[] CIDS = cid.split(",");
			for (int i = 0 ; i < CIDS.length; i++){
				if(!StringUtil.isEmpty(CIDS[i])){
					PushtoSingle.singlePush(CIDS[i],type,content,title,id,status);
				}
			}
			resultInfo.setCode(IConstants.QT_CODE_OK);
			resultInfo.setMessage("推送公告成功");
			logger.info("推送公告成功");
		}catch (Exception e){
			resultInfo.setCode(IConstants.QT_CODE_ERROR);
			resultInfo.setMessage("推送公告失败");
			logger.error("推送公告失败",e);
		}
		return resultInfo;
	}
}
