package com.um.mapper;

import com.um.common.MyMapper;
import com.um.domain.dto.RecycleOrderDTO;
import com.um.domain.po.RecycleOrderPO;
import com.um.domain.request.OrderQueryRequeset;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/14 20:42
 */
@Component("recycleOrderMapper")
public interface RecycleOrderMapper extends MyMapper<RecycleOrderPO> {

    public List<RecycleOrderDTO> queryRecycleOrderPage(OrderQueryRequeset orderQueryRequeset);

}
