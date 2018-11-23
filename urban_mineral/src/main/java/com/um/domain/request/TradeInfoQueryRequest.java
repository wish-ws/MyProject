package com.um.domain.request;

import com.um.domain.common.BaseRequest;
import lombok.Data;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 17:34
 */
@Data
public class TradeInfoQueryRequest extends BaseRequest {


    private static final long serialVersionUID = 1602259094862770451L;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 废品类型
     */
    private Integer itemType;

    /**
     * 交易类型
     */
    private Integer tradeType;

    /**
     * 供求信息发布人
     */
    private Integer creatorUserId;

}
