package com.um.common.enums;

/**
 * @author : ws
 * @project : com.um
 * @description : 废品类型
 * @date : 2018/11/14 20:23
 */
public enum ItemTypeEnum {

    APPLIANCE(1,"家电"),
    STEEL(2,"钢铁"),
    PLASTIC(3,"塑料"),
    GLASS(4,"玻璃"),
    OTHER(5,"其他");


    public int key;

    public String desc;

    ItemTypeEnum(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
