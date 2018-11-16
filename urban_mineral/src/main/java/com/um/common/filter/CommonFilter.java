package com.um.common.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : ws
 * @project : com.um
 * @description : 过滤器，统一设置loginName参数
 * @date : 2018/8/28 16:41
 */
@Slf4j
public class CommonFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        //vue前后端分离，会导致跨域请求，每次请求发送预检请求，导致请求后台两次，故这里设置1小时缓存
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        //前端跨域请求携带credentials，后台这里设置true
        response.setHeader("Access-Control-Allow-Credentials", "true");
        //IE8+可使用XDomainRequest进行跨域请求
        response.setHeader("XDomainRequestAllowed","1");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
