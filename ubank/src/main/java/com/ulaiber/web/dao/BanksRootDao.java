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
    List<BranchsChildren> getBranchsChild(int bankNo);

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
     * @param map
     * @return BankRoles
     */
    List<BankRoles> getRoleByType(Map<String, Object> map);

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
     * @param number 员工ID
     * @return ResultInfo
     */
    int removeBankUser(int[] number);

    /**
     * 根据移动电话查询当前电话是否已存在
     * @param mobile 移动电话
     * @return BankUsers
     */
    BankUsers getUserByMobile(String mobile);


    /**
     * 新增集团
     * @param group 集团信息
     * @return int
     */
    int insertGroup(Group group);

    /**
     * 新增公司
     * @param company 公司信息
     * @return int
     */
    int insertCompany(Company company);

    /**
     * 新增角色信息
     * @param roles 角色信息
     * @return int
     */
    int insertRole(Roles roles);

    /**
     * 新增用户
     * @param user 用户信息
     * @return int
     */
    int insertEmployee(User user);

    /**
     * 新增业务信息
     * @param business 业务信息
     * @return int
     */
    int insertBusiness(Business business);

    /**
     * 新增权限层级关系
     * @param user 用户信息
     * @return int
     */
    int insertPermission(User user);

    /**
     * 新增角色权限菜单
     * @param list 菜单ID
     * @return int
     */
    int insertRoleMenu(List<Map<String, Object>> list);

    /**
     * 获取业务数量
     * @param map
     * @return int
     */
    int getBusinessCount(Map<String, Object> map);

    /**
     * 业务查询
     * @param map
     * @return Business
     */
    List<Business> queryBusiness(Map<String, Object> map);

    /**
     * 修改银行用户密码
     * @param map
     * @return int
     */
    int modifyPwd(Map<String, Object> map);
}
