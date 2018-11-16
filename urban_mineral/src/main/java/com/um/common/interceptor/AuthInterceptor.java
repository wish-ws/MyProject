package com.um.common.interceptor;


import lombok.extern.slf4j.Slf4j;
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

//    @Autowired
//    private UserRemote userRemote;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        String menuUrlDev = request.getRequestURI();
//        String token = request.getHeader("Authorization");
//        if(StringUtils.isEmpty(token)){
//            log.error(request.getRequestURI() + "---接口缺失token");
//            response.getWriter().print(ErrorCodeEnum.UN_LOGIN.errorNo);
//            return false;
//        }
//        Jwt jwt = JwtUtil.parseJWT(token);
//        if(null == jwt || 0 == jwt.getResult()){
//            log.error(request.getRequestURI() + jwt.getParseResCode());
//            response.getWriter().print(ErrorCodeEnum.UN_LOGIN.errorNo);
//            return false;
//        }
//        String loginName = jwt.getLoginName();
//        String userName = jwt.getUserName();
//
//        //先判断登陆
//        boolean isLogin = UserCache.getLoginStatusByLoginName(loginName);
//        if(!isLogin){
//            log.error(loginName + "---该用户没有登录");
//            response.getWriter().print(ErrorCodeEnum.UN_LOGIN.errorNo);
//            return false;
//        }
//
//        //注销用户
//        if(menuUrlDev.equals("/*/logout")){
//            //移除登录状态
//            UserCache.removeUserCache(loginName);
//            log.error(loginName + "该用户已经注销");
//            response.getWriter().print(ErrorCodeEnum.UN_LOGIN.errorNo);
//            return false;
//        }
//
//
//        //判断权限
//
//
//
//        request.setAttribute("loginName",loginName);
        request.setAttribute("userId",1);
        return super.preHandle(request,response,handler);
    }
}
