package com.ulaiber.web.service;

import com.ulaiber.web.model.*;

import java.util.List;
import java.util.Map;

/**
 * 报表业务层接口
 * Created by daiqingwen on 2017/8/28.
 */
public interface ReportService {

    int getLeaveCount(String sysflag, String groupNumber,int pageNum,int pageSize); //获取申请记录总数

    // 申请记录查询
    List<LeaveReturnVO> leaveQuery(LeaveReportVO leaveReportVO, String sysflag, String groupNumber,String[] comArr,int pageNum,int pageSize);

    List<Map<String,Object>> getUserById(String[] ids); //根据用户ID获取用户名

    int getReimCount(String sysflag, String groupNumber, int pageNum, int pageSize); //获取报销记录总数

    //报销记录查询
    List<ReimReportVO> reimQuery(LeaveReportVO leaveReportVO, String sysflag, String groupNumber, String[] comArr, int pageNum, int pageSize);

    List<Reimbursement> getReimRecordById(int[] ids); //根据申请记录ID，获取报销详情

    List<Reimbursement> getReimDetailsById(int id);   //根据申请记录ID获取报销记录
}
