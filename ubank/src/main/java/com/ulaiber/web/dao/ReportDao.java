package com.ulaiber.web.dao;

import com.ulaiber.web.model.LeaveReturnVO;
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
}
