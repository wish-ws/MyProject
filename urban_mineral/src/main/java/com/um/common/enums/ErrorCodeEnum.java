package com.um.common.enums;

/**
 * 系统错误编码
 */
public enum ErrorCodeEnum {



    /***********系统异常类错误代码 以0开头***********/
    SYSTEM_EXCEPTION("000","SYSTEM_EXCEPTION","系统异常"),
    INVALID_INPUTPARAM("001","INVALID_INPUTPARAM","无效的入参对象"),
    UNKNOWN_EXCEPTION("002","UNKNOWN_EXCEPTION","未知异常"),
    CONNECT_TIME_OUT("003","CONNECT_TIME_OUT","连接超时"),
    READ_TIME_OUT("004","READ_TIME_OUT","访问超时"),




    /********登陆异常 以1开头***/
    LOGIN_EXCEPTION("100","LOGIN_EXCEPTION","系统繁忙，请您稍后再试。"),
    UN_LOGIN("101","UN_LOGIN","还未登录，请先登录"),
    LOGIN_ERROR_USERNAME_OR_PASSWORD_ERROR("102","LOGIN_USERNAME_OR_PASSWORD_ERROR","用户名密码错误"),
    LOGIN_ERROR_USERNAME_NOT_EXIST("103","LOGIN_USERNAME_NOT_EXIST","用户不存在"),
    NO_PERMISSION("105","NO_PERMISSION","权限不足"),
    EXCEPTION_SIGNATURE("106","EXCEPTION_SIGNATURE","签名异常"),
    FALI_SIGNATURE("107","FALI_SIGNATURE","签名验证失败"),
    EXPIRED_SIGNATURE("108","EXPIRED_SIGNATURE","签名过期"),
    NO_SIGNATURE("109","NO_SIGNATURE","没有签名"),
    REGET_TOKEN("110","REFRESH_TOKEN","重新获取token");











    public String errorNo;
    public String errorCode;//业务场景编号
    public String errorDesc;//业务场景描述

    private ErrorCodeEnum(String errorNo, String errorCode, String errorDesc)
    {
        this.errorNo = errorNo;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    public static String getKeyByValue(String errorCode) {
        String key = "000";
        for(ErrorCodeEnum errorCodeEnum : ErrorCodeEnum.values()) {
            if(errorCodeEnum.errorCode.equals(errorCode)) {
                key = errorCodeEnum.errorNo;
                break;
            }
        }
        return key;
    }

    public static String getDescByValue(String errorCode) {
        String desc = "";
        for(ErrorCodeEnum errorCodeEnum : ErrorCodeEnum.values()) {
            if(errorCodeEnum.errorCode.equals(errorCode)) {
                desc = errorCodeEnum.errorDesc;
                break;
            }
        }
        return desc;
    }

    public static String getErrorCodeByKey(String key) {
        String errorCode = "";
        for(ErrorCodeEnum errorCodeEnum : ErrorCodeEnum.values()) {
            if(errorCodeEnum.errorNo.equals(key)) {
                errorCode = errorCodeEnum.errorCode;
                break;
            }
        }
        return errorCode;
    }
}
