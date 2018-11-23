package com.um.mapper;

import com.um.common.MyMapper;
import com.um.domain.dto.BusinessImgDTO;
import com.um.domain.dto.TradeInfoDTO;
import com.um.domain.po.BusinessImgPO;
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
@Component("businessImgMapper")
public interface BusinessImgMapper extends MyMapper<BusinessImgPO> {



    public List<BusinessImgDTO> queryBusinessImgList(String businessCode, Integer businessType);


}
