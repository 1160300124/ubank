package com.ulaiber.web.dao;

import com.ulaiber.web.model.BankAccount;

import java.util.List;

/**
 * 公司信息dao层
 * Created by daiqingwen on 2017/7/21.
 */
public interface CompanyDao {

    List<BankAccount> getBankAccountByNum(String[] accounts); //根据银行账户编号获取账户信息

}
