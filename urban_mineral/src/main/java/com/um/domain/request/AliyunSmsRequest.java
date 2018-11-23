package com.um.domain.request;

import lombok.Data;

/**
 * @author : ws
 * @project : com.um
 * @description : 阿里云短信服务请求类
 * @date : 2018/11/22 16:14
 */
@Data
public class AliyunSmsRequest {

    /**
     * 必填:待发送手机号
     */
    private String phoneNumbers;

    /**
     * 必填:短信签名
     */
    private String signName;

    /**
     * 必填:短信模板
     */
    private String templateCode;

    /**
     * 可选:模板中的变量替换JSON串,
     * 如模板内容为   "亲爱的${name},您的验证码为${code}"  时,
     * 此处的值为    "{\"name\":\"Tom\", \"code\":\"123\"}"
     */
    private String templateParam;



}
