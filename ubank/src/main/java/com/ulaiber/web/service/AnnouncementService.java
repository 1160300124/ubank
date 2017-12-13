package com.ulaiber.web.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ulaiber.web.model.announcement.Announcement;
import com.ulaiber.web.model.announcement.Attachment;
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
	 * 推送消息
	 * @param userIds 用户id集合
	 * @param aid   公告id
	 * @param title 标题
	 * @throws Exception 
	 */
	void send(List<Long> userIds, long aid, String title) throws Exception;
	
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

	/**
	 * 根据用户id分页查询公告(TO APP)
	 * @param userId 用户id
	 * @param limit 每页多少条
	 * @param offset 从第几条开始
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> getAnnouncementsByUserId(long userId, int pageSize, int pageNum);
	
	/**
	 * 根据用户id查询公告总数(TO APP)
	 * @param userId 用户id
	 * @return int
	 */
	int getTotalCountByUserId(long userId);
	
	/**
	 * 根据用户id分组查询每个公告的附件个数
	 * @param userId 用户id
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> getAttachmentCountByUserId(long userId);
	
	/**
	 * 根据aid查询公告附件
	 * @param aid 公告id
	 * @return List<Attachment>
	 */
	List<Attachment> getAttachmentsByAid(long aid);
	
	/**
	 * 更新用户公告为已读
	 * @param userId 用户id
	 * @param aid 公告id
	 * @return boolean
	 */
	boolean updateTypeByUserIdAndRid(@Param("userId") long userId, @Param("aid") long aid);
	
	/**
	 * 获取用户未读公告条数
	 * @param userId 用户id
	 * @return int
	 */
	int getUnreadCountByUserId(long userId);
}
