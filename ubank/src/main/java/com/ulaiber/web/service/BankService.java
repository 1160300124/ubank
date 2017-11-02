package com.ulaiber.web.service;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.Bank;
import com.ulaiber.web.model.BankAccount;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;

public interface BankService {
	
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

	/**
	 * 获取公司绑定的银行信息
	 * @param code 邀请码
	 * @return BankAccount
	 */
    BankAccount getBankByCode(String code);

	/**
	 * 根据银行编号获取银行信息
	 * @param bankNumber 银行编号
	 * @return Bank
	 */
	List<Bank> queryBanksByNumber(String bankNumber);

	/**
	 * 删除原绑定银行卡
	 * @param map
	 * @return int
	 */
    int deleteOriginCart(Map<String, Object> map);

	/**
	 * 根据二类户账号ID和银行卡号查询二类户信息
	 * @param SubAcctNo 二类户ID
	 * @return SecondAcount
	 */
    SecondAcount querySecondAccount(String SubAcctNo);

	/**
	 * 根据用户ID获取对应公司的邀请码
	 * @param userid 用户ID
	 * @return String
	 */
	String getCodeByuserid(long userid);

	/**
	 * 更新二类账户余额
	 * @param sa 上海银行二类账户
	 * @return int
	 */
	int updateSecondAcc(SecondAcount sa );

}
