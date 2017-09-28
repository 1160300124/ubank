package com.ulaiber.web.dao;

import com.ulaiber.web.model.BankUsers;
import com.ulaiber.web.model.Menu;

import java.util.List;

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
}
