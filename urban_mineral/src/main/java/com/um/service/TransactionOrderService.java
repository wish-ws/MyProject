package com.um.service;

import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.RecycleOrderDTO;
import com.um.domain.dto.TransactionOrderDTO;
import com.um.domain.request.OrderQueryRequeset;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 16:49
 */
public interface TransactionOrderService {

    void createTransactionOrder(TransactionOrderDTO transactionOrderDTO);

    PaginationSupportDTO queryTransactionOrderPage(OrderQueryRequeset orderQueryRequeset);

    TransactionOrderDTO queryTransactionOrderDetail(String orderCode);

    /**
     * 接单，交货，结算，审核，取消
     * @param transactionOrderDTO
     * @param modifyType
     * @return
     */
    String modifyTransactionOrder(TransactionOrderDTO transactionOrderDTO,Integer modifyType);


}
