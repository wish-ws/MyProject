package com.um.common.enums;

/**
 * @author : ws
 * @project : com.um
 * @description : 回收单状态
 * @date : 2018/11/14 19:46
 */
public enum RecycleOrderStatusEnum {

    WAIT_FOR_RECEIVE(1,"待接单"),
    WAIT_FOR_RECYCLE(2,"待回收"),
    WAIT_FOR_SETTLE(3,"待结算"),
    COMPLETE(4,"已完成"),
    CANCEL(5,"已取消");


    public int key;
    public String desc;

    RecycleOrderStatusEnum(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
