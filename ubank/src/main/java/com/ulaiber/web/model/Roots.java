package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 权限层级关系表
 * Created by daiqingwen on 2017/8/8.
 */
public class Roots implements Serializable{

    public int userid;
    public String groupNumber;
    public String companyNumber;
    public String dept_number;
    public String sysflag;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getDept_number() {
        return dept_number;
    }

    public void setDept_number(String dept_number) {
        this.dept_number = dept_number;
    }

    public String getSysflag() {
        return sysflag;
    }

    public void setSysflag(String sysflag) {
        this.sysflag = sysflag;
    }
}
