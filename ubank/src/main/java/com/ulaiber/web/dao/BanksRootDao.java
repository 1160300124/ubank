package com.ulaiber.web.dao;

import com.ulaiber.web.model.BankUsers;
import com.ulaiber.web.model.Branch;
import com.ulaiber.web.model.Headquarters;
import com.ulaiber.web.model.Menu;

import java.util.List;
import java.util.Map;

/**
 * 银行权限数据库持久层
 * Created by daiqingwen on 2017/9/26.
 */
public interface BanksRootDao {

    /**
     * 银行用户登录
     * @param mobile
     * @return
     */
    BankUsers bankUserLogin(String mobile);

    /**
     * 根据角色名获取银行管理页
     * @param userName
     * @return
     */
    List<Menu> getBankMenuByUser(String userName);

    /**
     * 根据银行名称查询银行信息
     * @param bankName
     * @return Headquarters
     */
    Headquarters queryBankByName(String bankName);

    /**
     * 新增总行
     * @param headquarters
     * @return
     */
    int insertHeadquarters(Headquarters headquarters);

    /**
     * 获取总行数量
     * @return
     */
    int getHeadquartersCount();

    /**
     * 查询总行信息
     * @param map
     * @return Headquarters
     */
    List<Headquarters> queryHeadquarters(Map<String, Object> map);

    /**
     * 根据银行编号修改银行信息
     * @param bankNo 银行编号
     * @return int
     */
    int modifyHeadquarters(Headquarters headquarters);

    /**
     * 根据总行编号查询是否存在分部
     * @param number 总行编号
     * @return Branch
     */
    List<Branch> queryBranchByBankNo(int[] number);
}
