package com.um.domain.dto;

import com.um.domain.common.BaseDTO;
import com.um.domain.common.BaseDTO;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description : 供求信息
 * @date : 2018/11/14 16:27
 */
@Data
public class TradeInfoDTO extends BaseDTO {

    private static final long serialVersionUID = 2503005074614624213L;


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


    private List<BusinessImgDTO> businessImgDTOList;


    /**
     * 图片业务类型
     * 1资讯图片，2订单支付凭证图片
     */
    private Integer businessType;

    /**
     * 资讯创建人登录名
     */
    private String accountName;

    /**
     * 资讯创建人城市名称
     */
    private String cityName;

    /**
     * 资讯创建人头像
     */
    private String avatarImgPath;


}
