package com.ulaiber.web.service;

import com.ulaiber.web.model.Group;

/**
 * 权限管理业务层
 * Created by daiqingwen on 2017/7/18.
 */
public interface PermissionService {

    int addGroup(Group group); //新增集团

    Group searchGroupByName(String name); //根据集团名称查询是否数据已存在
}
