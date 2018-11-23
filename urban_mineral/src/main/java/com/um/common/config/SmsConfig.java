package com.um.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @class_name: SmsConfig
 * @description: 短信配置
 * @author: ws
 * @date: 2018/8/2
 */
@Component
@ConfigurationProperties(prefix = "sms")
@Data
public class SmsConfig {

    /**
     * 必填:短信签名
     */
    private String signName;

    /**
     * 必填:短信模板
     */
    private String templateCode;

    /**
     * 短信模板参数
     */
    private String templateParam;


}
