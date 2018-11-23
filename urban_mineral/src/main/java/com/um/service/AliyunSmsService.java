package com.um.service;

import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.AddressDTO;
import com.um.domain.dto.UserDTO;
import com.um.domain.request.AliyunSmsRequest;
import com.um.domain.request.UserQueryRequest;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 16:50
 */
public interface AliyunSmsService {

    boolean sendSms(AliyunSmsRequest aliyunSmsRequest);
}
