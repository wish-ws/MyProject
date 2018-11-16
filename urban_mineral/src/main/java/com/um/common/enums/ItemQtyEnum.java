package com.um.common.enums;

/**
 * @author : ws
 * @project : com.um
 * @description : 废品数量等级
 * @date : 2018/11/14 20:28
 */
public enum ItemQtyEnum {

    _1(1,"1-10台"),
    _2(2,"11-50台"),
    _3(3,"50台以上"),
    _4(4,"10-500kg"),
    _5(5,"500-1000kg"),
    _6(6,"1000kg以上");


    public int key;

    public String desc;

    ItemQtyEnum(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
