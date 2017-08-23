package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 结果返回信息
 * Created by daiqingwen on 2017/8/23.
 */
public class ReturnData implements Serializable{
    //请求返回码
    private int code;

    //返回信息
    private String message;

    //返回数据
    private Object auditData;

    //返回数据
    private Object applyForData;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getAuditData() {
        return auditData;
    }

    public void setAuditData(Object auditData) {
        this.auditData = auditData;
    }

    public Object getApplyForData() {
        return applyForData;
    }

    public void setApplyForData(Object applyForData) {
        this.applyForData = applyForData;
    }
}
