package com.ulaiber.web.model.ShangHaiAcount;

import java.io.Serializable;

/**
 * 提现记录表
 * Created by daiqingwen on 2017/11/2.
 */
public class Withdraw implements Serializable{
    private long id;            //ID
    private String SubAcctNo;   //平台理财专属子账户
    private String ProductCd;   //理财产品参数
    private String BindCardNo;  //银行卡号
    private double Amount;      //交易金额
    private String BizDate;     //交易日期
    private String Currency;    //入账币种
    private String TheirRef;    //交易摘要
    private String Purpose;     //用途
    private String Attach;      //附件信息
    private String MemoInfo;    //交易备注
    private String type;        //银行类型；0 上海银行
    private String SPRsUID;             //主机流水号
    private String RqUID;               //请求流水号
    private String StatusCode;          //返回结果码
    private String ServerStatusCode;    //返回结果信息

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubAcctNo() {
        return SubAcctNo;
    }

    public void setSubAcctNo(String subAcctNo) {
        SubAcctNo = subAcctNo;
    }

    public String getProductCd() {
        return ProductCd;
    }

    public void setProductCd(String productCd) {
        ProductCd = productCd;
    }

    public String getBindCardNo() {
        return BindCardNo;
    }

    public void setBindCardNo(String bindCardNo) {
        BindCardNo = bindCardNo;
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

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getTheirRef() {
        return TheirRef;
    }

    public void setTheirRef(String theirRef) {
        TheirRef = theirRef;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getAttach() {
        return Attach;
    }

    public void setAttach(String attach) {
        Attach = attach;
    }

    public String getMemoInfo() {
        return MemoInfo;
    }

    public void setMemoInfo(String memoInfo) {
        MemoInfo = memoInfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSPRsUID() {
        return SPRsUID;
    }

    public void setSPRsUID(String SPRsUID) {
        this.SPRsUID = SPRsUID;
    }

    public String getRqUID() {
        return RqUID;
    }

    public void setRqUID(String rqUID) {
        RqUID = rqUID;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public String getServerStatusCode() {
        return ServerStatusCode;
    }

    public void setServerStatusCode(String serverStatusCode) {
        ServerStatusCode = serverStatusCode;
    }
}
