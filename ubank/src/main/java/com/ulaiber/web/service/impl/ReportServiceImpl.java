package com.ulaiber.web.service.impl;

import com.ulaiber.web.dao.ReportDao;
import com.ulaiber.web.model.LeaveReportVO;
import com.ulaiber.web.model.LeaveReturnVO;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.ReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表业务层接口实现类
 * Created by daiqingwen on 2017/8/28.
 */
@Service
public class ReportServiceImpl extends BaseService implements ReportService {

    @Resource
    private ReportDao reportDao;

    @Override
    public int getLeaveCount(String sysflag, String groupNumber,int pageNum,int pageSize) {
        Map<String ,Object > map = new HashMap<String,Object>();
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        map.put("sysflag" , sysflag);
        map.put("groupNumber" , groupNumber);
        int total = reportDao.getLeaveCount(map);
        return total;
    }

    @Override
    public List<LeaveReturnVO> leaveQuery(LeaveReportVO leaveReportVO, String sysflag, String groupNumber,String[] comArr,int pageNum,int pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("sysflag" , sysflag);
        map.put("groupNumber" , groupNumber);
        map.put("companyNumber" , comArr);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        map.put("companyNum" , leaveReportVO.getCompanyNum());
        map.put("deptNum" , leaveReportVO.getDeptNum());
        map.put("startDate" , leaveReportVO.getStartDate());
        map.put("endDate" , leaveReportVO.getEndDate());
        map.put("groupNum" , leaveReportVO.getGroupNum());
        map.put("result" , leaveReportVO.getResult());
        map.put("username" , leaveReportVO.getUsername());
        map.put("status" , leaveReportVO.getStatus());
        return reportDao.leaveQuery(map);
    }

    @Override
    public List<Map<String,Object>> getUserById(String[] ids) {
        return reportDao.getUserById(ids);
    }
}
