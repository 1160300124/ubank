package com.ulaiber.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ulaiber.web.model.announcement.Announcement;
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
	 * @param limit
	 * @param offset
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
}
