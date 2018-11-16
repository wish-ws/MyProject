package com.um.common.enums;

/**
 * @author : ws
 * @project : com.um
 * @description : 行业认证状态
 * @date : 2018/11/14 20:05
 */
public enum VerificationStatusEnum {

    YES(1,"已认证"),
    NO(0,"未认证");

    public int key;

    public String desc;

    VerificationStatusEnum(int key,String desc) {
        this.key = key;
        this.desc = desc;
    }

}
