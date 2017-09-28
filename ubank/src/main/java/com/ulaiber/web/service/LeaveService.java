package com.ulaiber.web.service;

import com.ulaiber.web.model.*;

import java.util.List;
import java.util.Map;

/**
 * 申请请假业务层
 * Created by daiqingwen on 2017/8/21.
 */
public interface LeaveService {
    int saveLeaveRecord(LeaveRecord leaveRecord); //保存申请请假

    int saveAditor(LeaveRecord leaveRecord);  //保存请假审批人

    List<ApplyForVO> queryApplyRecord(String userId,int pageNum,int pageSize);   //查询个人申请记录

    int cancelApply(String applyId); //取消请假申请

    List<LeaveAudit> queryAuditor(int[] ids); //查询审批人记录

    List<Map<String,Object>> getLeaveRecord(String userId);  //查询工作提醒

    List<AuditVO> getLeaveAuditor(String userId); //获取个人所有审批记录

    List<Map<String,Object>> getPendingRecord(String userId);   //获取待审批记录

    List<Map<String,Object>> getAlreadyRecord(String userId);   //获取已审批记录

    ApplyForVO queryPeningRecord(int id,String userId); //获取待审批记录

    List<ApplyForVO> queryAlreadRecord(int[] ids); //获取已审批记录

    List<LeaveAudit> getAuditorByUserId(String userId,int pageNum,int pageSize); //根据申请记录编号获取审批人

    List<LeaveAudit> getAuditorBySort(String recordNo, int sortValue); //根据排序号和申请记录编号获取审批人

    List<AuditVO> queryAuditorByRecord(String recordNo);  //根据申请记录号查询待审批人记录

    List<LeaveAudit> queryAuditorByUserId(String userId,int pageNum,int pageSize);  //根据申请记录编号获取已审批人

    int confirmAudit(AuditData auditData);  //确认审批

    int updateRecord(AuditData auditData);  //更新申请记录为最新的状态

    Map<String,Object> queryApplyRecordById(int id);  //根据申请记录ID获取申请记录

    int getUserTotalByDate(String date);  //根据日期查询用户

    List<User> getUserByDate(String date,int pageNum,int pageSize);  //根据日期分页查询用户

    int updateUser(String userId, String CID); //修改用户个推CID

    int insertRemedy(Remedy remedy); //新增补卡记录

}
