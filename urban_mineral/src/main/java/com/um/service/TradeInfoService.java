package com.um.service;

import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.TradeInfoDTO;
import com.um.domain.request.TradeInfoQueryRequest;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 16:48
 */
public interface TradeInfoService {

    PaginationSupportDTO queryTradeInfoPage(TradeInfoQueryRequest tradeInfoQueryRequest);

    void createTradeInfo(TradeInfoDTO tradeInfoDTO);

    void deleteTradeInfo(Integer id);
}
