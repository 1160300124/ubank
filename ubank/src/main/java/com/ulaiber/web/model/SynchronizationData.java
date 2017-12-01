package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 服务器与客户端需要同步的数据
 * Created by 戴庆文 on 2017/8/31.
 */
public class SynchronizationData implements Serializable {
    private long id;             //用户ID
    private String username;    //用户名
    private String deptName;    //部门名称
    private String image;       //头像
    private String mobile;      //电话
    private String disabled;    //是否作废；0 否，1 是

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
