package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 银行角色信息
 * Created by daiqingwen on 2017/10/13.
 */
public class BankRoles implements Serializable{
    private long id;            //编号
    private String roleName;    //角色名
    private int type;           //角色类型。0 总部管理员，1 分部管理员，2 支部管理员，3业务员

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
