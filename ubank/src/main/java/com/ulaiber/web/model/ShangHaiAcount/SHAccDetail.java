package com.ulaiber.web.model.ShangHaiAcount;

import java.io.Serializable;

/**
 * 上海二类户账户明细
 * Created by daiqingwen on 2017/11/28.
 */
public class SHAccDetail implements Serializable{
    private String TxnRef;      //主机流水号
    private String TxnBsnId;    //业务流水号
    private String TxnDate;     //交易日期
    private String TxnTime;     //交易时间
    private String FlowCode;    //交易代码
    private String TheirRef;    //交易摘要
    private String TxnAmt;      //交易金额

    public String getTxnRef() {
        return TxnRef;
    }

    public void setTxnRef(String txnRef) {
        TxnRef = txnRef;
    }

    public String getTxnBsnId() {
        return TxnBsnId;
    }

    public void setTxnBsnId(String txnBsnId) {
        TxnBsnId = txnBsnId;
    }

    public String getTxnDate() {
        return TxnDate;
    }

    public void setTxnDate(String txnDate) {
        TxnDate = txnDate;
    }

    public String getTxnTime() {
        return TxnTime;
    }

    public void setTxnTime(String txnTime) {
        TxnTime = txnTime;
    }

    public String getFlowCode() {
        return FlowCode;
    }

    public void setFlowCode(String flowCode) {
        FlowCode = flowCode;
    }

    public String getTheirRef() {
        return TheirRef;
    }

    public void setTheirRef(String theirRef) {
        TheirRef = theirRef;
    }

    public String getTxnAmt() {
        return TxnAmt;
    }

    public void setTxnAmt(String txnAmt) {
        TxnAmt = txnAmt;
    }
}
