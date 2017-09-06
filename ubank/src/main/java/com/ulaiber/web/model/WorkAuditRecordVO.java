package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 工作审批记录实体类
 * Created by daiqingwen on 2017/8/31.
 */
public class WorkAuditRecordVO implements Serializable {
    private int mark;       //标识；0 个人待审批记录标识 ，1 个人已审批记录标识
    private int count;      //数量
    private Object date;    //最新的那条记录日期
    private Object status;  //最新的那条记录当前所处状态

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

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

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }
}
