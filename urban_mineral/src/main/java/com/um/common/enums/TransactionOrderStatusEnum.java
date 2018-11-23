package com.um.common.enums;

/**
 * @author : ws
 * @project : com.um
 * @description : 交易单状态
 * @date : 2018/11/14 19:46
 */
public enum TransactionOrderStatusEnum {

    WAIT_FOR_RECEIVE(1,"待接单"),
    WAIT_FOR_DELIVER(2,"待交货"),
    WAIT_FOR_SETTLE(3,"待结算-未支付"),
    WAIT_FOR_AUDIT(4,"待结算-未审核"),
    COMPLETE(5,"已完成"),
    CANCEL(6,"已取消");


    public int key;
    public String desc;

    TransactionOrderStatusEnum(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }


    public static String getDescByKey(int key){
        String desc = null;
        for (TransactionOrderStatusEnum transactionOrderStatusEnum : TransactionOrderStatusEnum.values()) {
            if(transactionOrderStatusEnum.key == key){
                desc = transactionOrderStatusEnum.desc;
            }
        }
        return desc;
    }
}
