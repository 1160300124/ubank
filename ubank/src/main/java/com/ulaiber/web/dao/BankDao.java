package com.ulaiber.web.dao;

import java.util.List;

import com.ulaiber.web.model.Bank;

/**
 * 银行卡数据库接口
 * 
 * @author huangguoqing
 *
 */
public interface BankDao {

	/**
	 * 根据银行编号查询银行信息
	 * 
	 * @param bankNo 银行编号
	 * @return Bank 银行信息
	 */
	Bank getBankByBankNo(String bankNo);
	
	/**
	 * 查询所有的银行信息
	 * 
	 * @return List<Bank> 银行信息集合
	 */
	List<Bank> getAllBanks();


	int deleteComByNum(String comNum);  //根据公司编号删除银行账户信息表中的数据

	int deleteCompanyByNum(String[] idsArr); //根据公司编号删除银行账户信息表中的数据
}
