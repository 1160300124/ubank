package com.ulaiber.web.dao;

import com.ulaiber.web.model.User;

/**
 * 用户权限层级关系dao层
 * Created by daiqingwen on 2017/8/1.
 */
public interface LevelInfoDao {

    int addPermission(User user) ; //新增用户权限层级信息
}
