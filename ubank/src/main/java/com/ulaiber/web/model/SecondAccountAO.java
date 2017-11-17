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
    private double AvaiBal;             //可用余额 (子账户余额+可用基金份额)
    private double WorkingBal;          //余额(子账户余额)
    private double FundShare;           //基金份额(以基金公司为准，不含当日申购赎回的交易份额)
    private double AvaiFundShare;       //可用基金份额(当前实际可用的基金份额，含当日申购赎回的交易份额)
    private double EarningsYesterday;   //昨日收益
    private String ProductCd;            //理财产品参数
    private String ModiType;            //修改类型  00:换卡 01:修改绑定卡手机号
    private int size;                   //上传银行图片压缩大小

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

    public double getAvaiBal() {
        return AvaiBal;
    }

    public void setAvaiBal(double avaiBal) {
        AvaiBal = avaiBal;
    }

    public double getWorkingBal() {
        return WorkingBal;
    }

    public void setWorkingBal(double workingBal) {
        WorkingBal = workingBal;
    }

    public double getFundShare() {
        return FundShare;
    }

    public void setFundShare(double fundShare) {
        FundShare = fundShare;
    }

    public double getAvaiFundShare() {
        return AvaiFundShare;
    }

    public void setAvaiFundShare(double avaiFundShare) {
        AvaiFundShare = avaiFundShare;
    }

    public double getEarningsYesterday() {
        return EarningsYesterday;
    }

    public void setEarningsYesterday(double earningsYesterday) {
        EarningsYesterday = earningsYesterday;
    }

    public String getProductCd() {
        return ProductCd;
    }

    public void setProductCd(String productCd) {
        ProductCd = productCd;
    }

    public String getModiType() {
        return ModiType;
    }

    public void setModiType(String modiType) {
        ModiType = modiType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
