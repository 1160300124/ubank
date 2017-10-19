package com.ulaiber.web.service;

import com.ulaiber.web.model.*;

import java.util.List;
import java.util.Map;

/**
 * 权限管理业务层
 * Created by daiqingwen on 2017/7/18.
 */
public interface PermissionService {

    int addGroup(Group group); //新增集团

    Group searchGroupByName(String name); //根据集团名称查询是否数据已存在

    List<Group> groupQuery(String search, int pageSize, int pageNum,String sysflag,String groupNumber); //查询所有集团

    int modifyGroup(Group group); //修改集团信息

    int deleteGroup(String[] numberArr);  //删除集团

    List<Company> companyQuery(String search, int pageSize, int pageNum,String sysflag,String groupNumber);   //获取所有公司信息

    Departments getDeptByNum(String deptNum);   //根据部门编号获取对应部门

    int addDept(Departments dept);  //新增部门

    List<Departments> departmentQuery(String search, int pageSize, int pageNum,String sysflag,String[] comArr);  //获取所有部门信息

    int getTotal(String sysflag,String groupNumber); //获取集团总数

    int getDeptTotal(String sysflag,String[] comArr);  //获取部门总数

    int editDept(Departments dept); //修改部门

    int deptDelete(String[] number);

    List<Group> getAllGroup(String sysflag,String groupNumber);   // 获取所有集团信息

    int addCom(Company company);  //插入公司基本信息

    List<Bank> getAllBank();  //获取所有银行信息

    int addBankAccount(List<Map<String, Object>> list); //插入银行账户信息

    int getCompanyTotal(String sysflag,String groupNumber);  //获取公司总数

    List<BankAccount> getBankAccountByNum(String[] accounts);  //根据银行账户编号获取账户信息

    Company getComByName(String comName);  //根据公司名称获取公司信息

    int deleteComByNum(String comNum); //根据公司编号删除银行账户信息表中的数据

    int updateCompany(Company company);  //更新银行信息表

    List<Company> getAllCompany(String sysflag,String groupNumber);  // 获取所有公司信息

    List<Departments> getAllDept(); //获取所有部门信息

    User getEmpByName(String userName,String mobile);  //根据员工姓名查询对应的信息

    int addEmployee(User user);  //新增员工信息

    int getEmpTotal(String sysflag,String[] comArr); //获取员工总数

    List<User> empQuery(String search, int pageSize, int pageNum,String sysflag,String[] comArr); //分页查询员工信息

    int editEmp(User user); //修改员工信息

    int empDelete(String[] number); //根据员工编号删除对应的员工

    List<Roles> roleAllQuery(String sysflag,String companyNumber);  //获取所有角色信息

    List<Roles> getRoleByName(String roleName);  //根据角色名，获取角色信息

    int addRole(String com_numbers, String roleName,String names); //新增角色信息

    int settingRoleMenu(String roleId, String menuId); //设置角色权限

    List<RoleMenu> getRoleMenuByRoleid(String roleId);  // 根据角色id查询该角色是否被创建

    int getRoleTotal(String sysflag,String companyNumber);  //获取角色总数

    List<Roles> roleQuery(String search, int pageSize, int pageNum,String sysflag,String[] comArr);  //分页查询角色信息

    int modifyRole(String com_numbers, String roleName , String roleId,String names);  //修改角色信息

    int deleteRoles(String[] idsArr); //删除角色信息


    int deleteRolesMenu(String[] idsArr);  //删除角色对应的权限菜单

    int deleteCompanys(String[] idsArr);  //删除公司信息

    int deleteCompanyByNum(String[] idsArr); //根据公司编号删除对应的银行账户

    List<User> queryUserByRoleid(String[] idsArr);  //根据角色id，判断当前角色下是否有用户存在

    List<User> queryUserByDeptid(String[] number); //根据部门id查询该部门是否存在用户

    List<Departments> queryDeptByCompanyNum(String[] idsArr); //根据公司编号查询该公司是否存在部门

    List<Company> queryComByGroupid(String[] numberArr); //根据集团编号查询是否存在公司

    int addPermission(User user);   //新增用户权限层级信息

    int setRoleMenuByRoleId( String roleId, String menuId); // 根据角色id，删除对应的菜单

    List<Company> getComByGroup(String groupNum); //根据集团获取公司名

    List<Departments> getDeptByCom(String comNum); //根据公司编号获取部门

    int editRoots(User user); //修改权限对应关系表

    List<Company> getAllCompanybyGroupNum(String sysflag, String groupNumber);

    List<Departments> queryAllDept(String sysflag, String companyNumber); //根据当前角色所属公司编号，查询对应的部门

    List<Departments> getDeptEmpCount();  //获取各个部门员工人数

    int deleteRoots(String[] number); //删除权限层级表中的记录
    
    List<Company> getCompanysByNums(String[] nums); //根据公司编号查询公司

    Map<String,Object> queryRoleById(String roleid);

    int updateRole(String roleid, String comNo, String name); //更新角色所属公司

    int insertRole(Roles roles);  //新增角色信息
}
