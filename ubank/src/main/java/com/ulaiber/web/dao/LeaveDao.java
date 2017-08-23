package com.ulaiber.web.dao;

import com.ulaiber.web.model.ApplyForVO;
import com.ulaiber.web.model.LeaveRecord;

import java.util.List;

/**
 * 申请请假数据库持久层
 * Created by daiqingwen on 2017/8/21.
 */
public interface LeaveDao {
    int saveLeaveRecord(LeaveRecord leaveRecord); //申请请假

    List<LeaveRecord> queryApplyRecord(String userid); // 查询个人申请记录

    int cancelApply(String applyId); //取消请假申请

    List<ApplyForVO> getLeaveRecord(String userId); //查询工作提醒
}
