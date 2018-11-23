package com.um.service;

import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.AddressDTO;
import com.um.domain.dto.UserDTO;
import com.um.domain.request.UserQueryRequest;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 16:50
 */
public interface UserService {

    PaginationSupportDTO queryUserPage(UserQueryRequest userQueryRequest);

    void createPlatformUser(UserDTO userDTO,String operator);

    void modifyPlatformUser(UserDTO userDTO, String operator);

    void modifyUserEnable(UserDTO userDTO);

    boolean sendVerifyCode(String accountName);

    void registerUser(UserDTO userDTO);

    UserDTO login(UserDTO userDTO);

    UserDTO queryUserInfoByUserId(Integer userId);

    void modifyUser(UserDTO userDTO);

    void resetPassword(UserDTO userDTO);

    AddressDTO queryUserAddressByUserId(Integer userId);

    void saveOrUpdateUserAddress(AddressDTO addressDTO);
}
