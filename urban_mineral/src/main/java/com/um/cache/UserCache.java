package com.um.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.um.common.exception.ServiceException;
import com.um.domain.dto.AddressDTO;
import com.um.domain.dto.UserDTO;
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


                //测试代码
                UserDTO userDTO = new UserDTO();
                userDTO.setAccountName("wish");
                userDTO.setAccountType(1);

                userDTO.setAvatarImgPath(null);
                userDTO.setRoleCodes("R001");
                userDTO.setStatus(1);
                userDTO.setUserId(1);
                userDTO.setUserName("你松哥");
                userDTO.setVerificationContent("钢铁");
                userDTO.setVerificationStatus(1);

                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setAddressDetail("16栋1101");
                addressDTO.setCityCode("SZX");
                addressDTO.setCityName("深圳");
                addressDTO.setContactName("王松");
                addressDTO.setContactTel("13713794964");
                addressDTO.setId(1);
                addressDTO.setProvinceName("广东省");
                addressDTO.setRegionName("龙华新区");
                addressDTO.setUserId(1);

                userDTO.setAddressDTO(addressDTO);
                return userDTO;
                //测试代码
            }
        };
    }

    public static UserDTO getCachedUserDTO(Integer userId) {
        try {
            return userCache.get(userId);
        } catch (ExecutionException e) {
            throw new ServiceException("获取userDTO缓存失败，userId=" + userId,e);
        }
    }

    public static void putUserToCache(Integer userId,UserDTO userDTO){
        userCache.put(userId,userDTO);
    }

    public static void removeUserCache(Integer userId){
        userCache.invalidate(userId);
    }


    


}
