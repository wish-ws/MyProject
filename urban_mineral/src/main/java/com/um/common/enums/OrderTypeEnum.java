package com.um.common.enums;

/**
 * @author : ws
 * @project : com.um
 * @description : 交易单类型
 * @date : 2018/11/14 20:36
 */
public enum OrderTypeEnum {

    SALE(1,"销售"),
    PURCHASE(2,"采购");

    public int key;
    public String desc;

    OrderTypeEnum(int key,String desc) {
        this.key = key;
        this.desc = desc;
    }

}
