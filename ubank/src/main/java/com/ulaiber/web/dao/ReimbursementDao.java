package com.ulaiber.web.dao;

import com.ulaiber.web.model.LeaveRecord;
import com.ulaiber.web.model.Reimbursement;

import java.util.List;
import java.util.Map;

/**
 * 报销数据库持久层
 * Created by daiqingwen on 2017/9/11.
 */
public interface ReimbursementDao {

    int insertRecord(LeaveRecord leaveRecord);  //新增申请记录

    int insertReim(List<Reimbursement> list); //新增报销记录

    List<Reimbursement> queryReimbersement(int recordNo);  //根据申请记录ID，获取报销记录
}
