package com.um.common.enums;

/**
 * @Auther: ws
 * @Date: 2018年11月14日21:33:02
 * @Description: 编码开头
 */
public enum CodePrefitEnum {

    RECYCLE(1,"R"),
    TRANSACTION(2,"T");


    public int key;
    public String desc;


    CodePrefitEnum(int key,String desc) {
        this.key = key;
        this.desc = desc;
    }
}
