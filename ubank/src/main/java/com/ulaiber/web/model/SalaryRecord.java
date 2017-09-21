package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 工资申请审批表实体类
 * Created by daiqingwen on 2017/9/21.
 */
public class SalaryRecord implements Serializable {
    private int id;
    private int recordNo;
    private String username;
    private double salary;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(int recordNo) {
        this.recordNo = recordNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
