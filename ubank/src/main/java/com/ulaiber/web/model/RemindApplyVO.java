package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 申请提醒记录
 * Created by daiqingwen on 2017/8/31.
 */
public class RemindApplyVO implements Serializable{
    private int count;
    private Object date;
    private Object type;
    private Object applyName;
    private Object reason;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getApplyName() {
        return applyName;
    }

    public void setApplyName(Object applyName) {
        this.applyName = applyName;
    }

    public Object getReason() {
        return reason;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }
}
