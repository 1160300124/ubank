package com.ulaiber.web.service.impl;

import com.ulaiber.web.dao.LeaveDao;
import com.ulaiber.web.dao.OvertimeDao;
import com.ulaiber.web.model.ApplyForVO;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.OvertimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 加班业务层接口实现类
 * Created by daiqingwen on 2017/8/26.
 */
@Service
public class OvertimeServiceImpl extends BaseService implements OvertimeService{

    @Resource
    private OvertimeDao overtimeDao;

    @Resource
    private LeaveDao leaveDao;

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int addOvertimeRecord(ApplyForVO applyForVO) {
        int result = overtimeDao.addRecord(applyForVO);
        applyForVO.setId(applyForVO.getId());
        int result2 = overtimeDao.addOvertimeRecord(applyForVO);
        return result2;
    }


}
