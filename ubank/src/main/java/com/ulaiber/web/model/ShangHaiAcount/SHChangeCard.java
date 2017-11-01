package com.ulaiber.web.model.ShangHaiAcount;

import java.io.Serializable;

/**
 * 改绑信息
 * Created by daiqingwen on 2017/10/25.
 */
public class SHChangeCard implements Serializable{
    private long id;
    private long userid;                //用户ID
    private String SubAcctNo;           //平台理财专属子账户
    private String ProductCd;           //理财产品参数
    private String CustName;            //姓名
    private String IdNo;                //身份证号
    private String BindCardNo;          //原绑定银行卡号
    private String NewCardNo;           //新绑定银行卡号
    private String ReservedPhone;       //银行卡预留手机号 银联代扣项目必输,长度为11，第一位为1
    private String NewReservedPhone;    //新银行卡预留手机号 银联代扣项目必输,长度为11，第一位为1
    private String ModiType;            //修改类型  00:换卡 01:修改绑定卡手机号
    private String StatusCode;          //返回结果码
    private String ServerStatusCode;    //返回结果信息
    private String SPRsUID;             //主机流水号
    private String RqUID;              //请求流水号

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

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getIdNo() {
        return IdNo;
    }

    public void setIdNo(String idNo) {
        IdNo = idNo;
    }

    public String getBindCardNo() {
        return BindCardNo;
    }

    public void setBindCardNo(String bindCardNo) {
        BindCardNo = bindCardNo;
    }

    public String getNewCardNo() {
        return NewCardNo;
    }

    public void setNewCardNo(String newCardNo) {
        NewCardNo = newCardNo;
    }

    public String getReservedPhone() {
        return ReservedPhone;
    }

    public void setReservedPhone(String reservedPhone) {
        ReservedPhone = reservedPhone;
    }

    public String getNewReservedPhone() {
        return NewReservedPhone;
    }

    public void setNewReservedPhone(String newReservedPhone) {
        NewReservedPhone = newReservedPhone;
    }

    public String getModiType() {
        return ModiType;
    }

    public void setModiType(String modiType) {
        ModiType = modiType;
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
}
