package com.um.domain.po;

import com.um.domain.common.BaseDTO;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : ws
 * @project : com.um
 * @description : 平台配置
 * @date : 2018/11/20 22:46
 */
@Data
@Table(name="t_platform_config")
public class PlatformConfigPO extends BaseDTO {


    private static final long serialVersionUID = -3099406983846443466L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String configKey;


    private String configValue;





}
