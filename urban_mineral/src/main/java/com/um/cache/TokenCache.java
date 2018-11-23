package com.um.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * @author : ws
 * @project : com.um
 * @description : token缓存，后台控制token有效性
 * @date : 2018年11月12日18:23:10
 */
@Slf4j
@Component
public class TokenCache {


    static LoadingCache<String, Boolean> tokenCache = CacheBuilder.newBuilder()
            .maximumSize(2000)
//            .expireAfterWrite(10L, TimeUnit.DAYS)
            .build(createTokenCacheLoader());


    private static CacheLoader<String, Boolean> createTokenCacheLoader() {
        return new CacheLoader<String, Boolean>() {
            @Override
            public Boolean load(String accountName) throws Exception {
                return false;
            }
        };
    }

    public static boolean isExist(String token) {
        try {
            Boolean flag = tokenCache.get(token);
            if(null == flag){
                return false;
            }else{
                return flag;
            }
        } catch (ExecutionException e) {
            log.error("获取token缓存失败",e);
            return false;
        }
    }

    public static void putTokenToCache(String token,boolean flag){
        tokenCache.put(token,flag);
    }

    public static void removeTokenCache(String token){
        tokenCache.invalidate(token);
    }


}
