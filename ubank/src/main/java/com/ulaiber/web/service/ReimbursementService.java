package com.ulaiber.web.service;

import com.ulaiber.web.model.ReimbursementVO;

/**
 * 报销业务接口
 * Created by daiqingwen on 2017/9/11.
 */
public interface ReimbursementService {

    int insert(ReimbursementVO vo);  //新增报销记录
}
