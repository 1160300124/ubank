package com.ulaiber.web.dao;

import com.ulaiber.web.model.LeaveRecord;

/**
 * 申请请假数据库持久层
 * Created by daiqingwen on 2017/8/21.
 */
public interface LeaveDao {
    int saveLeaveRecord(LeaveRecord leaveRecord); //申请请假
}
