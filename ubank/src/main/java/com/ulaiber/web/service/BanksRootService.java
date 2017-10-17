package com.ulaiber.web.service;

import com.ulaiber.web.model.*;

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
    List<Headquarters> getAllHeadquarters(int bankNo);

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

    /**
     * 查询支行
     * @param search 搜索关键字
     * @param pageSize 页大小
     * @param pageNum 页码
     * @param type 标识。所属部门是总行？分行？支行？
     * @param bankNo  角色所属部门
     * @return map
     */
    List<BranchsChildren> queryBranchsChild(String search, int pageSize, int pageNum, String type, int bankNo);

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
     * @param numberArr 支行ID
     * @return BranchsChildren
     */
    List<BranchsChildren> querySalemanByBranchChildId(String[] numberArr);

    /**
     * 删除支行
     * @param numberArr 支行ID
     * @return int
     */
    int removeBranchChild(String[] numberArr);

    /**
     * 获取银行用户数量
     * @param type 标识。所属部门是总行？分行？支行？
     * @param bankNo 银行编号
     * @return int
     */
    int getBankUsersCount(String type, int bankNo);

    /**
     * 查询银行用户
     * @param search 搜索关键字
     * @param pageSize 页大小
     * @param pageNum 页码
     * @param type 标识。所属部门是总行？分行？支行？
     * @param bankNo  角色所属部门
     * @return BranchsChildren
     */
    List<BranchsChildren> queryBankUsers(String search, int pageSize, int pageNum, String type, int bankNo,String name,String mobile);

    /**
     * 根据银行编号获取总行
     * @param bankNo 银行编号
     * @return Headquarters
     */
    List<Headquarters> getHeadquarters(int bankNo);

    /**
     * 根据总行编号获取支行
     * @param bankNo 银行编号
     * @return BranchsChildren
     */
    List<BranchsChildren> getBranchChild(int bankNo);

    /**
     * 根据分行编号获取分行
     * @param bankNo 银行编号
     * @return Branch
     */
    List<Branch> getBranchs(int bankNo);

    /**
     * 根据银行编号获取支行
     * @param bankNo 银行编号
     * @return BranchsChildren
     */
    List<BranchsChildren> getBranchChildByID(int bankNo);

    /**
     * 根据银行类型获取角色
     * @param id 银行编号
     * @param type 标识。所属部门是总行？分行？支行？
     * @return ResultInfo
     */
    List<BankRoles> getRoleByType(int id, String type);

    /**
     * 根据名称查询当前用户是否已存在
     * @param name 名称
     * @return BankUsers
     */
    BankUsers getUserByName(String name);

    /**
     * 新增银行用户
     * @param bankUsers 银行用户信息
     * @return ResultInfo
     */
    int insertBankUser(BankUsers bankUsers);

    /**
     * 修改银行用户
     * @param bankUsers 银行用户信息
     * @return int
     */
    int modifyBankUser(BankUsers bankUsers);

    /**
     * 删除银行员工
     * @param numberArr 员工ID
     * @return ResultInfo
     */
    int removeBankUser(String[] numberArr);

    /**
     * 根据移动电话查询当前电话是否已存在
     * @param mobile 移动电话
     * @return BankUsers
     */
    BankUsers getUserByMobile(String mobile);

    /**
     * 新增业务信息
     * @param business 业务信息
     * @param password 集团管理员临时密码
     * @return ResultInfo
     */
    ResultInfo insertBusinessInfo(Business business, String password);

    /**
     * 获取业务数量
     * @param type  标识。所属部门是总行？分行？支行？
     * @param bankNo 银行编号
     * @param roleType 角色类型。0 总部管理员，1 分部管理员，2 支部管理员，3业务员
     * @return int
     */
    int getBusinessCount(String type, int roleType, int bankNo,int number);

    /**
     * 业务查询
     * @param pageSize 页大小
     * @param pageNum 页码
     * @param type  标识。所属部门是总行？分行？支行？
     * @param bankNo 银行编号
     * @param roleType 角色类型。0 总部管理员，1 分部管理员，2 支部管理员，3业务员
     * @param heaquarters 总行
     * @param branch 分行
     * @param child 支行
     * @param name 业务员名称
     * @param groupName 集团名称
     * @return Map
     */
    List<Business> queryBusiness(int pageSize, int pageNum, String type, int bankNo, int roleType,
                                 String heaquarters, String branch, String child, String name, String groupName, int number);
}
