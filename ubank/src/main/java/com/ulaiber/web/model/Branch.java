package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 银行分部信息
 * Created by daiqingwen on 2017/9/29.
 */
public class Branch implements Serializable{
    private int id;
    private String name;
    private int headquartersNo;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
