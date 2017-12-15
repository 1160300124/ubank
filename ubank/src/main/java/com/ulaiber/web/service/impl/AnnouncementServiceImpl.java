package com.ulaiber.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.dao.AnnouncementDao;
import com.ulaiber.web.dao.UserDao;
import com.ulaiber.web.model.announcement.Announcement;
import com.ulaiber.web.model.announcement.Attachment;
import com.ulaiber.web.model.announcement.UserOfAnnouncement;
import com.ulaiber.web.service.AnnouncementService;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.utils.DateTimeUtil;
import com.ulaiber.web.utils.PushtoSingle;

/** 
 * 公告业务逻辑实现类
 *
 * @author  huangguoqing
 * @date 创建时间：2017年12月5日 下午6:59:30
 * @version 1.0 
 * @since 
 */
@Service
public class AnnouncementServiceImpl extends BaseService implements AnnouncementService{
	
	@Resource
	private AnnouncementDao dao;
	
    @Resource
    private UserDao userDao;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(Announcement announcement) {
		announcement.setCreateTime(DateTimeUtil.date2Str(new Date()));
		List<UserOfAnnouncement> list = new ArrayList<UserOfAnnouncement>();
		if (dao.save(announcement) > 0){
			for (long userId : announcement.getUserIds()){
				UserOfAnnouncement uoa = new UserOfAnnouncement();
				uoa.setAid(announcement.getAid());
				uoa.setUserId(userId);
				list.add(uoa);
			}
			
			if (announcement.getAttachments().size() > 0){
				for (Attachment att : announcement.getAttachments()){
					att.setAid(announcement.getAid());
				}
				dao.batchInsertAttachments(announcement.getAttachments());
			}
			return  dao.batchInsertUserOfAnnouncement(list) > 0;
		}
		return false;
	}
	
	@Override
	public void send(List<Long> userIds, long aid, String title) throws Exception {
		List<String> CIDs = userDao.queryCIDsByIds(userIds);
		for (String CID : CIDs){
			if(StringUtils.isNotEmpty(CID)){
				PushtoSingle.singlePush(CID, IConstants.NOTICE, "", title, aid, "");
			}
		}
		
	}

	@Override
	public List<Announcement> getAnnouncements(int limit, int offset, String companyId) {
		return dao.getAnnouncements(limit, offset, companyId);
	}

	@Override
	public int getTotalCount(String companyId) {
		return dao.getTotalCount(companyId);
	}

	@Override
	public Announcement getAnnouncementByAid(long aid) {
		return dao.getAnnouncementByAid(aid);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean deleteByAid(long aid) {
		if (dao.deleteByAid(aid) > 0){
			 dao.deleteUserOfAnnouncementByAid(aid);
			 return dao.deleteAttachmentsByAid(aid) > 0;
		}
		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean batchInsertUserOfAnnouncement(List<UserOfAnnouncement> uoas) {
		return dao.batchInsertUserOfAnnouncement(uoas) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean batchDeleteUserOfAnnouncement(List<Long> userIds) {
		return dao.batchDeleteUserOfAnnouncement(userIds) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean deleteUserOfAnnouncementByAid(long aid) {
		return dao.deleteUserOfAnnouncementByAid(aid) > 0;
	}

	@Override
	public List<UserOfAnnouncement> getUserIdsByRid(long aid) {
		return dao.getUserIdsByRid(aid);
	}

	@Override
	public List<Map<String, Object>> getAnnouncementsByUserId(long userId, int pageSize, int pageNum) {
		int offset = pageSize * (pageNum -1);
		return dao.getAnnouncementsByUserId(userId, pageSize, offset);
	}

	@Override
	public int getTotalCountByUserId(long userId) {
		return dao.getTotalCountByUserId(userId);
	}

	@Override
	public List<Map<String, Object>> getAttachmentCountByUserId(long userId) {
		return dao.getAttachmentCountByUserId(userId);
	}

	@Override
	public List<Attachment> getAttachmentsByAid(long aid) {
		return dao.getAttachmentsByAid(aid);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean updateTypeByUserIdAndAid(long userId, long aid) {
		return dao.updateTypeByUserIdAndAid(userId, aid);
	}

	@Override
	public int getUnreadCountByUserId(long userId) {
		return dao.getUnreadCountByUserId(userId);
	}

}
