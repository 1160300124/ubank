package com.ulaiber.web.dao;

import com.ulaiber.web.model.Roles;

import java.util.List;
import java.util.Map;

/**
 * 角色信息dao层
 * Created by daiqingwen on 2017/7/25.
 */
public interface RolesDao {

    List<Roles> roleAllQuery(); //获取所有角色信息

    List<Roles> getRoleByName(String roleName); //根据角色名，获取角色信息

    int addRole(Map<String,Object> map);  //新增角色信息

    List<Roles> roleQuery(Map<String, Object> map); //分页查询角色信息

    int modifyRole(Map<String, Object> map);  //修改角色信息

    int deleteRoles(String[] idsArr);  //删除角色信息
}
