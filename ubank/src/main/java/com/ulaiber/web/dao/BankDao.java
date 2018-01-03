package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.*;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;
import com.ulaiber.web.model.ShangHaiAcount.Withdraw;

/**
 * 银行卡数据库接口
 *
 * @author huangguoqing
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
     * @param map
     * @return SecondAcount
     */
    SecondAcount querySecondAccount(Map<String, Object> map);

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
    int updateSecondAcc(SecondAcount sa);

    /**
     * 根据二类账户号查询账户余额
     * @param subAcctNo 二类账户号
     * @return SecondAcount
     */
    SecondAcount queryAccount(String subAcctNo);


    /**
     * 新增提现记录
     * @param withd 提现记录
     * @return int
     */
    int insertWithdraw(Withdraw withd);

    /**
     * 根据二类账户查询账单
     * @param map
     * @return Withdraw
     */
    List<Bill> queryWithdraw(Map<String, Object> map);

    /**
     * 更新交易记录
     * @param map
     * @return int
     */
    int updateWithdraw(Map<String, Object> map);

    /**
     * 根据流水号查询交易详情
     * @param rqUID 流水号
     * @return BillDetail
     */
    BillDetail queryWithdrawByRqUID(String rqUID);

    /**
     * 根据流水号查询工资转入详情
     * @param rqUID 流水号
     * @return BillDetail
     */
    BillDetail querySalariesByRqUID(String rqUID);

    /**
     * 根据二类户账号获取CID
     * @param subAcctNo 二类户账号
     * @return Map
     */
    Map<String,Object> queryCIdbySub(String subAcctNo);

    /**
     * 更新二类户冻结状态
     * @param map 二类户账号
     * @return int
     */
    int updateAccFreeze(Map<String, Object> map);
    
	/**
	 * 根据公司编号查询该公司下所有二类户
	 * @param companyNum 公司编号
	 * @return List<SecondAcount>
	 */
	List<SecondAcount> getSubByCompanyNum(String companyNum);

    /**
     * 插入转账记录
     * @param tran 转账信息
     * @return
     */
    int insertTransfer(Transfer tran);

    /**
     * 查询二类户激活状态
     * @param subAccNo 二类户账号
     * @return ResultInfo
     */
    String queryActiveStatus(String subAccNo);

    /**
     * 查询该记录是否已存在转账记录表中
     * @param rqUID 交易流水号
     * @return Transfer
     */
    Transfer queryTransfer(String rqUID);
}
