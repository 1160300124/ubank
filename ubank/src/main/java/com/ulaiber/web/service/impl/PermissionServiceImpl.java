package com.ulaiber.web.service.impl;

import com.ulaiber.web.dao.PermissionDao;
import com.ulaiber.web.model.Company;
import com.ulaiber.web.model.Departments;
import com.ulaiber.web.model.Group;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.PermissionService;
import org.springframework.stereotype.Service;

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
    public List<Company> companyQuery() {
        List<Company> result = permissionDao.companyQuery();
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
}
