package com.um.controller;

import com.um.cache.UserCache;
import com.um.common.exception.ParameterException;
import com.um.domain.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ws
 * @project : com.um
 * @description : 父controller
 * @date : 2018/8/24 18:00
 */
@Slf4j
@RestController
public class BaseController{


    /**
     * @description: 获取当前登陆用户
     * @author: ws
     * @date: 2018/11/14
     */
    public UserDTO getCurrentUser(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        Integer userId = (Integer) request.getAttribute("userId");
        UserDTO userDTO = UserCache.getCachedUserDTO(userId);
        if(null == userDTO){
            throw new ParameterException("当前用户缓存不存在");
        }
        return userDTO;
    }





}
