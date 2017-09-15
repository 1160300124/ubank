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

    List<LeaveAudit> queryAuditor(int[] ids); //查询审批人记录

    List<AuditVO> getLeaveAuditor(String userId); //获取个人所有审批记录

    List<LeaveAudit> getAuditorByUserId(Map<String, Object> map); //根据申请记录编号获取审批人

    List<LeaveAudit> getAuditorBySort(Map<String, Object> map); //根据排序号和申请记录编号获取审批人

    List<AuditVO> queryAuditorByRecord(String recordNo); //根据申请记录号查询待审批人记录

    List<LeaveAudit> queryAuditorByUserId(Map<String, Object> map); //根据申请记录编号获取已审批人

    int confirmAudit(Map<String, Object> map);  //确认审批

    Map<String,Object> queryCIDByUserid(int userid); //查询用户个推CID

    String queryUserIdByRecordNo(Map<String, Object> map); //获取当前被审批记录的用户ID

    List<Map<String,Object>> queryCIDByIds(int[] ids); //根据多个id获取CID
}
