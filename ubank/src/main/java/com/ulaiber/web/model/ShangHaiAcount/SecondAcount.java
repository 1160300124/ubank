package com.ulaiber.web.model.ShangHaiAcount;

import java.io.Serializable;

/**
 * 上海银行二类账户信息
 * Created by daiqingwen on 2017/10/23.
 */
public class SecondAcount implements Serializable {
    private long userid;                //用户ID
    private String EacctNo;             //E账户主账户
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
}
