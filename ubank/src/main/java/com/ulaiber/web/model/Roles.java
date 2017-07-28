package com.ulaiber.web.model;

/**
 * 角色信息实体类
 * Created by daiqingwen on 2017/7/25.
 */
public class Roles {
    private int role_id;
    private String role_name;
    private String companyNumber;

    //get set
    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }
}
