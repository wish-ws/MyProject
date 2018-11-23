package com.um.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.um.domain.common.BaseRequest;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.NewsDTO;
import com.um.domain.dto.PlatformConfigDTO;
import com.um.domain.po.NewsPO;
import com.um.domain.po.PlatformConfigPO;
import com.um.mapper.NewsMapper;
import com.um.mapper.PlatformConfigMapper;
import com.um.service.NewsService;
import com.um.service.PlatformConfigService;
import com.um.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 16:50
 */
@Slf4j
@Service
public class PlatformConfigServiceImpl implements PlatformConfigService {

    @Autowired
    private PlatformConfigMapper platformConfigMapper;

    @Override
    public List<PlatformConfigDTO> queryAllPlatformConfigList() {

        Example example = new Example(PlatformConfigPO.class);
        example.selectProperties("configKey","configValue");
        List<PlatformConfigPO> configPOList = platformConfigMapper.selectByExample(example);
        return BeanUtil.transformList(configPOList,PlatformConfigDTO.class);
    }
}
