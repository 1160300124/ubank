package com.ulaiber.web.dao;

import com.ulaiber.web.model.User;

import java.util.List;
import java.util.Map;

/**
 * 员工数据持久层
 * Created by daiqingwen on 2017/7/22.
 */
public interface EmployeeDao {

    User getEmpByName(Map<String,Object> map);//根据员工姓名查询对应的信息

    int addEmployee(User user);   //新增员工信息

    int getEmpTotal(Map<String, Object> map); //获取员工总数

    List<User> empQuery(Map<String, Object> map); //分页查询员工信息

    int editEmp(User User); //修改员工信息

    int empDelete(String[] number);  //根据员工编号删除对应的员工

    int editRoots(User user);  //修改权限对应关系表

    int deleteRoots(String[] number); //删除权限层级表中的记录
}
