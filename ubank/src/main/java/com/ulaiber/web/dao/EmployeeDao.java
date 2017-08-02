package com.ulaiber.web.dao;

import com.ulaiber.web.model.User;

import java.util.List;
import java.util.Map;

/**
 * 员工数据持久层
 * Created by daiqingwen on 2017/7/22.
 */
public interface EmployeeDao {

    User getEmpByName(String userName);//根据员工姓名查询对应的信息

    int addEmployee(User user);   //新增员工信息

    int getEmpTotal(); //获取员工总数

    List<User> empQuery(Map<String, Object> map); //分页查询员工信息

    int editEmp(User User); //修改员工信息

    int empDlete(String[] number);  //根据员工编号删除对应的员工
}
