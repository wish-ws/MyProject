package com.um.controller;

import com.um.cache.UserCache;
import com.um.common.enums.AccountTypeEnum;
import com.um.common.enums.PlatformRoleCodeEnum;
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



    public boolean hasRecycleOrderPermission(){
        //交易端回收订单操作，只有平台管理员和交易端操作员有权限
        Integer accountType = this.getCurrentUser().getAccountType();
        String roleCodes = this.getCurrentUser().getRoleCodes();
        boolean hasPermission = false;
        if (accountType.intValue() == AccountTypeEnum.BACK_STAGE.key
                && (roleCodes.indexOf(PlatformRoleCodeEnum.ADMIN.code) >= 0 || roleCodes.indexOf(PlatformRoleCodeEnum.TRANSACTION_OPR.code) >= 0)
                ) {
            hasPermission = true;
        }
        return hasPermission;
    }


    public boolean hasTransactionOrderPermission(){
        //交易订单操作，只有平台管理员/交易端操作员/交易端用户 有权限
        Integer accountType = this.getCurrentUser().getAccountType();
        String roleCodes = this.getCurrentUser().getRoleCodes();
        boolean hasPermission = false;
        if (accountType.intValue() == AccountTypeEnum.BACK_STAGE.key
                && (roleCodes.indexOf(PlatformRoleCodeEnum.ADMIN.code) >= 0 || roleCodes.indexOf(PlatformRoleCodeEnum.TRANSACTION_OPR.code) >= 0)
                ) {
            hasPermission = true;
        }
        if (accountType.intValue() == AccountTypeEnum.PLATFORM.key){
            hasPermission = true;
        }
        return hasPermission;
    }


    public boolean hasPlatformAdminPermission(){
        //平台管理权限
        boolean hasPermission = false;
        Integer accountType = this.getCurrentUser().getAccountType();
        String roleCodes = this.getCurrentUser().getRoleCodes();
        if (accountType.intValue() == AccountTypeEnum.BACK_STAGE.key && roleCodes.indexOf(PlatformRoleCodeEnum.ADMIN.code) >= 0){
            hasPermission = true;
        }
        return hasPermission;
    }



}
