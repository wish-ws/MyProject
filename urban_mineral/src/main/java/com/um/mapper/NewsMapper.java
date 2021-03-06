package com.um.mapper;

import com.um.common.MyMapper;
import com.um.domain.dto.TradeInfoDTO;
import com.um.domain.po.NewsPO;
import com.um.domain.po.TradeInfoPO;
import com.um.domain.request.TradeInfoQueryRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 17:59
 */
@Component("newsMapper")
public interface NewsMapper extends MyMapper<NewsPO> {


}
