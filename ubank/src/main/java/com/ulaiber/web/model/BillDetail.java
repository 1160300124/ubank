package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 交易明细
 * Created by daiqingwen on 2017/11/14.
 */
public class BillDetail implements Serializable{

    private String RqUID;		//交易流水号
    private double amount;      //金额
    private String remark;      //备注
    private int tradingStatus;  //交易状态 0 处理中 1 成功 2 失败
    private String CreateDate;  //创建日期
    private String updateTime;  //更新时间
    private String BindCardNo;  //绑定银行卡
    private String username;    //用户名
    private String icon;        //图标

    public String getRqUID() {
        return RqUID;
    }

    public void setRqUID(String rqUID) {
        RqUID = rqUID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getTradingStatus() {
        return tradingStatus;
    }

    public void setTradingStatus(int tradingStatus) {
        this.tradingStatus = tradingStatus;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBindCardNo() {
        return BindCardNo;
    }

    public void setBindCardNo(String bindCardNo) {
        BindCardNo = bindCardNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
