package com.um.common.enums;

/**
 * 状态
 */
public enum StatusEnum {

    VALID(1,"有效"),
    INVALID(0,"无效");

    public int key;
    public String desc;

    StatusEnum(int key,String desc) {
        this.key = key;
        this.desc = desc;
    }


}
