package com.um.domain.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.um.domain.common.BaseDTO;
import com.um.domain.common.BaseDTO;
import com.um.util.DateUtil;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : ws
 * @project : com.um
 * @description : 回收订单
 * @date : 2018/11/13 17:32
 */
@Data
@Table(name = "t_recycle_order")
public class RecycleOrderPO extends BaseDTO {

    private static final long serialVersionUID = -2799099624581286138L;

    /**
     * 订单id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    /**
     * 订单编码
     */
    private String orderCode;

    /**
     * 废品类型
     */
    private Integer itemType;

    /**
     * 废品数量
     */
    private Integer itemQty;

    /**
     * 地址
     */
    private String address;

    /**
     * 备注
     */
    private String remark;

    /**
     * 订单状态1待接单2待回收3待结算4已完成5已取消
     */
    private Integer orderStatus;

    /**
     * 创建订单用户id
     */
    private Integer creatorUserId;

    /**
     * 订单接收人
     */
    private String orderReceiver;

    /**
     * 接收订单时间
     */
    private String receivedTime;


    public void setReceivedTime(String receivedTime) {
        if(StringUtils.isNotEmpty(receivedTime)){
            receivedTime = DateUtil.dateFormat(receivedTime,DateUtil.hour_format);
        }
        this.receivedTime = receivedTime;
    }
}
