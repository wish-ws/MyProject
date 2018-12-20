package com.um.mapper;

import com.um.common.MyMapper;
import com.um.domain.dto.TransactionOrderDTO;
import com.um.domain.po.RecycleOrderPO;
import com.um.domain.po.TransactionOrderPO;
import com.um.domain.request.OrderQueryRequeset;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/14 20:42
 */
@Component("transactionOrderMapper")
public interface TransactionOrderMapper extends MyMapper<TransactionOrderPO> {


    public List<TransactionOrderDTO> queryTransactionOrderPage(OrderQueryRequeset orderQueryRequeset);
}
