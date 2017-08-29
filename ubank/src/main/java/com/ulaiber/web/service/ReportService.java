package com.ulaiber.web.service;

import com.ulaiber.web.model.LeaveReportVO;
import com.ulaiber.web.model.LeaveReturnVO;

import java.util.List;

/**
 * 报表业务层接口
 * Created by daiqingwen on 2017/8/28.
 */
public interface ReportService {

    int getLeaveCount(String sysflag, String groupNumber); //获取申请记录总数

    List<LeaveReturnVO> leaveQuery(LeaveReportVO leaveReportVO, String sysflag, String groupNumber,String[] comArr); // 申请记录查询
}
