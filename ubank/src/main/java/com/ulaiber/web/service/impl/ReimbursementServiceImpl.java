package com.ulaiber.web.service.impl;

import com.ulaiber.web.dao.LeaveAuditDao;
import com.ulaiber.web.dao.ReimbursementDao;
import com.ulaiber.web.model.LeaveRecord;
import com.ulaiber.web.model.Reimbursement;
import com.ulaiber.web.model.ReimbursementVO;
import com.ulaiber.web.service.ReimbursementService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报销业务接口实现类
 * Created by daiqingwen on 2017/9/11.
 */
@Service
public class ReimbursementServiceImpl implements ReimbursementService{

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final Logger logger = Logger.getLogger(ReimbursementServiceImpl.class);

    @Resource
    private ReimbursementDao reimbursementDao;

    @Resource
    private LeaveAuditDao leaveAuditDao;

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int insert(ReimbursementVO vo) {
        Map<String,Object> map = new HashMap<>();
        map.put("auditor" , vo.getAuditor());
        map.put("userid" , vo.getUserid());
        map.put("reason" , vo.getReason());
        map.put("date",sdf.format(new Date()));
        //新增申请记录
        int result = reimbursementDao.insertRecord(map);
        if(result <=0){
            return 0;
        }
        //保存报销数据
        List<Reimbursement> list  = (List<Reimbursement>) vo.getData();
        LeaveRecord leaveRecord = new LeaveRecord();
        for (Reimbursement re : list){
            re.setRecordNo(leaveRecord.getId());
        }
        //新增报销记录
        int result2 = reimbursementDao.insertReim(list);
        if(result2 <=0){
            return 0;
        }
        //保存审批人
        String[] arr = vo.getAuditor().split(",");
        List<Map<String,Object>> auditorList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++){
            Map<String ,Object> auditorMap = new HashMap<>();
            auditorMap.put("userid",leaveRecord.getUserid());
            auditorMap.put("recordNo",String.valueOf(leaveRecord.getId()));
            auditorMap.put("auditor",arr[i]);
            auditorMap.put("auditDate",sdf.format(new Date()));
            auditorMap.put("sort",i+1);
            auditorList.add(auditorMap);
        }
        int result3= leaveAuditDao.saveAditor(auditorList);
        return result3;
    }
}