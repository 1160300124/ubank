package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 支行信息
 * Created by daiqingwen on 2017/10/10.
 */
public class BranchsChildren implements Serializable {
    private int id;
    private String name;
    private int headquartersNo;
    private int branchNo;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeadquartersNo() {
        return headquartersNo;
    }

    public void setHeadquartersNo(int headquartersNo) {
        this.headquartersNo = headquartersNo;
    }

    public int getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(int branchNo) {
        this.branchNo = branchNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
