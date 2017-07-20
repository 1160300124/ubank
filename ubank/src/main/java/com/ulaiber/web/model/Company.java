package com.ulaiber.web.model;

/**
 * 公司信息实体类
 * Created by daiqingwen on 2017/7/19.
 */
public class Company {
    private int companyNumber;
    private String name;
    private String legalPerson;
    private String com_bank;
    private String com_bankNum;
    private int group_num;
    private String details;

    //get set
    public int getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getCom_bank() {
        return com_bank;
    }

    public void setCom_bank(String com_bank) {
        this.com_bank = com_bank;
    }

    public int getGroup_num() {
        return group_num;
    }

    public void setGroup_num(int group_num) {
        this.group_num = group_num;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}