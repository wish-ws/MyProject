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
 * @description : 用户地址信息
 * @date : 2018/11/12 22:46
 */
@Data
@Table(name="t_user_address")
public class AddressPO extends BaseDTO {
    private static final long serialVersionUID = -682203557850084956L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 联系人名称
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String contactTel;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 地址详情
     */
    private String addressDetail;

    /**
     * 用户id
     */
    private Integer userId;





}
