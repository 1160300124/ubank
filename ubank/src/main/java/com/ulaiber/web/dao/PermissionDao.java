package com.ulaiber.web.dao;

import com.ulaiber.web.model.Group;
import com.ulaiber.web.model.ResultInfo;

import java.util.List;

/**
 * 权限管理dao接口
 * Created by daiqingwen on 2017/7/18.
 */
public interface PermissionDao {

    int addGroup(Group group);  //新增集团

    Group searchGroupByName(String name); //根据集团名称查询是否数据已存在


    List<Group> groupQuery(); //查询所有集团

    int modifyGroup(Group group);  //修改集团信息

    int deleteGroup(String[] numberArr);    //删除集团
}




