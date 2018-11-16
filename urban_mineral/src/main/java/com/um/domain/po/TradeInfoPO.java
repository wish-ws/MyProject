package com.um.domain.po;

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
 * @description : 供求信息
 * @date : 2018/11/14 16:27
 */
@Data
@Table(name = "t_trade_info")
public class TradeInfoPO extends BaseDTO {

    private static final long serialVersionUID = 2503005074614624213L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 交易类型1供应2采购
     */
    private Integer tradeType;

    /**
     * 废品类型
     * 1废家电2废钢铁3废塑料4废玻璃5其他
     */
    private Integer itemType;

    /**
     * 供求信息内存
     */
    private String content;

    /**
     * 状态1有效0无效
     */
    private Integer status;

    /**
     * 创建人用户id
     */
    private Integer creatorUserId;

}
