package com.ulaiber.web.service;

import com.ulaiber.web.model.SalaryAuditVO;
import com.ulaiber.web.model.SalaryRecord;

import java.util.List;

/**
 * 工资审批业务接口
 * Created by daiqingwen on 2017/9/21.
 */
public interface SalaryAuditService {

    int salaryAudit(SalaryAuditVO salaryAuditVO);

    List<SalaryRecord> querySalaryByRecordNo(int recordNo);//根据申请记录ID，获取工资发放审批记录
}
