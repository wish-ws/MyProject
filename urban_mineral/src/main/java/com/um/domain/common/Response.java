package com.um.domain.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Response implements Serializable {

    private static final long serialVersionUID = 4520643165108139778L;

    /**
     * 结果(1：成功 0：失败)
     */
    protected Integer result;

    /**
     * 错误编码
     */
    protected String failCode;

    /**
     * 失败原因
     */
    protected String failReason;

    /**
     * 返回对象
     */
    protected Object model;

    public Response(){}
    public Response(Integer result){
        this.result=result;
    }
    public Response(Integer result, String failCode, String failReason){
        this.result=result;
        this.failCode=failCode;
        this.failReason=failReason;
    }

}
