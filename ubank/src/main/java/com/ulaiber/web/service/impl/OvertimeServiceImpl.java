package com.ulaiber.web.service.impl;

import com.ulaiber.web.dao.LeaveAuditDao;
import com.ulaiber.web.dao.LeaveDao;
import com.ulaiber.web.dao.OvertimeDao;
import com.ulaiber.web.model.ApplyForVO;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.OvertimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 加班业务层接口实现类
 * Created by daiqingwen on 2017/8/26.
 */
@Service
public class OvertimeServiceImpl extends BaseService implements OvertimeService{

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private OvertimeDao overtimeDao;

    @Resource
    private LeaveAuditDao leaveAuditDao;

    @Resource
    private LeaveDao leaveDao;

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int addOvertimeRecord(ApplyForVO applyForVO) {
        int result = overtimeDao.addRecord(applyForVO);  //新增申请记录
        applyForVO.setId(applyForVO.getId());
        int result2 = overtimeDao.addOvertimeRecord(applyForVO);  //新增加班申请记录
        List<Map<String,Object>> list = new ArrayList<>();
        String[] auditor = applyForVO.getAuditor().split(",");
        for (int i = 0; i < auditor.length; i++){
            Map<String ,Object> map = new HashMap<>();
            map.put("userid",applyForVO.getUserid());
            map.put("recordNo",String.valueOf(applyForVO.getId()));
            map.put("auditor",auditor[i]);
            map.put("auditDate",sdf.format(new Date()));
            map.put("sort",i+1);
            list.add(map);
        }
        int result3 = leaveAuditDao.saveAditor(list);
        return result3;
    }


}
