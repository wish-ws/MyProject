package com.um.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.um.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * @author : ws
 * @project : com.um
 * @description : token缓存，后台控制token有效性（启用/禁用用户，重置密码，退出登陆……）
 * @date : 2018年11月12日18:23:10
 */
@Slf4j
@Component
public class TokenCache {


    static LoadingCache<Integer, String> tokenCache = CacheBuilder.newBuilder()
            .maximumSize(2000)
            .initialCapacity(10)
            .build(createTokenCacheLoader());


    private static CacheLoader<Integer, String> createTokenCacheLoader() {
        return new CacheLoader<Integer, String>() {
            @Override
            public String load(Integer userId) throws Exception {
                return null;
            }
        };
    }

    public static boolean isExist(Integer userId) {
        try {
            String token = tokenCache.get(userId);
            if(StringUtils.isEmpty(token)){
                return false;
            }else{
                return true;
            }
        } catch (Exception e) {
            log.error("获取token缓存失败",e);
            return false;
        }
    }

    public static String get(Integer userId){
        try {
            return tokenCache.get(userId);
        } catch (Exception e) {
            log.error("获取token缓存失败，userId=" + userId,e);
            return null;
        }
    }

    public static void put(Integer userId,String token){
        tokenCache.put(userId,token);
    }

    public static void remove(Integer userId){
        tokenCache.invalidate(userId);
    }

}
