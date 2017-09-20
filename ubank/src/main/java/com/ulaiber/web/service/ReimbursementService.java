package com.ulaiber.web.service;

import com.ulaiber.web.model.Reimbursement;
import com.ulaiber.web.model.ReimbursementVO;

import java.util.List;

/**
 * 报销业务接口
 * Created by daiqingwen on 2017/9/11.
 */
public interface ReimbursementService {

    int insert(ReimbursementVO vo);  //新增报销记录

    List<Reimbursement> queryReimbersement(int recordNo); //根据申请记录ID，获取报销记录
}
