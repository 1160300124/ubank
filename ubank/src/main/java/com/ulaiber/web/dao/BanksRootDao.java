package com.ulaiber.web.dao;

import com.ulaiber.web.model.*;

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

    /**
     * 根据银行编号删除银行
     * @param number 总行编号
     * @return int
     */
    int removeHeadquarters(int[] number);

    /**
     * 获取所有总部
     * @return Headquarters
     */
    List<Headquarters> getAllHeadquarters(int bankNo);

    /**
     * 获取分行数量
     * @param map
     * @return int
     */
    int getbranchCount(Map<String, Object> map);

    /**
     * 分部查询
     * @param map
     * @return map
     */
    List<Branch> queryBranchs(Map<String, Object> map);

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
     * 获取各个分行业务员的总数
     * @param ids 分行ID
     * @return map
     */
    List<Map<Object,Object>> getBranchSalemenCount(int[] ids);

    /**
     * 根据分行编号修改分行信息
     * @param branch 分行信息
     * @return int
     */
    int modifyBranchs(Branch branch);

    /**
     * 根据分行编号查询是否存在支行
     * @param map
     * @return int
     */
    List<BranchsChildren> queryBranchChildrenByBranchId(Map<String,Object> map);

    /**
     * 删除分行
     * @param number 分行ID
     * @return int
     */
    int removeBranchs(int[] number);

    /**
     * 查询分支数量
     * @param map
     * @return int
     */
    int getBranchChilCount(Map<String, Object> map);

    /**
     * 查询支行
     * @param map
     * @return BranchsChildren
     */
    List<BranchsChildren> queryBranchsChild(Map<String, Object> map);

    /**
     * 获取所有分部
     * @param bankNo 银行编号
     * @return ResultInfo
     */
    List<Branch> getAllBranchs(int bankNo);

    /**
     * 根据支行名称查询支行是否已存在
     * @param childName 支行名称
     * @return BranchsChildren
     */
    BranchsChildren queryBranchChildByName(String childName);

    /**
     * 新增支行
     * @param bc 支行信息
     * @return int
     */
    int insertBranchsChild(BranchsChildren bc);


    /**
     * 修改支行
     * @param bc 支行信息
     * @return int
     */
    int modifyBranchsChild(BranchsChildren bc);

    /**
     * 根据支行编号查询是否存在业务员
     * @param number 支行ID
     * @return BranchsChildren
     */
    List<BranchsChildren> querySalemanByBranchChildId(int[] number);

    /**
     * 删除支行
     * @param number 支行ID
     * @return int
     */
    int removeBranchChild(int[] number);

    /**
     * 获取银行用户数量
     * @param map
     * @return int
     */
    int getBankUsersCount(Map<String, Object> map);

    /**
     * 获取银行用户
     * @param map
     * @return BranchsChildren
     */
    List<BranchsChildren> queryBankUsers(Map<String, Object> map);
}
