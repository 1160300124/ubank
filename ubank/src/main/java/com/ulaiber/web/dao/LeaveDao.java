package com.ulaiber.web.dao;

import com.ulaiber.web.model.ApplyForVO;
import com.ulaiber.web.model.LeaveRecord;
import com.ulaiber.web.model.User;

import java.util.List;
import java.util.Map;

/**
 * 申请请假数据库持久层
 * Created by daiqingwen on 2017/8/21.
 */
public interface LeaveDao {
    int saveLeaveRecord(LeaveRecord leaveRecord); //申请请假

    List<ApplyForVO> queryApplyRecord(String userid); // 查询个人申请记录

    int cancelApply(String applyId); //取消请假申请

    int cancelApplyAudit(String applyId);  //取消请假审批人

    List<Map<String,Object>> getLeaveRecord(String userId); //查询工作提醒

    List<Map<String, Object>> getPendingRecord(String userId); //获取待审批记录

    List<Map<String, Object>> getAlreadyRecord(String userId); //获取已审批记录

    ApplyForVO queryPeningRecord(int id); //获取待审批记录

    ApplyForVO queryAlreadRecord(int id);  //获取已审批记录

    int updateRecord(Map<String, Object> map); //更新申请记录为最新的状态

    Map<String,Object> queryApplyRecordById(int id);  //根据申请记录ID获取申请记录


}
