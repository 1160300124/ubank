package com.ulaiber.web.service.impl;

import com.ulaiber.web.controller.backend.SalaryAuditController;
import com.ulaiber.web.dao.LeaveAuditDao;
import com.ulaiber.web.dao.SalaryAuditDao;
import com.ulaiber.web.model.LeaveRecord;
import com.ulaiber.web.model.SalaryAuditVO;
import com.ulaiber.web.model.SalaryRecord;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.SalaryAuditService;
import com.ulaiber.web.utils.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工资审批业务接口实现类
 * Created by daiqingwen on 2017/9/21.
 */

@Service
public class SalaryAuditServiceImpl extends BaseService implements SalaryAuditService{

    public static final Logger logger = Logger.getLogger(SalaryAuditServiceImpl.class);

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private SalaryAuditDao salaryAuditDao;

    @Resource
    private LeaveAuditDao leaveAuditDao;

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int salaryAudit(SalaryAuditVO salaryAuditVO) {
        LeaveRecord leaveRecord = new LeaveRecord();
        leaveRecord.setAuditor(salaryAuditVO.getAuditor());
        leaveRecord.setUserid(salaryAuditVO.getUserId());
        leaveRecord.setReason(salaryAuditVO.getReason());
        leaveRecord.setCreateDate(sdf.format(new Date()));
        //新增申请记录
        int result = salaryAuditDao.insertRecord(leaveRecord);
        if(result <= 0){
            logger.info(">>>>>>>>>>>插入申请记录失败");
            return 0;
        }

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
        //新增审批人
        int result2 = leaveAuditDao.saveAditor(list);
        if(result2 <= 0){
            logger.info(">>>>>>>>>>>>>插入审批人失败");
            return 0;
        }
        String[] salaryArr = salaryAuditVO.getSalary().split(",");
        List<SalaryRecord> paramList = new ArrayList<>();
        for (int i = 0 ; i < salaryArr.length ; i++){
            String[] salary = salaryArr[i].split(":");
            SalaryRecord vo = new SalaryRecord();
            vo.setRecordNo(leaveRecord.getId());
            vo.setUsername(salary[0]);
            vo.setSalary(Double.parseDouble(salary[1]));
            paramList.add(vo);
        }
        //新增工资审批记录
        int result3 = salaryAuditDao.insertSalaryRecord(paramList);
        if(result3 <= 0){
            logger.info(">>>>>>>>>>>>插入工资审批记录失败");
        }
        int userid = Integer.parseInt(auditor[0]);
        String reason = leaveRecord.getReason(); //原因/备注
        String mark = "0";  //申请类型
        Map<String,Object> map2 = leaveAuditDao.queryCIDByUserid(userid);  //查询用户个推CID
        StringUtil.sendMessage(map2,reason,mark); //消息推送
        return result3;
    }

    @Override
    public List<SalaryRecord> querySalaryByRecordNo(int recordNo) {
        return salaryAuditDao.querySalaryByRecordNo(recordNo);
    }
}
