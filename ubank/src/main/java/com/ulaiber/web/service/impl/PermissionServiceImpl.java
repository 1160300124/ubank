package com.ulaiber.web.service.impl;

import com.ulaiber.web.dao.PermissionDao;
import com.ulaiber.web.model.Group;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
