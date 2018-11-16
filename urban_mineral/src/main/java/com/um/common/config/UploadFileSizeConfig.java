package com.um.common.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * @author : ws
 * @project : com.um
 * @description : 系统级文件上传大小配置
 * @date : 2018/8/3 10:06
 */
@Configuration
public class UploadFileSizeConfig {


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("30MB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("102400KB");
        return factory.createMultipartConfig();
    }
}
