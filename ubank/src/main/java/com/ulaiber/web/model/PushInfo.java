package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 推送信息
 * Created by daiqingwen on 2017/9/14.
 */
public class PushInfo implements Serializable{
    private int type;
    private String content;
    private String title;

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
}
