package com.ulaiber.web.model;

import javax.print.DocFlavor;
import java.io.Serializable;

/**
 * 部门信息实体类
 * Created by daiqingwen on 2017/7/19.
 */
public class Departments implements Serializable {
    private String dept_number;
    private String name;
    private String company_num;
    private String count;
    private String remark;

    public String getDept_number() {
        return dept_number;
    }

    public void setDept_number(String dept_number) {
        this.dept_number = dept_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany_num() {
        return company_num;
    }

    public void setCompany_num(String company_num) {
        this.company_num = company_num;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
