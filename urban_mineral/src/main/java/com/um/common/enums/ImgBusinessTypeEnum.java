package com.um.common.enums;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/14 20:32
 */
public enum ImgBusinessTypeEnum {

    NEWS(1,"资讯"),
    STATEMENT(2,"支付凭证");

    public int key;
    public String desc;

    ImgBusinessTypeEnum(int key,String desc) {
        this.key = key;
        this.desc = desc;
    }


}
