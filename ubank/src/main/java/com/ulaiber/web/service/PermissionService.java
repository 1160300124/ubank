package com.ulaiber.web.service;

import com.ulaiber.web.model.Company;
import com.ulaiber.web.model.Departments;
import com.ulaiber.web.model.Group;

import java.util.List;

/**
 * 权限管理业务层
 * Created by daiqingwen on 2017/7/18.
 */
public interface PermissionService {

    int addGroup(Group group); //新增集团

    Group searchGroupByName(String name); //根据集团名称查询是否数据已存在

    List<Group> groupQuery(String search, int pageSize, int pageNum); //查询所有集团

    int modifyGroup(Group group); //修改集团信息

    int deleteGroup(String[] numberArr);  //删除集团

    List<Company> companyQuery();   //获取所有公司信息

    Departments getDeptByNum(String deptNum);   //根据部门编号获取对应部门

    int addDept(Departments dept);  //新增部门

    List<Departments> departmentQuery(String search, int pageSize, int pageNum);  //获取所有部门信息

    int getTotal(); //获取集团总数

    int getDeptTotal();  //获取部门总数

    int editDept(Departments dept); //修改部门

    int deptDelete(String[] number);
}
