package com.ulaiber.web.service.impl;

import com.ulaiber.web.dao.LeaveAuditDao;
import com.ulaiber.web.dao.LeaveDao;
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
}
