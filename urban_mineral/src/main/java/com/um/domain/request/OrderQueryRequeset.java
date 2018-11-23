package com.um.domain.request;

import com.um.domain.common.BaseRequest;
import lombok.Data;

/**
 * @author : ws
 * @project : com.um
 * @description : 订单查询请求类
 * @date : 2018/11/14 19:44
 */
@Data
public class OrderQueryRequeset extends BaseRequest {


    private static final long serialVersionUID = 6769521789019071978L;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 创建人用户id
     */
    private Integer creatorUserId;

    /**
     * 订单编号
     */
    private String orderCode;


}
