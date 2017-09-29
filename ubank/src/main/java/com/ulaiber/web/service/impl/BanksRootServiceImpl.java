package com.ulaiber.web.service.impl;

import com.ulaiber.web.dao.BanksRootDao;
import com.ulaiber.web.model.Branch;
import com.ulaiber.web.model.Headquarters;
import com.ulaiber.web.model.Menu;
import com.ulaiber.web.service.BanksRootService;
import com.ulaiber.web.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Headquarters queryBankByName(String bankName) {
        return banksRootDao.queryBankByName(bankName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int insertHeadquarters(Headquarters headquarters) {
        return banksRootDao.insertHeadquarters(headquarters);
    }

    @Override
    public int getHeadquartersCount() {
        return banksRootDao.getHeadquartersCount();
    }

    @Override
    public List<Headquarters> queryHeadquarters(String search, int pageSize, int pageNum) {
        Map<String,Object> map = new HashMap<>();
        map.put("search",search);
        map.put("pageSize",pageSize);
        map.put("pageNum",pageNum);
        List<Headquarters> list = banksRootDao.queryHeadquarters(map);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyHeadquarters(Headquarters headquarters) {
        return banksRootDao.modifyHeadquarters(headquarters);
    }

    @Override
    public List<Branch> queryBranchByBankNo(String[] numberArr) {
        int[] number = new int[numberArr.length];
        for (int i = 0 ; i < numberArr.length ; i++){
            number[i] = Integer.parseInt(numberArr[i]);
        }
        return banksRootDao.queryBranchByBankNo(number);
    }

    @Override
    public int removeHeadquarters(String[] numberArr) {
        int[] number = new int[numberArr.length];
        for (int i = 0 ; i < numberArr.length ; i++){
            number[i] = Integer.parseInt(numberArr[i]);
        }
        return 0;
    }
}
