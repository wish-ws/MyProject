package com.um.controller;

import com.um.domain.common.Response;
import com.um.domain.dto.PlatformConfigDTO;
import com.um.service.PlatformConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : ws
 * @project : com.um
 * @description : 平台配置
 * @date : 2018/11/14 19:29
 */
@Slf4j
@RestController
@RequestMapping("/platform/")
public class PlatformConfigController extends BaseController{


    @Autowired
    private PlatformConfigService platformConfigService;

    @GetMapping("/config")
    public Response queryPlatformConfig(){
        Response response = new Response();
        try {
            List<PlatformConfigDTO> platformConfigDTOList = platformConfigService.queryAllPlatformConfigList();
            Map<String,Object> resMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(platformConfigDTOList)){
                for (PlatformConfigDTO platformConfigDTO : platformConfigDTOList) {
                    resMap.put(platformConfigDTO.getConfigKey(),platformConfigDTO.getConfigValue());
                }
            }
            response.setResult(1);
            response.setModel(resMap);
        } catch(Exception e) {
            log.error("---queryPlatformConfig error",e);
            response.setResult(0);
            response.setFailReason("查询平台配置失败");
        }
        return response;
    }

}
