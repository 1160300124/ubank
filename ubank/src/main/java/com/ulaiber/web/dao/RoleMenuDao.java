package com.ulaiber.web.dao;

import com.ulaiber.web.model.RoleMenu;

import java.util.List;
import java.util.Map;

/**
 * 角色权限菜单dao层
 * Created by daiqingwen on 2017/7/27.
 */
public interface RoleMenuDao {

    int settingRoleMenu(List<Map<String, Object>> list); //设置角色权限

    List<RoleMenu> getRoleMenuByRoleid(String roleId);  //根据角色id查询该角色是否被创建

    int getRoleTotal(); //获取角色总数

    int deleteRolesMenu(String[] idsArr);  //删除角色对应的权限菜单
}
