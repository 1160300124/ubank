package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 二类户信息
 * Created by daiqingwen on 2017/10/31.
 */
public class SecondAccountAO implements Serializable {

    private String CustName;            //姓名
    private String SubAcctNo;           //平台理财专属子账户
    private String RqUID;               //请求流水号
    private int disabled;               //是否作废 0 否 1 是
    private String bankCardNo;          //银行卡号
    private long bankNo;                //银行编号
    private String bankName;            //银行名称
    private String type;                 //银行类型

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
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

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public long getBankNo() {
        return bankNo;
    }

    public void setBankNo(long bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
