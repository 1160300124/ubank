package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 转账记录信息
 * Created by daiqingwen on 2017/11/30.
 */
public class Transfer implements Serializable {
    private long userId;                //用户ID
    private String SubAcctNo;           //平台理财专属子账户
    private String RqUID;               //交易流水号
    private String BizTime;             //交易时间
    private double Amount;              //交易金额
    private String BizDate;             //交易日期
    private String TheirRef;            //交易摘要
    private int status;                 //交易状态 0 处理中 1 成功 2 失败
    private int trading;                //交易类型 0 提现 1 工资转入,2 其他银行转账记录

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSubAcctNo() {
        return SubAcctNo;
    }

    public void setSubAcctNo(String subAcctNo) {
        SubAcctNo = subAcctNo;
    }

    public String getRqUID() {
        return RqUID;
    }

    public void setRqUID(String rqUID) {
        RqUID = rqUID;
    }

    public String getBizTime() {
        return BizTime;
    }

    public void setBizTime(String bizTime) {
        BizTime = bizTime;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getBizDate() {
        return BizDate;
    }

    public void setBizDate(String bizDate) {
        BizDate = bizDate;
    }

    public String getTheirRef() {
        return TheirRef;
    }

    public void setTheirRef(String theirRef) {
        TheirRef = theirRef;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTrading() {
        return trading;
    }

    public void setTrading(int trading) {
        this.trading = trading;
    }
}
