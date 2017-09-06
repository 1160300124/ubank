package com.ulaiber.web.dao;

import com.ulaiber.web.model.ApplyForVO;

/**
 * 加班数据库持久层
 * Created by daiqingwen on 2017/8/26.
 */
public interface OvertimeDao {

    int addOvertimeRecord(ApplyForVO applyForVO); //新增加班申请记录

    int addRecord(ApplyForVO applyForVO);//新增申请记录
}
