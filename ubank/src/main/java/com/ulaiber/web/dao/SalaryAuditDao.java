package com.ulaiber.web.dao;

import com.ulaiber.web.model.LeaveRecord;
import com.ulaiber.web.model.SalaryRecord;

import java.util.List;

/**
 * 工资审批数据库持久层
 * Created by daiqingwen on 2017/9/21.
 */
public interface SalaryAuditDao {

    int insertRecord(LeaveRecord leaveRecord); //新增申请记录

    int insertSalaryRecord(List<SalaryRecord> paramList);  //新增工资审批记录

    List<SalaryRecord> querySalaryByRecordNo(int recordNo);  //根据申请记录ID，获取工资发放审批记录
}
