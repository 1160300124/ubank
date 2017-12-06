package com.ulaiber.web.service;

import java.util.List;

import com.ulaiber.web.model.announcement.Announcement;
import com.ulaiber.web.model.announcement.UserOfAnnouncement;

/** 
 * 公告业务逻辑接口
 *
 * @author  huangguoqing
 * @date 创建时间：2017年12月5日 下午6:54:41
 * @version 1.0 
 * @since 
 */
public interface AnnouncementService {
	/**
	 * 保存公告
	 * @param announcement announcement
	 * @return boolean
	 */
	boolean save(Announcement announcement);
	
	/**
	 * 分页查询公告
	 * @param limit
	 * @param offset
	 * @return List<Announcement>
	 */
	List<Announcement> getAnnouncements(int limit, int offset, String companyId);
	
	/**
	 * 查询总条数
	 * @param companyId 公司id
	 * @return int
	 */
	int getTotalCount(String companyId);
	
	/**
	 * 根据aid获取公告
	 * @param aid 公告id
	 * @return Announcement
	 */
	Announcement getAnnouncementByAid(long aid);
	
	/**
	 * 根据aid删除公告
	 * @param aid 公告id
	 * @return boolean
	 */
	boolean deleteByAid(long aid);
	
	/**
	 * 批量插入用户公告关系表
	 * @param uoas
	 * @return boolean
	 */
	boolean batchInsertUserOfAnnouncement(List<UserOfAnnouncement> uoas);
	
	/**
	 * 根据用户id批量删除用户公告关系表
	 * @param userIds
	 * @return boolean
	 */
	boolean batchDeleteUserOfAnnouncement(List<Long> userIds);
	
	/**
	 * 根据aid删除用户公告关系表
	 * @param aid 公告id
	 * @return boolean 
	 */
	boolean deleteUserOfAnnouncementByAid(long aid);
	
	/**
	 * 根据aid查询用户公告关系表
	 * @param aid 公告id
	 * @return List<UserOfAnnouncement>
	 */
	List<UserOfAnnouncement> getUserIdsByRid(long aid);
}
