package com.um.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.um.common.exception.ServiceException;
import com.um.domain.dto.AddressDTO;
import com.um.domain.dto.UserDTO;
import com.um.service.UserService;
import com.um.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author : ws
 * @project : com.um
 * @description : 用户信息缓存
 * @date : 2018年11月12日18:23:10
 */
@Slf4j
@Component
public class UserCache {


    static LoadingCache<Integer, UserDTO> userCache = CacheBuilder.newBuilder()
            .maximumSize(2000)
            .expireAfterWrite(10L, TimeUnit.DAYS)
            .build(createUserCacheLoader());


    private static CacheLoader<Integer, UserDTO> createUserCacheLoader() {
        return new CacheLoader<Integer, UserDTO>() {
            @Override
            public UserDTO load(Integer userId) throws Exception {
                UserService userService = (UserService) SpringContextUtil.getBean(UserService.class);
                UserDTO userDTO = userService.queryUserInfoByUserId(userId);
                return userDTO;
            }
        };
    }

    public static UserDTO getCachedUserDTO(Integer userId) {
        try {
            return userCache.get(userId);
        } catch (ExecutionException e) {
            log.error("获取userDTO缓存失败，userId=" + userId,e);
            return null;
        }
    }

    public static void putUserToCache(Integer userId,UserDTO userDTO){
        userCache.put(userId,userDTO);
    }

    public static void removeUserCache(Integer userId){
        userCache.invalidate(userId);
    }


    


}
