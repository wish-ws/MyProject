package com.um.common.enums;

/**
 * @author : ws
 * @project : com.um
 * @description : 用户类型
 * @date : 2018/11/14 20:01
 */
public enum AccountTypeEnum {

    BACK_STAGE(1,"后台管理用户"),
    RECYCLE(2,"回收用户"),
    PLATFORM(3,"交易用户");

    public int key;
    public String desc;

    AccountTypeEnum(int key,String desc) {
        this.key = key;
        this.desc = desc;
    }
}
