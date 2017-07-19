package com.ulaiber.web.dao;

import com.ulaiber.web.model.Group;
import com.ulaiber.web.model.ResultInfo;

/**
 * 权限管理dao接口
 * Created by daiqingwen on 2017/7/18.
 */
public interface PermissionDao {

    int addGroup(Group group);  //新增集团

    Group searchGroupByName(String name); //根据集团名称查询是否数据已存在


}




