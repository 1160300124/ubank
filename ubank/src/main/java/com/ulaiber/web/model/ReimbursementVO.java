package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 报销传递的参数
 * Created by daiqingwen on 2017/9/11.
 */
public class ReimbursementVO implements Serializable {
    private Object data;
    private String reason;
    private String auditor;
    private String userid;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}