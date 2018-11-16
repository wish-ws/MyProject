package com.um.mapper;

import com.um.common.MyMapper;
import com.um.domain.po.UserPO;
import org.springframework.stereotype.Component;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 15:19
 */
@Component("userMapper")
public interface UserMapper extends MyMapper<UserPO> {


}
