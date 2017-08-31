package com.ulaiber.web.model;

import java.io.Serializable;

/**
 * 加班特有的字段
 * Created by daiqingwen on 2017/8/30.
 */
public class OvertimeVO implements Serializable{

    private String holiday;     //是否节假日；0 否，1 是
    private String mode;        //加班核算方式；0 调休，1 发工资

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
