package com.ulaiber.web.service;

import com.ulaiber.web.model.Branch;
import com.ulaiber.web.model.Headquarters;
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
     * @return Menu
     */
    List<Menu> getBankMenuByUser(String userName);

    /**
     * 根据银行名称查询银行信息
     * @param bankName 银行名称
     * @return Headquarters
     */
    Headquarters queryBankByName(String bankName);

    /**
     * 新增总行
     * @param headquarters
     * @return int
     */
    int insertHeadquarters(Headquarters headquarters);

    /**
     * 获取总行数量
     * @return int
     */
    int getHeadquartersCount();

    /**
     * 查询总行信息
     * @param search 搜索字段
     * @param pageSize 页大小
     * @param pageNum 页码
     * @return map
     */
    List<Headquarters> queryHeadquarters(String search, int pageSize, int pageNum);

    /**
     * 根据银行编号修改银行信息
     * @param bankNo 银行编号
     * @return int
     */
    int modifyHeadquarters(Headquarters headquarters);

    /**
     * 根据总行编号查询是否存在分部
     * @param numberArr 总行编号
     * @return Branch
     */
    List<Branch> queryBranchByBankNo(String[] numberArr);
}
