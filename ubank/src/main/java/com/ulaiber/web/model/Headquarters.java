package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 总部信息
 * Created by daiqingwen on 2017/9/29.
 */
public class Headquarters implements Serializable {
    private int id;
    private String bankName;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
