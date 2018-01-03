package com.ulaiber.web.controller.backend;

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
import com.ulaiber.web.controller.api.AttendanceApiController;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.User;
import com.ulaiber.web.model.announcement.Announcement;
import com.ulaiber.web.model.announcement.Attachment;
import com.ulaiber.web.service.AnnouncementService;
import com.ulaiber.web.utils.MathUtil;
import com.ulaiber.web.utils.PushtoSingle;
import com.ulaiber.web.utils.StringUtil;
import com.ulaiber.web.utils.SysConf;

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
	
	/**
	 * 分页查询公告列表
	 * @param limit
	 * @param offset
	 * @param order
	 * @param request
	 * @param response
	 * @return
	 */
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
	public ResultInfo uploadPicOfAnnounce(MultipartHttpServletRequest multiRequest, HttpServletRequest request, HttpServletResponse response){
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
        				
        				String path = upload(request, IConstants.ATTACHMENT_TEMP_UPLOAD_PATH, file.getBytes(), fileName);
        				logger.debug("upload temp path----" + path);
        				if (StringUtils.isEmpty(path)){
        					info.setCode(IConstants.QT_CODE_ERROR);
            				info.setMessage("上传图片失败：IO异常！" );
            				return info;
        				}
        				info.setCode(IConstants.QT_CODE_OK);
        				info.setData(SysConf.getProperty("SYS_DOMAIN_NAME", "http://ubankapp.cn") + path);
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
	
	/**
	 * 公告正文图片上传
	 * @param multiRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "uploadAttachment", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo uploadAttachment(MultipartHttpServletRequest multiRequest, HttpServletRequest request, HttpServletResponse response){
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
        			String type = "0";
        			if (fileName.endsWith("doc") || fileName.endsWith("docx")){
        				type = "0";	
        			}
        			else if (fileName.endsWith("xls") || fileName.endsWith("xlsx")){
        				type = "1";	
        			}
        			else if (fileName.endsWith("ppt") || fileName.endsWith("pptx")){
        				type = "2";	
        			}
        			else if (fileName.endsWith("pdf")){
        				type = "3";	
        			}
        			else if (fileName.endsWith("zip")){
        				type = "4";	
        			}
        			else if (fileName.endsWith("rar")){
        				type = "5";	
        			}
        			else {
        				info.setCode(IConstants.QT_CODE_ERROR);
        				info.setMessage("上传附件格式错误" );
        				return info;
        			}
        			long fileSize = file.getSize();
        			double kBsize = MathUtil.div(fileSize, 1024 , 1);
        			String size = kBsize + "KB";
        			if (kBsize > 1024){
        				double MBsize = MathUtil.div(fileSize, 1024 * 1024, 2);
        				size = MBsize + "MB";
        			}
        			// 如果名称不为"",说明该文件存在，否则说明该文件不存在
        			if (fileName.trim() != "") {
        				logger.info("upload file name: " + fileName.trim());
        				
        				String path = upload(request, IConstants.ATTACHMENT_TEMP_UPLOAD_PATH, file.getBytes(), fileName);
        				logger.debug("upload temp path----" + path);
        				if (StringUtils.isEmpty(path)){
        					info.setCode(IConstants.QT_CODE_ERROR);
            				info.setMessage("上传附件失败：IO异常！" );
            				return info;
        				}
        				info.setCode(IConstants.QT_CODE_OK);
        				Attachment att = new Attachment();
        				att.setAttachment_name(fileName);
        				att.setAttachment_path(SysConf.getProperty("SYS_DOMAIN_NAME", "http://ubankapp.cn") + path);
        				att.setAttachment_size(size);
        				att.setAttachment_type(type);
        				info.setData(att);
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
	
	/**
	 * 保存公告
	 * @param announcement 公告实体
	 * @param request request
	 * @param response response
	 * @return ResultInfo
	 */
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
				service.send(announcement.getUserIds(), announcement.getAid(), announcement.getAnnounceTitle());
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
	 * 获取公告附件
	 * @param aid 公告ID
	 * @param request request
	 * @param response response
	 * @return ResultInfo
	 */
	@RequestMapping(value = "getAttachments", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getAttachments(long aid, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		try {

			List<Attachment> list = service.getAttachmentsByAid(aid);
			info.setCode(IConstants.QT_CODE_OK);
			info.setData(list);
		} catch (Exception e) {
			logger.error("deleteAnnouncement exception:", e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("获取附件失败。");
			return info;
		}

		return info;
	}
	
	/**
	 * 撤回公告
	 * @param request request
	 * @param response response
	 * @return ResultInfo
	 */
	@RequestMapping(value = "deleteAnnouncement", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo deleteAnnouncement(long aid, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		try {

			boolean flag = service.deleteByAid(aid);
			if (flag){
				info.setCode(IConstants.QT_CODE_OK);
				info.setMessage("公告撤回成功。");
			} else {
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("公告撤回失败。");
			}
		} catch (Exception e) {
			logger.error("deleteAnnouncement exception:", e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("公告撤回失败。");
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
			content = title;
			title = "您有一条新的公告消息";
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
