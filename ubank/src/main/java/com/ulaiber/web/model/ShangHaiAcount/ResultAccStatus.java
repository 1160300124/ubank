package com.ulaiber.web.model.ShangHaiAcount;

import java.io.Serializable;

/**
 * 二类户返回状态
 * Created by daiqingwen on 2017/10/31.
 */
public class ResultAccStatus implements Serializable{

    //请求返回码
    private String code;

    //返回信息
    private String message;

    //返回数据
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
