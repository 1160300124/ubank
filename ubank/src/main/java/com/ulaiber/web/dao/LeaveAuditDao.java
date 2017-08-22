package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

/**
 * 请假审批数据持久层
 * Created by daiqingwen on 2017/8/22.
 */
public interface LeaveAuditDao {

    int saveAditor(List<Map<String, Object>> list); //保存请假审批人
}
