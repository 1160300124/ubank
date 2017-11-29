package com.ulaiber.web.service;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.SalaryAuditVO;
import com.ulaiber.web.model.SalaryRecord;

/**
 * 工资审批业务接口
 * Created by daiqingwen on 2017/9/21.
 */
public interface SalaryAuditService {

	/**
	 * 新增工资审批记录
	 * @param salaryAuditVO
	 * @return
	 */
    int salaryAudit(SalaryAuditVO salaryAuditVO);

    /**
     * 根据申请记录ID，获取工资发放审批记录
     * @param recordNo 申请记录ID
     * @return
     */
    SalaryRecord querySalaryByRecordNo(int recordNo);
    
    /**
     * 根据申请记录ID，获取工资发放详情
     * @param recordNo
     * @return
     */
    List<Map<String, Object>>getSalaryDetailByRecordNo(int recordNo);
}
