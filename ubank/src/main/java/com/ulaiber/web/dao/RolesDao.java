package com.ulaiber.web.dao;

import com.ulaiber.web.model.Company;
import com.ulaiber.web.model.Roles;
import com.ulaiber.web.model.User;

import java.util.List;
import java.util.Map;

/**
 * 角色信息dao层
 * Created by daiqingwen on 2017/7/25.
 */
public interface RolesDao {

    List<Roles> roleAllQuery(Map<String, Object> map); //获取所有角色信息

    List<Roles> getRoleByName(String roleName); //根据角色名，获取角色信息

    int addRole(Map<String, Object> map);  //新增角色信息

    List<Roles> roleQuery(Map<String, Object> map); //分页查询角色信息

    int modifyRole(Map<String, Object> map);  //修改角色信息

    int deleteRoles(String[] idsArr);  //删除角色信息

    List<User> queryUserByRoleid(String[] idsArr);  //根据角色id，判断当前角色下是否有用户存在

    List<Company> getAllCompanybyGroupNum(Map<String, Object> map); //根据集团编号获取公司

    Map<String,Object> queryRoleById(String roleid);//根据角色ID获取角色信息

    int updateRole(Map<String, Object> map);  //更新角色所属公司
}
