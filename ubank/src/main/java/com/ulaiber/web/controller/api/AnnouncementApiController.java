package com.ulaiber.web.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.ulaiber.web.model.announcement.Announcement;
import com.ulaiber.web.model.announcement.Attachment;
import com.ulaiber.web.service.AnnouncementService;

/** 
 * 公告Api控制器
 *
 * @author  huangguoqing
 * @date 创建时间：2017年12月6日 上午11:43:47
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/api/v1/")
public class AnnouncementApiController extends BaseController {
	
	private static Logger logger = Logger.getLogger(AnnouncementApiController.class);
	
	@Autowired
	private AnnouncementService service;
	
	/**
	 * 获取用户公告列表
	 * @param userId 用户id
	 * @param pageSize 每页多少条
	 * @param pageNum 第几页
	 * @param request request
	 * @param response response
	 * @return ResultInfo
	 */
	@RequestMapping(value = "getAnnouncementList", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getAnnouncementList(long userId, int pageSize, int pageNum, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (pageSize <= 0){
			pageSize =20;
		}
		if(pageNum <= 0){
			pageNum = 1;
		}
		try {
			List<Map<String, Object>> list = service.getAnnouncementsByUserId(userId, pageSize, pageNum);
			List<Map<String, Object>> data = service.getAttachmentCountByUserId(userId);
			Map<String, Object> attachmentMap = new HashMap<String, Object>();
			for (Map<String, Object> map : data){
				attachmentMap.put(map.get("aid").toString(), map.get("total"));
			}
			for (Map<String, Object> map: list){
				map.put("announce_body", filterHtml(fiterHtmlTag(map.get("announce_body").toString(), "img")));
				map.put("attachment_count", null == attachmentMap.get(map.get("aid").toString()) ? 0 : attachmentMap.get(map.get("aid").toString()));
			}
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("获取公告记录成功。");
			info.setData(list);
		} catch (Exception e) {
			logger.error("getAnnouncementList exception:", e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("获取公告记录失败。");
		}
		return info;
	}
	
    /**  
     *   
     * 基本功能：过滤所有以"<"开头以">"结尾的标签  
     * <p>  
     *   
     * @param str  
     * @return String  
     */  
    public static String filterHtml(String str) {   
        Pattern pattern = Pattern.compile("<([^>]*)>");   
        Matcher matcher = pattern.matcher(str);   
        StringBuffer sb = new StringBuffer();   
        boolean result1 = matcher.find();   
        while (result1) {   
            matcher.appendReplacement(sb, "");   
            result1 = matcher.find();   
        }   
        matcher.appendTail(sb);   
        return sb.toString();   
    }  
	
    /**  
     *   
     * 基本功能：过滤指定标签  
     * <p>  
     *   
     * @param str  
     * @param tag  
     *            指定标签  
     * @return String  
     */  
    public static String fiterHtmlTag(String str, String tag) {   
        String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";   
        Pattern pattern = Pattern.compile(regxp);   
        Matcher matcher = pattern.matcher(str);   
        StringBuffer sb = new StringBuffer();   
        boolean result1 = matcher.find();   
        while (result1) {   
            matcher.appendReplacement(sb, "[图片]");   
            result1 = matcher.find();   
        }   
        matcher.appendTail(sb);   
        return sb.toString();   
    } 
    
	/**
	 * 获取用户公告列表
	 * @param userId 用户id
	 * @param aid 公告id
	 * @param request request
	 * @param response response
	 * @return ResultInfo
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String detail(Long userId, Long aid, HttpServletRequest request, HttpServletResponse response){
		try {
			if (userId != null && aid != null){
				//更新公告为已读
				service.updateTypeByUserIdAndAid(userId, aid);
				//获取公告信息，附件信息
				Announcement announcement = service.getAnnouncementByAid(aid);
				List<Attachment> list = service.getAttachmentsByAid(aid);
				request.setAttribute("title", announcement.getAnnounceTitle());
				request.setAttribute("companyName", announcement.getCompanyName());
				request.setAttribute("createTime", announcement.getCreateTime());
				request.setAttribute("content", announcement.getAnnounceBody());
				request.setAttribute("attachments", list);
			}
		} catch (Exception e) {
			logger.error("detail exception:" ,e);
		}
		return "announcementDetail";
	}
	
	/**
	 * 获取用户公告列表
	 * @param userId 用户id
	 * @param aid 公告id
	 * @param request request
	 * @param response response
	 * @return ResultInfo
	 */
	@RequestMapping(value = "getUnreadCount", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getUnreadCount(Long userId, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		try {
			if (userId != null){
				//获取未读公告
				int count = service.getUnreadCountByUserId(userId);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("count", count);
				info.setCode(IConstants.QT_CODE_OK);
				info.setData(count);
			}
		} catch (Exception e) {
			logger.error("getUnreadCount exception:" ,e);
			info.setCode(IConstants.QT_CODE_ERROR);
		}
		return info;
	}
	
}
