package com.ulaiber.web.service;

import com.ulaiber.web.model.ApplyForVO;
import com.ulaiber.web.model.AuditVO;
import com.ulaiber.web.model.LeaveAudit;
import com.ulaiber.web.model.LeaveRecord;

import java.util.List;
import java.util.Map;

/**
 * 申请请假业务层
 * Created by daiqingwen on 2017/8/21.
 */
public interface LeaveService {
    int saveLeaveRecord(LeaveRecord leaveRecord); //保存申请请假

    int saveAditor(LeaveRecord leaveRecord);  //保存请假审批人

    List<LeaveRecord> queryApplyRecord(String userid);   //查询个人申请记录

    int cancelApply(String applyId); //取消请假申请

    int cancelApplyAudit(String applyId); //取消请假审批人

    List<LeaveAudit> queryAuditor(String[] ids); //查询审批人记录

    List<ApplyForVO> getLeaveRecord(String userId);  //查询工作提醒

    List<AuditVO> getLeaveAuditor(String userId); //获取个人所有审批记录
}
