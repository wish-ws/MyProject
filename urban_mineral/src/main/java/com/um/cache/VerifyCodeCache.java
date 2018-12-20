package com.um.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author : ws
 * @project : com.um
 * @description : 注册发送的手机验证码缓存
 * @date : 2018年11月22日18:23:10
 */
@Slf4j
@Component
public class VerifyCodeCache {

    static LoadingCache<String, String> verifyCodeCache = CacheBuilder.newBuilder()
            .maximumSize(2000)
            .initialCapacity(10)
            .expireAfterAccess(60L, TimeUnit.SECONDS)//60s不读写就过期了
            .build(createVerifyCodeCacheLoader());


    private static CacheLoader<String, String> createVerifyCodeCacheLoader() {
        return new CacheLoader<String, String>() {
            @Override
            public String load(String accountName) throws Exception {
                return null;
            }
        };
    }

    public static void put(String accountName,String verifyCode){
        verifyCodeCache.put(accountName,verifyCode);
    }

    public static String get(String accountName){
        try {
            return verifyCodeCache.get(accountName);
        } catch (Exception e) {
            log.error("获取验证码缓存失败,accountName=" + accountName,e);
            return null;
        }
    }


}
