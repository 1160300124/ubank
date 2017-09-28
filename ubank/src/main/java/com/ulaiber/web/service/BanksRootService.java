package com.ulaiber.web.service;

import com.ulaiber.web.model.Menu;

import java.util.List;

/**
 * 银行权限接口
 * Created by daiqingwen on 2017/9/26.
 */
public interface BanksRootService {
    /**
     * 根据角色名获取银行管理页
     * @param userName
     * @return
     */
    List<Menu> getBankMenuByUser(String userName);
}
