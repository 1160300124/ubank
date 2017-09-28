package com.ulaiber.web.service.impl;

import com.ulaiber.web.dao.BanksRootDao;
import com.ulaiber.web.model.Menu;
import com.ulaiber.web.service.BanksRootService;
import com.ulaiber.web.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 银行权限接口实现类
 * Created by daiqingwen on 2017/9/26.
 */
@Service
public class BanksRootServiceImpl extends BaseService implements BanksRootService{

    @Resource
    private BanksRootDao banksRootDao;
    @Override
    public List<Menu> getBankMenuByUser(String userName) {
        return banksRootDao.getBankMenuByUser(userName);
    }
}
