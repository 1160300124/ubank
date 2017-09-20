package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 报销报表返回对象
 * Created by daiqingwen on 2017/9/19.
 */
public class ReimReportVO implements Serializable {
    private int id;             //申请记录ID
    private String createDate;  //提交时间
    private String username;    //用户名
    private String company;     //公司
    private String dept;        //部门
    private int totalAmount;    //金额
    private String reason;      //申请内容
    private int count;          //报销项
    private String status;      //审核状态

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
