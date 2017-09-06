package com.ulaiber.web.service;

import com.ulaiber.web.model.LeaveReportVO;
import com.ulaiber.web.model.LeaveReturnVO;
import com.ulaiber.web.model.User;

import java.util.List;
import java.util.Map;

/**
 * 报表业务层接口
 * Created by daiqingwen on 2017/8/28.
 */
public interface ReportService {

    int getLeaveCount(String sysflag, String groupNumber,int pageNum,int pageSize); //获取申请记录总数

    List<LeaveReturnVO> leaveQuery(LeaveReportVO leaveReportVO, String sysflag, String groupNumber,String[] comArr,int pageNum,int pageSize); // 申请记录查询

    List<Map<String,Object>> getUserById(String[] ids); //根据用户ID获取用户名
}
