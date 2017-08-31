package com.ulaiber.web.dao;

import com.ulaiber.web.model.AuditVO;
import com.ulaiber.web.model.LeaveAudit;

import java.util.List;
import java.util.Map;

/**
 * 请假审批数据持久层
 * Created by daiqingwen on 2017/8/22.
 */
public interface LeaveAuditDao {

    int saveAditor(List<Map<String, Object>> list); //保存请假审批人

    List<LeaveAudit> queryAuditor(String[] ids); //查询审批人记录

    List<AuditVO> getLeaveAuditor(String userId); //获取个人所有审批记录

    List<LeaveAudit> getAuditorByUserId(String userId); //根据申请记录编号获取审批人

    List<LeaveAudit> getAuditorBySort(Map<String, Object> map); //根据排序号和申请记录编号获取审批人

    List<LeaveAudit> queryAuditorByRecord(String recordNo); //根据申请记录号查询待审批人记录

    List<LeaveAudit> queryAuditorByUserId(String userId); //根据申请记录编号获取已审批人

    int confirmAudit(Map<String, Object> map);  //确认审批
}
