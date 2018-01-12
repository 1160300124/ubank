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

    int getUserTotalByDate(String date, String companyNumber);  //根据日期查询用户

    List<User> getUserByDate(String date, String companyNumber, int pageNum, int pageSize);  //根据日期分页查询新增用户

    int updateUser(String userId, String CID); //修改用户个推CID

    int insertRemedy(Remedy remedy); //新增补卡记录

    Remedy getRemedyRecordByUserId(int recordNo); //根据记录Id查询补卡信息
    
    List<Map<String, Object>> getTotalTimeByCompanyNumAndMonth(String companyNum, String type, String month); //获取某个公司某个月份所有人的审批通过的请假或加班的总时长 
    
    LeaveRecord getLeaveRecordByUserIdAndDate(long userId, String date); //查询用户指定日期是否有审批通过的请假记录
    
    LeaveRecord getLeaveRecordByMobileAndDate(String mobile, String date); //查询用户指定日期是否有审批通过的请假记录

    LeaveRecord queryApplyStatus(String recordNo);  //根据审批状态获取申请记录状态

    int getLeaveRecordCount(long userId); //获取个人申请记录数量

    int getLeaveAuditorCount(long userId);  //获取个人审批记录数量

    List<User> getDeleteUserByDate(String date, String companyNumber, int pageNum, int pageSize); //根据日期分页查询删除用户
}
