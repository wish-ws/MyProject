package com.um.service;

import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.RecycleOrderDTO;
import com.um.domain.request.OrderQueryRequeset;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/14 19:41
 */
public interface RecycleOrderService {

    /**
     * 创建回收订单
     * @param recycleOrderDTO
     * @return
     */
    public void createRecycleOrder(RecycleOrderDTO recycleOrderDTO);

    /**
     * 分页查询回收单
     * @param orderQueryRequeset
     * @return
     */
    public PaginationSupportDTO queryRecycleOrderPage(OrderQueryRequeset orderQueryRequeset);

    /**
     * 查询回收订单详情
     * @param orderCode
     * @return
     */
    public RecycleOrderDTO queryRecycleOrderDetail(String orderCode);


}
