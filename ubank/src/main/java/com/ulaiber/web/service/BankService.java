package com.ulaiber.web.service;

import java.util.List;

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
	 * @param originCart 原绑定卡号
	 * @param userid 用户ID
	 * @param bankNo
	 *@param newCardNo @return int
	 */
    int deleteOriginCart(String originCart, long userid, long bankNo, String newCardNo);

	/**
	 * 根据二类户账号ID和银行卡号查询二类户信息
	 * @param id 二类户ID
	 * @return SecondAcount
	 */
    SecondAcount querySecondAccount(long id);
}
