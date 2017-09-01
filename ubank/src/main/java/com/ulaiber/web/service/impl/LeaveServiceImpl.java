package com.ulaiber.web.service.impl;

import com.ulaiber.web.dao.LeaveAuditDao;
import com.ulaiber.web.dao.LeaveDao;
import com.ulaiber.web.model.*;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.LeaveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 申请请假业务实现类
 * Created by daiqingwen on 2017/8/21.
 */
@Service
public class LeaveServiceImpl extends BaseService implements LeaveService{

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private LeaveDao leaveDao;

    @Resource
    private LeaveAuditDao leaveAuditDao;

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int saveLeaveRecord(LeaveRecord leaveRecord) {
        int result = leaveDao.saveLeaveRecord(leaveRecord);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int saveAditor(LeaveRecord leaveRecord) {
        List<Map<String,Object>> list = new ArrayList<>();
        String[] auditor = leaveRecord.getAuditor().split(",");
        for (int i = 0; i < auditor.length; i++){
            Map<String ,Object> map = new HashMap<>();
            map.put("userid",leaveRecord.getUserid());
            map.put("recordNo",String.valueOf(leaveRecord.getId()));
            map.put("auditor",auditor[i]);
            map.put("auditDate",sdf.format(new Date()));
            map.put("sort",i+1);
            list.add(map);
        }
        int result = leaveAuditDao.saveAditor(list);
        return result;
    }

    @Override
    public List<ApplyForVO> queryApplyRecord(String userid) {
        List<ApplyForVO> list = leaveDao.queryApplyRecord(userid);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int cancelApply(String applyId) {
        int result = leaveDao.cancelApply(applyId);
        int result2 = leaveDao.cancelApplyAudit(applyId);
        return result2;
    }

    @Override
    public List<LeaveAudit> queryAuditor(String[] ids) {
        return leaveAuditDao.queryAuditor(ids);
    }

    @Override
    public List<Map<String,Object>> getLeaveRecord(String userId) {
        return leaveDao.getLeaveRecord(userId);
    }

    @Override
    public List<AuditVO> getLeaveAuditor(String userId) {
        return leaveAuditDao.getLeaveAuditor(userId);
    }

    @Override
    public List<Map<String, Object>> getPendingRecord(String userId) {
        return leaveDao.getPendingRecord(userId);
    }

    @Override
    public List<Map<String, Object>> getAlreadyRecord(String userId) {
        return leaveDao.getAlreadyRecord(userId);
    }

    @Override
    public ApplyForVO queryPeningRecord(int id,String userId) {
        Map<String , Object> map = new HashMap<>();
        map.put("id" , id);
        map.put("userId" , userId);
        return leaveDao.queryPeningRecord(map);
    }

    @Override
    public ApplyForVO queryAlreadRecord(int id,String userId) {
        Map<String , Object> map = new HashMap<>();
        map.put("id" , id);
        map.put("userId" , userId);
        return leaveDao.queryAlreadRecord(map);
    }

    @Override
    public List<LeaveAudit> getAuditorByUserId( String userId,int pageNum,int pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId" ,userId);
        map.put("pageNum" ,pageNum);
        map.put("pageSize" ,pageSize);
        return leaveAuditDao.getAuditorByUserId(map);
    }

    @Override
    public List<LeaveAudit> getAuditorBySort(String recordNo, int sortValue) {
        Map<String,Object> map = new HashMap<>();
        map.put("recordNo" , recordNo);
        map.put("sort" , sortValue);
        return leaveAuditDao.getAuditorBySort(map);
    }

    @Override
    public List<AuditVO> queryAuditorByRecord(String recordNo) {
        return leaveAuditDao.queryAuditorByRecord(recordNo);
    }

    @Override
    public List<LeaveAudit> queryAuditorByUserId(String userId,int pageNum,int pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId" ,userId);
        map.put("pageNum" ,pageNum);
        map.put("pageSize" ,pageSize);
        return leaveAuditDao.queryAuditorByUserId(map);
    }

    @Override
    public int confirmAudit(String userId, String recordNo, String status,String reason) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId" , userId);
        map.put("recordNo" , recordNo);
        map.put("status" , status);
        map.put("reason",reason);
        map.put("date",sdf.format(new Date()));
        return leaveAuditDao.confirmAudit(map);
    }

    @Override
    public int updateRecord(String recordNo,String status) {
        Map<String,Object> map = new HashMap<>();
        map.put("recordNo" , recordNo);
        map.put("status" , status);
        return leaveDao.updateRecord(map);
    }

    @Override
    public Map<String,Object> queryApplyRecordById(int id) {
        return leaveDao.queryApplyRecordById(id);
    }

    @Override
    public int getUserTotalByDate(String date) {
        return leaveDao.getUserTotalByDate(date);
    }

    @Override
    public List<User> getUserByDate(String date,int pageNum,int pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("date" , date);
        map.put("pageNum" , pageNum);
        map.put("pageSize" , pageSize);
        return leaveDao.getUserByDate(map);
    }


}
