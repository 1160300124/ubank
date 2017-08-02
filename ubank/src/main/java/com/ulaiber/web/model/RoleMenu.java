package com.ulaiber.web.model;

/**
 * 角色权限菜单
 * Created by daiqingwen on 2017/7/27.
 */
public class RoleMenu {
    private int id;
    private int role_id;
    private String menuNumber;

    //get set
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getMenuNumber() {
        return menuNumber;
    }

    public void setMenuNumber(String menuNumber) {
        this.menuNumber = menuNumber;
    }
}
