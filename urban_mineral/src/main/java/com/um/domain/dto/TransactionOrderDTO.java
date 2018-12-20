package com.um.domain.dto;

import com.um.domain.common.BaseDTO;
import com.um.domain.common.BaseDTO;
import com.um.util.DateUtil;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private Integer orderId;

    /**
     * 订单编码
     */
    private String orderCode;

    /**
     * 订单类型
     * 1销售单2采购单
     */
    private Integer orderType;

    /**
     * 废品类型
     * 1废家电2废钢铁3废塑料4废玻璃5其他
     */
    private Integer itemType;

    /**
     * 废品重量
     */
    private Double itemWeight;

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
    private Integer buyerUserId;

    /**
     * 买方用户名
     */
    private String buyerUserName;

    /**
     * 买方登录名
     */
    private String buyerAccountName;

    /**
     * 卖方用户id
     */
    private Integer sellerUserId;

    /**
     * 卖方用户名
     */
    private String sellerUserName;

    /**
     * 卖方登录名
     */
    private String sellerAccountName;

    /**
     * 订单状态
     * 1待接单2待交货3待结算4已完成5已取消
     */
    private Integer orderStatus;

    /**
     * 订单接收人
     */
    private String orderReceiver;

    /**
     * 接收时间
     */
    private String receivedTime;

    /**
     * 支付凭证
     */
    private List<BusinessImgDTO> businessImgDTOList;

    /**
     * 创建人用户id
     */
    private Integer creatorUserId;

    /**
     * 供求消息创建人id
     */
    private Integer tradeInfoCreatorUserId;

    public void setReceivedTime(String receivedTime) {
        if(StringUtils.isNotEmpty(receivedTime)){
            receivedTime = DateUtil.dateFormat(receivedTime,DateUtil.hour_format);
        }
        this.receivedTime = receivedTime;
    }


    public void setBusinessImgDTOList(List<BusinessImgDTO> businessImgDTOList) {
        if(CollectionUtils.isEmpty(businessImgDTOList)){
            businessImgDTOList = new ArrayList<>();
        }
        this.businessImgDTOList = businessImgDTOList;
    }
}
