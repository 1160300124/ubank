package com.ulaiber.web.service.impl;

import com.ulaiber.web.dao.BanksRootDao;
import com.ulaiber.web.model.Branch;
import com.ulaiber.web.model.BranchsChildren;
import com.ulaiber.web.model.Headquarters;
import com.ulaiber.web.model.Menu;
import com.ulaiber.web.service.BanksRootService;
import com.ulaiber.web.service.BaseService;
import org.apache.commons.collections.map.HashedMap;
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
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int removeHeadquarters(String[] numberArr) {
        int[] number = new int[numberArr.length];
        for (int i = 0 ; i < numberArr.length ; i++){
            number[i] = Integer.parseInt(numberArr[i]);
        }
        return banksRootDao.removeHeadquarters(number);
    }

    @Override
    public List<Headquarters> getAllHeadquarters(int bankNo) {
        return banksRootDao.getAllHeadquarters(bankNo);
    }

    @Override
    public int getbranchCount(String type, int bankNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("bankNo",bankNo);
        return banksRootDao.getbranchCount(map);
    }

    @Override
    public List<Branch> queryBranchs(String search, int pageSize, int pageNum, String type, int bankNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("search",search);
        map.put("pageSize",pageSize);
        map.put("pageNum",pageNum);
        map.put("type" , type);
        map.put("bankNo" , bankNo);
        List<Branch> list = banksRootDao.queryBranchs(map);
        int[] ids = new int[list.size()];
        for (int i = 0 ; i < list.size() ; i++){
            Branch branch = list.get(i);
            ids[i] = (int) branch.getId();
        }
        //获取各个分行业务员的总数
        List<Map<Object , Object>> list2 = banksRootDao.getBranchSalemenCount(ids);
        for (int i = 0 ; i < list2.size() ; i++){
            Map<Object,Object> map2 = list2.get(i);
            for (int j = 0 ; j < list.size() ; j++){
                long id = (long) map2.get("bankNo");
                if(id == list.get(j).getId()){
                   Number num = (Number) map2.get("count");
                    list.get(j).setCount(num.intValue());
                }
            }
        }
        return list;
    }

    @Override
    public Branch queryBranchByName(String branchName) {
        return banksRootDao.queryBranchByName(branchName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int insertBranchs(Branch branch) {
        return banksRootDao.insertBranchs(branch);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyBranchs(Branch branch) {
        return banksRootDao.modifyBranchs(branch);
    }

    @Override
    public List<BranchsChildren> queryBranchChildrenByBranchId(String[] numberArr,int type) {
        int[] number = new int[numberArr.length];
        for (int i = 0 ; i < numberArr.length ; i++){
            number[i] = Integer.parseInt(numberArr[i]);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("number" , number);
        map.put("type" , type);
        return banksRootDao.queryBranchChildrenByBranchId(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int removeBranch(String[] numberArr) {
        int[] number = new int[numberArr.length];
        for (int i = 0 ; i < numberArr.length ; i++){
            number[i] = Integer.parseInt(numberArr[i]);
        }
        return banksRootDao.removeBranchs(number);
    }

    @Override
    public int getBranchChilCount(String type, int bankNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("bankNo",bankNo);
        return banksRootDao.getBranchChilCount(map);
    }

    @Override
    public List<BranchsChildren> queryBranchsChild(String search, int pageSize, int pageNum, String type, int bankNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("search",search);
        map.put("pageSize",pageSize);
        map.put("pageNum",pageNum);
        map.put("type",type);
        map.put("bankNo",bankNo);
        List<BranchsChildren> list = banksRootDao.queryBranchsChild(map);
        int[] ids = new int[list.size()];
        for (int i = 0 ; i < list.size() ; i++){
            BranchsChildren bc = list.get(i);
            ids[i] = bc.getId();
        }
        //获取各个支行业务员的总数
        List<Map<Object , Object>> list2 = banksRootDao.getBranchSalemenCount(ids);
        for (int i = 0 ; i < list2.size() ; i++){
            Map<Object,Object> map2 = list2.get(i);
            for (int j = 0 ; j < list.size() ; j++){
                long id = (long) map2.get("bankNo");
                if(id == list.get(j).getId()){
                    Number num = (Number) map2.get("count");
                    list.get(j).setCount(num.intValue());
                }
            }
        }
        return list;
    }

    @Override
    public List<Branch> getAllBranchs(int bankNo) {
        return banksRootDao.getAllBranchs(bankNo);
    }

    @Override
    public BranchsChildren queryBranchChildByName(String childName) {
        return banksRootDao.queryBranchChildByName(childName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int insertBranchsChild(BranchsChildren bc) {
        return banksRootDao.insertBranchsChild(bc);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyBranchsChild(BranchsChildren bc) {
        return banksRootDao.modifyBranchsChild(bc);
    }

    @Override
    public List<BranchsChildren> querySalemanByBranchChildId(String[] numberArr) {
        int[] number = new int[numberArr.length];
        for (int i = 0 ; i < numberArr.length ; i++){
            number[i] = Integer.parseInt(numberArr[i]);
        }
        return banksRootDao.querySalemanByBranchChildId(number);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int removeBranchChild(String[] numberArr) {
        int[] number = new int[numberArr.length];
        for (int i = 0 ; i < numberArr.length ; i++){
            number[i] = Integer.parseInt(numberArr[i]);
        }
        return banksRootDao.removeBranchChild(number);
    }

    @Override
    public int getBankUsersCount(String type, int bankNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("bankNo",bankNo);
        return banksRootDao.getBankUsersCount(map);
    }

    @Override
    public List<BranchsChildren> queryBankUsers(String search, int pageSize, int pageNum, String type, int bankNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("search",search);
        map.put("pageSize",pageSize);
        map.put("pageNum",pageNum);
        map.put("type",type);
        map.put("bankNo",bankNo);
        return banksRootDao.queryBankUsers(map);
    }


}
