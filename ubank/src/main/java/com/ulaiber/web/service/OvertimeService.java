package com.ulaiber.web.service;

import com.ulaiber.web.model.ApplyForVO;

/**
 * 加班业务层接口
 * Created by daiqingwen on 2017/8/26.
 */
public interface OvertimeService {
    int addOvertimeRecord(ApplyForVO applyForVO); //新增加班记录信息

}
