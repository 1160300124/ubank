package com.ulaiber.web.model.ShangHaiAcount;

import java.io.Serializable;

/**
 * 上海银行二类账户信息
 * Created by daiqingwen on 2017/10/23.
 */
public class SecondAcount implements Serializable {
    private long id;
    private long userid;                //用户ID
    private String EacctNo;             //E账户主账户
    private String CustName;            //姓名
    private String SubAcctNo;           //平台理财专属子账户
    private String CoopCustNo;          //合作方客户账号
    private String ProductCd;           //理财产品参数
    private String Sign;                //是否开通余额理财功能   N 否， Y 是
    private String FundCode;            //基金代码
    private String AcctOpenResult;      //平台账户开户结果   S 成功  F 失败
    private String AcctOpenDesc;        //平台账户开户结果描述
    private String FundAcctOpenResult;  //基金账户开户结果
    private String FundAcctOpenDesc;    //基金账户开户结果描述
    private String FundAcct;            //基金账户
    private String FundTxnAcct;         //基金交易账号
    private String StatusCode;          //返回结果码
    private String ServerStatusCode;    //返回结果信息
    private int disabled;               //是否作废 0 否 1 是
    private String createDate;          //创建时间
    private String IdNo;                //身份证号
    private String SPRsUID;             //主机流水号
    private String RqUID;               //请求流水号
    private String BindCardNo;          //绑定银行卡号
    private String ReservedPhone;       //银行卡预留手机号
    private long bankNo;                //银行卡编号
    private double AvaiBal;             //可用余额 (子账户余额+可用基金份额)
    private double WorkingBal;          //余额(子账户余额)
    private double FundShare;           //基金份额(以基金公司为准，不含当日申购赎回的交易份额)
    private double AvaiFundShare;       //可用基金份额(当前实际可用的基金份额，含当日申购赎回的交易份额)
    private double EarningsYesterday;   //昨日收益
    private String freeze;              //是否冻结 0 否 1 是

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getEacctNo() {
        return EacctNo;
    }

    public void setEacctNo(String eacctNo) {
        EacctNo = eacctNo;
    }

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

    public String getCoopCustNo() {
        return CoopCustNo;
    }

    public void setCoopCustNo(String coopCustNo) {
        CoopCustNo = coopCustNo;
    }

    public String getProductCd() {
        return ProductCd;
    }

    public void setProductCd(String productCd) {
        ProductCd = productCd;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public String getFundCode() {
        return FundCode;
    }

    public void setFundCode(String fundCode) {
        FundCode = fundCode;
    }

    public String getAcctOpenResult() {
        return AcctOpenResult;
    }

    public void setAcctOpenResult(String acctOpenResult) {
        AcctOpenResult = acctOpenResult;
    }

    public String getAcctOpenDesc() {
        return AcctOpenDesc;
    }

    public void setAcctOpenDesc(String acctOpenDesc) {
        AcctOpenDesc = acctOpenDesc;
    }

    public String getFundAcctOpenResult() {
        return FundAcctOpenResult;
    }

    public void setFundAcctOpenResult(String fundAcctOpenResult) {
        FundAcctOpenResult = fundAcctOpenResult;
    }

    public String getFundAcctOpenDesc() {
        return FundAcctOpenDesc;
    }

    public void setFundAcctOpenDesc(String fundAcctOpenDesc) {
        FundAcctOpenDesc = fundAcctOpenDesc;
    }

    public String getFundAcct() {
        return FundAcct;
    }

    public void setFundAcct(String fundAcct) {
        FundAcct = fundAcct;
    }

    public String getFundTxnAcct() {
        return FundTxnAcct;
    }

    public void setFundTxnAcct(String fundTxnAcct) {
        FundTxnAcct = fundTxnAcct;
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

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getIdNo() {
        return IdNo;
    }

    public void setIdNo(String idNo) {
        IdNo = idNo;
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

    public String getBindCardNo() {
        return BindCardNo;
    }

    public void setBindCardNo(String bindCardNo) {
        BindCardNo = bindCardNo;
    }

    public String getReservedPhone() {
        return ReservedPhone;
    }

    public void setReservedPhone(String reservedPhone) {
        ReservedPhone = reservedPhone;
    }

    public long getBankNo() {
        return bankNo;
    }

    public void setBankNo(long bankNo) {
        this.bankNo = bankNo;
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

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }
}
