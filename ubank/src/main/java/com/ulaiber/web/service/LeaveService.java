package com.ulaiber.web.service;

import com.ulaiber.web.model.LeaveRecord;

/**
 * 申请请假业务层
 * Created by daiqingwen on 2017/8/21.
 */
public interface LeaveService {
    int saveLeaveRecord(LeaveRecord leaveRecord); //保存申请请假


    int saveAditor(LeaveRecord leaveRecord);  //保存请假审批人
}
