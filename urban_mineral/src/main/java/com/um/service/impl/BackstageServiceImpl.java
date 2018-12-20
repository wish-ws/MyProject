package com.um.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.um.domain.common.BaseRequest;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.ClientFeedbackDTO;
import com.um.domain.dto.PlatformStatisticsDTO;
import com.um.domain.dto.SystemLogDTO;
import com.um.domain.po.ClientFeedbackPO;
import com.um.domain.po.SystemLogPO;
import com.um.domain.request.LogQueryRequest;
import com.um.mapper.FeedbackMapper;
import com.um.mapper.SystemLogMapper;
import com.um.mapper.UserMapper;
import com.um.service.BackstageService;
import com.um.util.BeanUtil;
import com.um.util.DateUtil;
import com.um.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 16:51
 */
@Slf4j
@Service
public class BackstageServiceImpl implements BackstageService {


    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SystemLogMapper systemLogMapper;


    @Override
    public PlatformStatisticsDTO queryPlatformStatistics() {
        return userMapper.queryPlatformStatistics();
    }

    @Override
    public PaginationSupportDTO queryFeedbackPage(BaseRequest baseRequest) {
        PaginationSupportDTO<ClientFeedbackDTO> paginationSupportDTO = new PaginationSupportDTO();
        PageHelper.startPage(baseRequest.getCurrentPage(), baseRequest.getPageSize());
        List<ClientFeedbackDTO> clientFeedbackDTOList = feedbackMapper.queryFeedbackPage(baseRequest);

        PageInfo<ClientFeedbackDTO> pageInfo = new PageInfo<>(clientFeedbackDTOList);
        paginationSupportDTO.copyProperties(pageInfo);
        return paginationSupportDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addFeedback(ClientFeedbackDTO clientFeedbackDTO) {
        ClientFeedbackPO feedbackPO = BeanUtil.transformBean(clientFeedbackDTO,ClientFeedbackPO.class);
        feedbackMapper.insertSelective(feedbackPO);
    }

    @Override
    public PaginationSupportDTO querySystemLogPage(LogQueryRequest logQueryRequest) {
        PaginationSupportDTO<SystemLogDTO> paginationSupportDTO = new PaginationSupportDTO();
        PageHelper.startPage(logQueryRequest.getCurrentPage(), logQueryRequest.getPageSize());

        Example example = new Example(SystemLogPO.class);
        example.selectProperties("createdTime","creator","logContent");
        example.setOrderByClause("created_time desc");
        Example.Criteria criteria = example.createCriteria();

        if(StringUtils.isNotEmpty(logQueryRequest.getStartDate())){
            criteria.andGreaterThanOrEqualTo("createdTime",logQueryRequest.getStartDate());
        }
        if(StringUtils.isNotEmpty(logQueryRequest.getEndDate())){
            criteria.andLessThanOrEqualTo("createdTime",DateUtil.getDate(DateUtil.stringToDate(logQueryRequest.getEndDate()),1,0));
        }
        if(StringUtils.isNotEmpty(logQueryRequest.getAccountName())){
            criteria.andEqualTo("creatorAccountName",logQueryRequest.getAccountName());
        }

        List<SystemLogPO> systemLogPOList = systemLogMapper.selectByExample(example);

        PageInfo<SystemLogPO> pageInfo = new PageInfo<>(systemLogPOList);
        paginationSupportDTO.copyProperties(pageInfo,SystemLogDTO.class);
        return paginationSupportDTO;
    }



}
