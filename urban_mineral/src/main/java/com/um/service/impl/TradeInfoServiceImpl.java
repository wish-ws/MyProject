package com.um.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.TradeInfoDTO;
import com.um.domain.po.TradeInfoPO;
import com.um.domain.request.TradeInfoQueryRequest;
import com.um.mapper.TradeInfoMapper;
import com.um.service.TradeInfoService;
import com.um.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : ws
 * @project : com.suton
 * @description :
 * @date : 2018/11/15 16:49
 */
@Service
public class TradeInfoServiceImpl implements TradeInfoService {


    @Autowired
    private TradeInfoMapper tradeInfoMapper;

    @Override
    public PaginationSupportDTO queryTradeInfoPage(TradeInfoQueryRequest tradeInfoQueryRequest) {

        PaginationSupportDTO<TradeInfoDTO> paginationSupportDTO = new PaginationSupportDTO();
        PageHelper.startPage(tradeInfoQueryRequest.getCurrentPage(), tradeInfoQueryRequest.getPageSize());

        List<TradeInfoDTO> tradeInfoDTOList = tradeInfoMapper.queryTradeInfoPage(tradeInfoQueryRequest);

        PageInfo<TradeInfoDTO> pageInfo = new PageInfo<>(tradeInfoDTOList);
        paginationSupportDTO.copyProperties(pageInfo,TradeInfoDTO.class);
        return paginationSupportDTO;
    }

    @Override
    public void createTradeInfo(TradeInfoDTO tradeInfoDTO) {

        TradeInfoPO tradeInfoPO = BeanUtil.transformBean(tradeInfoDTO,TradeInfoPO.class);
        tradeInfoMapper.insert(tradeInfoPO);

    }
}
