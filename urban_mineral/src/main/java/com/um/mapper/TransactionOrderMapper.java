package com.um.mapper;

import com.um.common.MyMapper;
import com.um.domain.po.RecycleOrderPO;
import com.um.domain.po.TransactionOrderPO;
import org.springframework.stereotype.Component;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/14 20:42
 */
@Component("transactionOrderMapper")
public interface TransactionOrderMapper extends MyMapper<TransactionOrderPO> {


}
