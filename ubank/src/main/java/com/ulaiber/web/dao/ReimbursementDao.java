package com.ulaiber.web.dao;

import com.ulaiber.web.model.Reimbursement;

import java.util.List;
import java.util.Map;

/**
 * 报销数据库持久层
 * Created by daiqingwen on 2017/9/11.
 */
public interface ReimbursementDao {

    int insertRecord(Map<String, Object> map);  //新增申请记录

    int insertReim(List<Reimbursement> list); //新增报销记录
}
