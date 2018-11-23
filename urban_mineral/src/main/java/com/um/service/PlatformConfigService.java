package com.um.service;

import com.um.domain.dto.PlatformConfigDTO;

import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 16:51
 */
public interface PlatformConfigService {

    public List<PlatformConfigDTO> queryAllPlatformConfigList();
}
