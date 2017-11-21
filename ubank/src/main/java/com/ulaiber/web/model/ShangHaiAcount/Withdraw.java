package com.ulaiber.web.model.ShangHaiAcount;

import java.io.Serializable;

/**
 * 提现记录表
 * Created by daiqingwen on 2017/11/2.
 */
public class Withdraw implements Serializable{
    private long id;                    //ID
    private long userId;                //用户ID
    private String SubAcctNo;           //平台理财专属子账户
    private String ProductCd;           //理财产品参数
    private String BindCardNo;          //银行卡号
    private double Amount;              //交易金额
    private String BizDate;             //交易日期
    private String Currency;            //入账币种
    private String TheirRef;            //交易摘要
    private String Purpose;             //用途
    private String Attach;              //附件信息
    private String MemoInfo;            //交易备注
    private String type;                //银行类型；0 上海银行
    private String SPRsUID;             //主机流水号
    private String RqUID;               //请求流水号
    private String StatusCode;          //返回结果码
    private String ServerStatusCode;    //返回结果信息
    private String CreateDate;          //创建时间
    private String updateTime;          //更新时间
    private String sortTime;            //排序时间
    private int status;                 //交易状态 0 处理中 1 成功 2 失败
    private int trading;                //交易类型 0 提现

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSortTime() {
        return sortTime;
    }

    public void setSortTime(String sortTime) {
        this.sortTime = sortTime;
    }
}
