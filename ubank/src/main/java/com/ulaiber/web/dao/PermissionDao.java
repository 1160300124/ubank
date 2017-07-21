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


    List<Group> groupQuery(Map<String,Object> map); //查询所有集团

    int modifyGroup(Group group);  //修改集团信息

    int deleteGroup(String[] numberArr);    //删除集团

    List<Company> companyQuery(Map<String,Object> map);   //获取所有公司信息

    Departments getDeptByNum(String deptNum);  //根据部门编号获取对应部门

    int addDept(Departments dept); //新增部门

    List<Departments> departmentQuery(Map<String,Object> map);   //获取所有部门信息

    int getTotal();  //获取总数

    int getDeptTotal();  //获取部门总数

    int editDept(Departments dept); //修改部门

    int deptDelete(String[] number);  // 删除部门

    List<Group> getAllGroup();  //获取所有集团信息

    int addCom(Company company);  //插入公司基本信息

    List<Bank> getAllBank(); //获取所有银行信息

    int addBankAccount(List<Map<String, Object>> list);  //插入银行账户信息

    int getCompanyTotal();   //获取公司总数
}




