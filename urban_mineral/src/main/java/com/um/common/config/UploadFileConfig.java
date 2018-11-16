package com.um.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @class_name: UploadFileConfig
 * @description: 上传配置，ftp配置
 * @author: ws
 * @date: 2018/8/2
 */
@Component
@ConfigurationProperties(prefix = "ftp")
@Data
public class UploadFileConfig {

    /**
     * 服务地址
     */
    private String addr;

    /**
     * 端口
     */
    private int port;

    /**
     * 服务器登录名
     */
    private String userName;

    /**
     * 用户登录秘密
     */
    private String passWord;

    /**
     * 图片域名
     */
    private String domainName;

    /**
     * ftp根路径
     */
    private String rootDir;


    /**
     * ftp 服务器 http协议
     */
    private String schema;

    /**
     * ftp 服务器域名
     */
    private String domain;








}
