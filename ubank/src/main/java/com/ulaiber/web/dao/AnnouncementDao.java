package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ulaiber.web.model.announcement.Announcement;
import com.ulaiber.web.model.announcement.Attachment;
import com.ulaiber.web.model.announcement.UserOfAnnouncement;

/** 
 * 公告数据库接口
 *
 * @author  huangguoqing
 * @date 创建时间：2017年12月5日 下午6:26:40
 * @version 1.0 
 * @since 
 */
public interface AnnouncementDao {
	
	/**
	 * 保存公告
	 * @param announcement announcement
	 * @return int
	 */
	int save(Announcement announcement);
	
	/**
	 * 分页查询公告
	 * @param limit 每页多少条
	 * @param offset 从第几条开始
	 * @return List<Announcement>
	 */
	List<Announcement> getAnnouncements(@Param("limit") int limit, @Param("offset") int offset, @Param("companyId") String companyId);
	
	/**
	 * 查询总条数
	 * @param companyId 公司id
	 * @return int
	 */
	int getTotalCount(@Param("companyId") String companyId);
	
	/**
	 * 根据aid获取公告
	 * @param aid 公告id
	 * @return Announcement
	 */
	Announcement getAnnouncementByAid(long aid);
	
	/**
	 * 根据aid删除公告
	 * @param aid 公告id
	 * @return int
	 */
	int deleteByAid(long aid);
	
	/**
	 * 批量插入用户公告关系表
	 * @param uoas
	 * @return int
	 */
	int batchInsertUserOfAnnouncement(List<UserOfAnnouncement> uoas);
	
	/**
	 * 根据用户id批量删除用户公告关系表
	 * @param userIds
	 * @return int
	 */
	int batchDeleteUserOfAnnouncement(List<Long> userIds);
	
	/**
	 * 根据aid删除用户公告关系表
	 * @param aid 公告id
	 * @return int 
	 */
	int deleteUserOfAnnouncementByAid(long aid);
	
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
	List<Map<String, Object>> getAnnouncementsByUserId(@Param("userId") long userId, @Param("limit") int limit, @Param("offset") int offset);
	
	/**
	 * 根据用户id查询公告总数
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
}
