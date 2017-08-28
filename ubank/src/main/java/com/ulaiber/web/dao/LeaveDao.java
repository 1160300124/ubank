package com.ulaiber.web.dao;

import com.ulaiber.web.model.ApplyForVO;
import com.ulaiber.web.model.LeaveRecord;

import java.util.List;
import java.util.Map;

/**
 * 申请请假数据库持久层
 * Created by daiqingwen on 2017/8/21.
 */
public interface LeaveDao {
    int saveLeaveRecord(LeaveRecord leaveRecord); //申请请假

    List<LeaveRecord> queryApplyRecord(String userid); // 查询个人申请记录

    int cancelApply(String applyId); //取消请假申请

    List<ApplyForVO> getLeaveRecord(String userId); //查询工作提醒

    List<Map<String,Object>> getPendingRecord(String userId); //获取待审批记录

    List<Map<String,Object>> getAlreadyRecord(String userId); //获取已审批记录

    ApplyForVO queryPeningRecord(int id); //获取待审批记录

    ApplyForVO queryAlreadRecord(int id);  //获取已审批记录

    int updateRecord(Map<String,Object> map); //更新申请记录为最新的状态
}
