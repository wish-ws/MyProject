package com.um.domain.dto;

import com.um.domain.common.BaseDTO;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : ws
 * @project : com.um
 * @description : 图片对象
 * @date : 2018/11/14 16:33
 */
@Data
public class BusinessImgDTO extends BaseDTO {


    private static final long serialVersionUID = -5288284637103394925L;


    private Integer id;

    /**
     * 业务编码如tradeInfoId,orderCode
     */
    private String businessCode;

    /**
     * 业务类型1资讯图片，2订单支付凭证图片……
     */
    private Integer businessType;

    /**
     * 图片路径
     */
    private String imgPath;

}
