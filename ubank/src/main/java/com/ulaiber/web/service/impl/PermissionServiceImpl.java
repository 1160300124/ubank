package com.ulaiber.web.service.impl;

import com.ulaiber.web.dao.CompanyDao;
import com.ulaiber.web.dao.EmployeeDao;
import com.ulaiber.web.dao.PermissionDao;
import com.ulaiber.web.model.*;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限管理接口实现类
 * Created by daiqingwen on 2017/7/18.
 */
@Service
public class PermissionServiceImpl extends BaseService implements PermissionService {

    @Resource
    private PermissionDao permissionDao;

    @Resource
    private CompanyDao companyDao;

    @Resource
    private EmployeeDao employeeDao;

    @Override
    public int addGroup(Group group) {
        int resultInfo = permissionDao.addGroup(group);
        return resultInfo;
    }

    @Override
    public Group searchGroupByName(String name) {
        Group group = permissionDao.searchGroupByName(name);
        return group;
    }

    @Override
    public List<Group> groupQuery(String search, int pageSize, int pageNum) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("search" ,search );
        map.put("pageSize" , pageSize);
        map.put("pageNum" , pageNum);
        List<Group> result = permissionDao.groupQuery(map);
        return result;
    }

    @Override
    public int modifyGroup(Group group) {
        int result = permissionDao.modifyGroup(group);
        return result;
    }

    @Override
    public int deleteGroup(String[] numberArr) {
        int result = permissionDao.deleteGroup(numberArr);
        return result;
    }

    @Override
    public List<Company> companyQuery(String search, int pageSize, int pageNum) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("search" ,search );
        map.put("pageSize" , pageSize);
        map.put("pageNum" , pageNum);
        List<Company> result = permissionDao.companyQuery(map);
        return result;
    }

    @Override
    public Departments getDeptByNum(String deptNum) {
        Departments result = permissionDao.getDeptByNum(deptNum);
        return result;
    }

    @Override
    public int addDept(Departments dept) {
        int result = permissionDao.addDept(dept);
        return result;
    }

    @Override
    public List<Departments> departmentQuery(String search, int pageSize, int pageNum) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("search" ,search );
        map.put("pageSize" , pageSize);
        map.put("pageNum" , pageNum);
        List<Departments> list  = permissionDao.departmentQuery(map);
        return list;
    }

    @Override
    public int getTotal() {
        int total = permissionDao.getTotal();
        return total;
    }

    @Override
    public int getDeptTotal() {
        int result = permissionDao.getDeptTotal();
        return result;
    }

    @Override
    public int editDept(Departments dept) {
        int result = permissionDao.editDept(dept);
        return result;
    }

    @Override
    public int deptDelete(String[] number) {
        int result = permissionDao.deptDelete(number);
        return result;
    }

    @Override
    public List<Group> getAllGroup() {
        List<Group> list = permissionDao.getAllGroup();
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int addCom(Company company) {
        int com = permissionDao.addCom(company);
        return com;
    }

    @Override
    public List<Bank> getAllBank() {
        List<Bank> list = permissionDao.getAllBank();
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int addBankAccount(List<Map<String, Object>> list) {
        int  account = companyDao.addBankAccount(list);
        return account;
    }

    @Override
    public int getCompanyTotal() {
        int comTotal = permissionDao.getCompanyTotal();
        return comTotal;
    }

    @Override
    public List<BankAccount> getBankAccountByNum(String[] accounts) {
        List<BankAccount> data = companyDao.getBankAccountByNum(accounts);
        return data;
    }

    @Override
    public Company getComByName(String comName) {
        Company company = companyDao.getComByName(comName);
        return company;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int deleteComByNum(String comNum) {
        int msg = companyDao.deleteComByNum(comNum);
        return msg;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int updateCompany(Company company) {
        int result = permissionDao.updateCompany(company);
        return result;
    }

    @Override
    public List<Company> getAllCompany() {
        List<Company> list = permissionDao.getAllCompany();
        return list;
    }

    @Override
    public List<Departments> getAllDept() {
        List<Departments> list = permissionDao.getAllDept();
        return list;
    }

    @Override
    public User getEmpByName(String userName) {
        User emp = employeeDao.getEmpByName(userName);
        return emp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int addEmployee(User user) {
        int result = employeeDao.addEmployee(user);
        return result;
    }

    @Override
    public int getEmpTotal() {
        int total = employeeDao.getEmpTotal();
        return total;
    }

    @Override
    public List<User> empQuery(String search, int pageSize, int pageNum) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("search" ,search );
        map.put("pageSize" , pageSize);
        map.put("pageNum" , pageNum);
        List<User> result = employeeDao.empQuery(map);
        return result;
    }

    @Override
    public int editEmp(User user) {
        int result = employeeDao.editEmp(user);
        return result;
    }

    @Override
    public int empDelete(String[] number) {
        int result = employeeDao.empDlete(number);
        return result;
    }


}
