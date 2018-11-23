package com.um.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : ws
 * @project : com.um
 * @description : 平台配置
 * @date : 2018/11/20 22:46
 */
@Data
public class PlatformConfigDTO implements Serializable {

    private static final long serialVersionUID = 2680124178601769262L;

    private String configKey;

    private String configValue;
}
