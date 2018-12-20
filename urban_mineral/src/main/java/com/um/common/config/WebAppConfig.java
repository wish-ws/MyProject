package com.um.common.config;


import com.um.common.filter.CommonFilter;
import com.um.common.interceptor.AuthInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 应用配置
 * Created by ws on 2018/11/12.
 */
@SpringBootConfiguration
public class WebAppConfig implements WebMvcConfigurer {

    /**
     * @description: 注册拦截器
     * @author: ws
     * @date: 2018/8/28
     */
    @Override  
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径  
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/*/login")
                .excludePathPatterns("/*/register")
                .excludePathPatterns("/*/verifycode/send/*")
                .excludePathPatterns("/*/password/reset")
                .excludePathPatterns("/*/news/page")
                .excludePathPatterns("/*/news/detail/*")
                .excludePathPatterns("/platform/tradeInfo/page")
                .excludePathPatterns("/platform/config")
                .excludePathPatterns("/*/upload/*");
    }


    /**
     * @description: 注册过滤器
     * @author: ws
     * @date: 2018/8/28
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration1() {
        //新建过滤器注册类
        FilterRegistrationBean registration = new FilterRegistrationBean();
        // 添加我们写好的过滤器
        registration.setFilter( new CommonFilter());
        // 设置过滤器的URL模式
        registration.addUrlPatterns("/*");
        return registration;
    }
}