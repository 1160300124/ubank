package com.ulaiber.web.service;

import com.ulaiber.web.model.Branch;
import com.ulaiber.web.model.BranchsChildren;
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
     * @param headquarters 银行信息
     * @return int
     */
    int modifyHeadquarters(Headquarters headquarters);

    /**
     * 根据总行编号查询是否存在分部
     * @param numberArr 总行编号
     * @return Branch
     */
    List<Branch> queryBranchByBankNo(String[] numberArr);

    /**
     * 根据银行编号删除银行
     * @param numberArr 总行编号
     * @return int
     */
    int removeHeadquarters(String[] numberArr);

    /**
     * 获取所有总部
     * @return Headquarters
     */
    List<Headquarters> getAllHeadquarters();

    /**
     * 获取分行数量
     * @param type  标识。所属部门是总行？分行？支行？
     * @param bankNo 角色所属部门
     * @return int
     */
    int getbranchCount(String type, int bankNo);

    /**
     * 分部查询
     * @param search 搜索内容
     * @param pageSize 页大小
     * @param pageNum 页码
     * @param type  标识。所属部门是总行？分行？支行？
     * @param bankNo 角色所属部门
     * @return map
     */
    List<Branch> queryBranchs(String search, int pageSize, int pageNum, String type, int bankNo);

    /**
     * 根据名称查询分行是否已存在
     * @param branchName 分行名称
     * @return Branch
     */
    Branch queryBranchByName(String branchName);

    /**
     * 新增分行
     * @param branch 分行信息
     * @return int
     */
    int insertBranchs(Branch branch);

    /**
     * 根据分行编号修改分行信息
     * @param branch 分行信息
     * @return int
     */
    int modifyBranchs(Branch branch);

    /**
     * 根据分行编号查询是否存在支行
     * @param numberArr 分行ID
     * @return
     */
    List<BranchsChildren> queryBranchChildrenByBranchId(String[] numberArr,int type);

    /**
     * 删除分行
     * @param numberArr 分行ID
     * @return
     */
    int removeBranch(String[] numberArr);

    /**
     * 获取支行总数
     * @param type 标识。所属部门是总行？分行？支行？
     * @param bankNo  角色所属部门
     * @return int
     */
    int getBranchChilCount(String type, int bankNo);
}
