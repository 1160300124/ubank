package com.ulaiber.web.model;

/**
 * 系统菜单实体类
 * Created by daiqingwen on 2017/7/17.
 */
public class Menu {
    private int id;         // ID
    private String name;    // 菜单名
    private String url;     // 菜单路径
    private String code;    // 菜单编码
    private String father;  // 菜单父类编码
    private String icon;    // 菜单图标
    private String sorting; // 排序号

    // get set
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }
}
