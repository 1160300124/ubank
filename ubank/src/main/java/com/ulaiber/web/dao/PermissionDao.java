package com.ulaiber.web.dao;

import com.ulaiber.web.model.*;

import java.util.List;
import java.util.Map;

/**
 * 权限管理dao接口
 * Created by daiqingwen on 2017/7/18.
 */
public interface PermissionDao {

    int addGroup(Group group);  //新增集团

    Group searchGroupByName(String name); //根据集团名称查询是否数据已存在


    List<Group> groupQuery(Map<String, Object> map); //查询所有集团

    int modifyGroup(Group group);  //修改集团信息

    int deleteGroup(String[] numberArr);    //删除集团

    List<Company> companyQuery(Map<String, Object> map);   //获取所有公司信息

    Departments getDeptByNum(String deptNum);  //根据部门编号获取对应部门

    int addDept(Departments dept); //新增部门

    List<Departments> departmentQuery(Map<String, Object> map);   //获取所有部门信息

    int getTotal(Map<String, Object> map);  //获取集团总数

    int getDeptTotal(Map<String, Object> map);  //获取部门总数

    int editDept(Departments dept); //修改部门

    int deptDelete(String[] number);  // 删除部门

    List<Group> getAllGroup(Map<String, Object> map);  //获取所有集团信息

    int addCom(Company company);  //插入公司基本信息

    List<Bank> getAllBank(); //获取所有银行信息

    int getCompanyTotal(Map<String, Object> map);   //获取公司总数

    int updateCompany(Company company);   //更新银行信息表

    List<Company> getAllCompany(Map<String, Object> map);  // 获取所有公司信息

    List<Departments> getAllDept(); //获取所有部门信息

    List<User> queryUserByDeptid(String[] number);  //根据部门id查询该部门是否存在用户

    List<Departments> queryDeptByCompanyNum(String[] idsArr); //根据公司编号查询该公司是否存在部门

    List<Departments> getDeptByCom(String comNum); //根据公司编号获取部门

    List<Departments> queryAllDept(Map<String, Object> map); //根据当前角色所属公司编号，查询对应的部门

    List<Departments> getDeptEmpCount();  //获取各个部门员工人数

    int insertRole(Roles roles); //新增角色信息

    /**
     * 根据邀请码获取公司和集团编号
     * @param code 邀请码
     * @return Company
     */
    Company getComAndGroupByCode(String code);

    /**
     * 给注册用户分配公司和集团
     * @param map
     * @return int
     */
    int insertRoots(Map<String, Object> map);

    /**
     * 查询当前电话号码是否已被注册
     * @param mobile 电话号码
     * @return user
     */
    User queryuserByMobile(String mobile);
}




