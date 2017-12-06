package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 推送信息
 * Created by daiqingwen on 2017/9/14.
 */
public class PushInfo implements Serializable{
    //推送待审批的记录 0
    //推送已审批的记录 1
    //推送交易信息 2
    //推送个人身份证上传反馈 3
    //推送公告 4
    private int type;

    //推送内容
    private String content;

    //推送标题
    private String title;

    //内容ID
    private long id;

    //状态，0 失败 1 成功
    private String status;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
