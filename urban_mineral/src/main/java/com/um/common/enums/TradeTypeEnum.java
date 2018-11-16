package com.um.common.enums;

/**
 * @author : ws
 * @project : com.um
 * @description : 供求类型
 * @date : 2018/11/14 20:30
 */
public enum TradeTypeEnum {

    SUPPLY(1,"供应"),
    PURCHASE(2,"采购");

    public int key;
    public String desc;

    TradeTypeEnum(int key,String desc) {
        this.key = key;
        this.desc = desc;
    }


}
