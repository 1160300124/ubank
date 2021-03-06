package com.ulaiber.web.dao;

import com.ulaiber.web.model.BankAccount;
import com.ulaiber.web.model.Company;

import java.util.List;
import java.util.Map;

/**
 * 公司信息dao层
 * Created by daiqingwen on 2017/7/21.
 */
public interface CompanyDao {

    List<BankAccount> getBankAccountByNum(String[] accounts); //根据银行账户编号获取账户信息

    Company getComByName(String comName);   //根据公司名称获取公司信息

    int addBankAccount(List<Map<String, Object>> list);  //插入银行账户信息

    int deleteCompanys(String[] idsArr); //删除公司信息

    List<Company> queryComByGroupid(String[] numberArr); //根据集团编号查询是否存在公司

    List<Company> getComByGroup(String groupNum); //根据集团获取公司名
    
    List<Company> getCompanysByNums(String[] nums); //根据公司编号查询公司
}
