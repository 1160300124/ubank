package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ulaiber.web.model.ApplyForVO;
import com.ulaiber.web.model.LeaveRecord;
import com.ulaiber.web.model.Remedy;
import com.ulaiber.web.model.User;

/**
 * 申请请假数据库持久层
 * Created by daiqingwen on 2017/8/21.
 */
public interface LeaveDao {
    int saveLeaveRecord(LeaveRecord leaveRecord); //申请请假

    List<ApplyForVO> queryApplyRecord(Map<String, Object> map); // 查询个人申请记录

    int cancelApply(String applyId); //取消请假申请

    int cancelApplyAudit(String applyId);  //取消请假审批人

    List<Map<String,Object>> getLeaveRecord(String userId); //查询工作提醒

    List<Map<String, Object>> getPendingRecord(String userId); //获取待审批记录

    List<Map<String, Object>> getAlreadyRecord(String userId); //获取已审批记录

    ApplyForVO queryPeningRecord(Map<String, Object> map); //获取待审批记录

    List<ApplyForVO> queryAlreadRecord(int[] ids);  //获取已审批记录

    int updateRecord(Map<String, Object> map); //更新申请记录为最新的状态

    Map<String,Object> queryApplyRecordById(int id);  //根据申请记录ID获取申请记录

    int getUserTotalByDate(Map<String, Object> map);  //根据日期查询用户

    List<User> getUserByDate(Map<String, Object> map);  //根据日期分页查询用户

    int updateUser(Map<String, Object> map); //修改用户个推CID

    int insertRemedyRecord(LeaveRecord leaveRecord); //新增申请记录

    int addRemedy(Remedy remedy);  //新增补卡信息

    Remedy getRemedyRecordByUserId(int recordNo); //根据记录Id查询补卡信息
    
    List<Map<String, Object>> getTotalTimeByCompanyNumAndMonth(Map<String, Object> params); //获取某个公司某个月份所有人的审批通过的请假或加班的总时长
    
    int updateRealLeaveTime(@Param("userId") long userId,@Param("time") double time, @Param("today") String date);//更新实际请假时长
    
    LeaveRecord getLeaveRecordByUserIdAndDate(@Param("userId") long userId, @Param("today") String date); //查询用户指定日期是否有审批通过的请假记录
    
    LeaveRecord getLeaveRecordByMobileAndDate(@Param("mobile") String mobile, @Param("today") String date); //查询用户指定日期是否有审批通过的请假记录

    LeaveRecord queryApplyStatus(int recordNo); //根据审批状态获取申请记录状态
}
