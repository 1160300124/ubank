package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 业务拓展信息
 * Created by daiqingwen on 2017/10/13.
 */
public class Business implements Serializable{
    private long id;        // ID
    private long number; // 业务员工号
    private long groupNo;   // 集团编号
    private long companyNo; // 公司编号
    private long userid;    // 管理员ID
    private String remark;  // 备注
    private String userName; // 集团管理人名称
    private String groupName; // 集团名称
    private String companyName; // 公司名称
    private String mobile; // 管理人移动电话
    private long bankNo;    //银行编号
    private String bankName; //银行名称
    private int type;       //银行标识：0 总部，1 分部，2 支部

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(long groupNo) {
        this.groupNo = groupNo;
    }

    public long getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(long companyNo) {
        this.companyNo = companyNo;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getBankNo() {
        return bankNo;
    }

    public void setBankNo(long bankNo) {
        this.bankNo = bankNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
