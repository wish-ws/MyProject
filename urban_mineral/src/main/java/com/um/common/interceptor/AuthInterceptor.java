package com.um.common.interceptor;


import com.um.cache.TokenCache;
import com.um.cache.UserCache;
import com.um.common.enums.ErrorCodeEnum;
import com.um.domain.common.Jwt;
import com.um.service.UserService;
import com.um.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ws
 * @project : com.um
 * @description : 简易权限拦截器
 * @date : 2018/8/24 18:07
 */
@Slf4j
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        //判断接口是否带参token
        String token = request.getHeader("Authorization");
        if(StringUtils.isEmpty(token)){
            log.error(request.getRequestURI() + "---接口缺失token");
            response.getWriter().print(ErrorCodeEnum.UN_LOGIN.errorNo);
            return false;
        }

        //解析token
        Jwt jwt = JwtUtil.parseJWT(token);
        if(null == jwt || 0 == jwt.getResult()){
            log.error(request.getRequestURI() + jwt.getParseResCode());
            response.getWriter().print(ErrorCodeEnum.UN_LOGIN.errorNo);
            return false;
        }
        Integer userId = jwt.getUserId();


        //判断token后台是否有效
        boolean isExist = TokenCache.isExist(userId);
        if(!isExist){
            log.error(userId + "---该用户登录超时");
            response.getWriter().print(ErrorCodeEnum.UN_LOGIN.errorNo);
            return false;
        }


        //退出登陆
        if(requestURI.equals("/*/logout")){
            //移除登陆token缓存
            TokenCache.remove(userId);
            log.error(userId + "---该用户已经退出登陆");
            response.getWriter().print(ErrorCodeEnum.UN_LOGIN.errorNo);
            return false;
        }

        request.setAttribute("userId",userId);
        return super.preHandle(request,response,handler);
    }
}
