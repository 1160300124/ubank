package com.ulaiber.web.dao;

import com.ulaiber.web.model.LeaveReturnVO;
import com.ulaiber.web.model.ReimReportVO;
import com.ulaiber.web.model.Reimbursement;
import com.ulaiber.web.model.User;

import java.util.List;
import java.util.Map;

/**
 * 报表数据持久层接口
 * Created by daiqingwen on 2017/8/28.
 */
public interface ReportDao {

    int getLeaveCount(Map<String, Object> map);  //获取申请记录总数

    List<LeaveReturnVO> leaveQuery(Map<String, Object> map);   //申请记录查询

    List<Map<String,Object>> getUserById(String[] ids);  //根据用户ID获取用户名

    int getReimCount(Map<String, Object> map); //获取报销记录总数

    List<ReimReportVO> reimQuery(Map<String, Object> map); //报销记录查询

    List<Reimbursement> getReimRecordById(int[] ids); //根据申请记录ID，获取报销详情

    List<Reimbursement> getReimDetailsById(int id);  //根据申请记录ID获取报销记录

    /**
     * 导出请假报表查询
     * @param map
     * @return LeaveReturnVO
     */
    List<LeaveReturnVO> reportQuery(Map<String, Object> map);

    /**
     * 导出报销报表查询
     * @param map
     * @return ReimReportVO
     */
    List<ReimReportVO> reimReportQuery(Map<String, Object> map);
}
