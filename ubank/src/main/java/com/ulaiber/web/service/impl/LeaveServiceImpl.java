package com.ulaiber.web.service.impl;

import com.ulaiber.web.dao.LeaveAuditDao;
import com.ulaiber.web.dao.LeaveDao;
import com.ulaiber.web.model.ApplyForVO;
import com.ulaiber.web.model.AuditVO;
import com.ulaiber.web.model.LeaveAudit;
import com.ulaiber.web.model.LeaveRecord;
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

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

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
            list.add(map);
        }
        int result = leaveAuditDao.saveAditor(list);
        return result;
    }

    @Override
    public List<LeaveRecord> queryApplyRecord(String userid) {
        List<LeaveRecord> list = leaveDao.queryApplyRecord(userid);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int cancelApply(String applyId) {
        int result = leaveDao.cancelApply(applyId);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int cancelApplyAudit(String applyId) {
        return leaveAuditDao.cancelApplyAudit(applyId);
    }

    @Override
    public List<LeaveAudit> queryAuditor(String[] ids) {
        return leaveAuditDao.queryAuditor(ids);
    }

    @Override
    public List<ApplyForVO> getLeaveRecord(String userId) {
        return leaveDao.getLeaveRecord(userId);
    }

    @Override
    public List<AuditVO> getLeaveAuditor(String userId) {
        return leaveAuditDao.getLeaveAuditor(userId);
    }
}
