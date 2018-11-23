package com.um.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/20 14:58
 */
@Data
public class PlatformStatisticsDTO implements Serializable {


    /**
     * 交易端总交易额
     */
    private String platformTotalTurnover;

    /**
     * 用户端总交易额
     */
    private String clientTotalTurnover;

    /**
     * 交易端总用户数
     */
    private String platformTotalUserQty;

    /**
     * 用户端总用户数
     */
    private String clientTotalUserQty;
}
