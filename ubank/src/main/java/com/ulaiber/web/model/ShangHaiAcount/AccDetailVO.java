package com.ulaiber.web.model.ShangHaiAcount;

import java.io.Serializable;

/**
 * 上海二类户查询账户明细参数
 * Created by daiqingwen on 2017/11/28.
 */
public class AccDetailVO implements Serializable{
    private String SubAcctNo;   //余额理财子帐号
    private String Currency;    //币种
    private String BeginDt;     //开始日期
    private String EndDt;       //结束日期
    private int PageSize;       //显示记录条数
    private int SkipRecord;     //记录显示起始数

    public String getSubAcctNo() {
        return SubAcctNo;
    }

    public void setSubAcctNo(String subAcctNo) {
        SubAcctNo = subAcctNo;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getBeginDt() {
        return BeginDt;
    }

    public void setBeginDt(String beginDt) {
        BeginDt = beginDt;
    }

    public String getEndDt() {
        return EndDt;
    }

    public void setEndDt(String endDt) {
        EndDt = endDt;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public int getSkipRecord() {
        return SkipRecord;
    }

    public void setSkipRecord(int skipRecord) {
        SkipRecord = skipRecord;
    }
}
