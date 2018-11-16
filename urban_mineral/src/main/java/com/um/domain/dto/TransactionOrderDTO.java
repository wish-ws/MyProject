package com.um.domain.dto;

import com.um.domain.common.BaseDTO;
import com.um.domain.common.BaseDTO;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/14 16:47
 */
@Data
public class TransactionOrderDTO extends BaseDTO {


    private static final long serialVersionUID = -6647746730014962554L;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 订单编码
     */
    private String orderCode;

    /**
     * 订单类型
     * 1销售单2采购单
     */
    private String orderType;

    /**
     * 废品类型
     * 1废家电2废钢铁3废塑料4废玻璃5其他
     */
    private String itemType;

    /**
     * 废品重量
     */
    private String itemWeight;

    /**
     * 废品单价
     */
    private String itemPrice;

    /**
     * 订单金额
     */
    private String orderAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 买方用户id
     */
    private String buyerUserId;

    /**
     * 卖方用户id
     */
    private String sellerUserId;

    /**
     * 订单状态
     * 1待接单2待交货3待结算4已完成5已取消
     */
    private String orderStatus;

    /**
     * 订单接收人
     */
    private String orderReceiver;

    /**
     * 接收时间
     */
    private String receivedTime;



}
