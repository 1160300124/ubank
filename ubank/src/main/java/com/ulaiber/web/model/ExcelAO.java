package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * excel字段内容
 * Created by daiqingwen on 2017/12/8.
 */
public class ExcelAO implements Serializable {
    private String id;          //序号
    private String name;        //姓名
    private String IDCard;      //身份证
    private String mobile;      //电话
    private String entryTime;   //入职时间
    private String message;     //错误信息
    private String dept;        //部门
    private double salary;      //工资

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
