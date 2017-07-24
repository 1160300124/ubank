package com.ulaiber.web.dao;

import com.ulaiber.web.model.Company;
import com.ulaiber.web.model.Employee;

import java.util.List;

/**
 * 员工数据持久层
 * Created by daiqingwen on 2017/7/22.
 */
public interface EmployeeDao {

    Employee getEmpByName(String empName);//根据员工姓名查询对应的信息

    int addEmployee(Employee employee);   //新增员工信息
}
