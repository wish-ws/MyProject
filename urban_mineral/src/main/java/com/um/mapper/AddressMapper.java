package com.um.mapper;

import com.um.common.MyMapper;
import com.um.domain.dto.PlatformStatisticsDTO;
import com.um.domain.dto.UserDTO;
import com.um.domain.po.AddressPO;
import com.um.domain.po.UserPO;
import com.um.domain.request.UserQueryRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 15:19
 */
@Component("addressMapper")
public interface AddressMapper extends MyMapper<AddressPO> {


}
