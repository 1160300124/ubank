package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 银行分部信息
 * Created by daiqingwen on 2017/9/29.
 */
public class Branch implements Serializable{
    private long id;
    private String name;
    private int headquartersNo;
    private String remark;
    private String headquarters;
    private int count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
