package com.um.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.um.common.enums.ImgBusinessTypeEnum;
import com.um.common.enums.VerificationStatusEnum;
import com.um.common.exception.ServiceException;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.BusinessImgDTO;
import com.um.domain.dto.TradeInfoDTO;
import com.um.domain.po.BusinessImgPO;
import com.um.domain.po.TradeInfoPO;
import com.um.domain.po.UserPO;
import com.um.domain.request.TradeInfoQueryRequest;
import com.um.mapper.BusinessImgMapper;
import com.um.mapper.TradeInfoMapper;
import com.um.mapper.UserMapper;
import com.um.service.TradeInfoService;
import com.um.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 16:49
 */
@Slf4j
@Service
public class TradeInfoServiceImpl implements TradeInfoService {


    @Autowired
    private TradeInfoMapper tradeInfoMapper;

    @Autowired
    private BusinessImgMapper businessImgMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public PaginationSupportDTO queryTradeInfoPage(TradeInfoQueryRequest tradeInfoQueryRequest) {

        PaginationSupportDTO<TradeInfoDTO> paginationSupportDTO = new PaginationSupportDTO();
        PageHelper.startPage(tradeInfoQueryRequest.getCurrentPage(), tradeInfoQueryRequest.getPageSize());

        List<TradeInfoDTO> tradeInfoDTOList = tradeInfoMapper.queryTradeInfoPage(tradeInfoQueryRequest);

        PageInfo<TradeInfoDTO> pageInfo = new PageInfo<>(tradeInfoDTOList);

        //查询图片
        if(CollectionUtils.isNotEmpty(pageInfo.getList())){
            BusinessImgPO imgSelect = null;
            for (TradeInfoDTO tradeInfoDTO : pageInfo.getList()) {
                imgSelect = new BusinessImgPO();
                imgSelect.setBusinessCode(tradeInfoDTO.getId().toString());
                imgSelect.setBusinessType(ImgBusinessTypeEnum.NEWS.key);
                List<BusinessImgPO> imgPOList = businessImgMapper.select(imgSelect);
                List<BusinessImgDTO> imgDTOList = BeanUtil.transformList(imgPOList,BusinessImgDTO.class);
                tradeInfoDTO.setBusinessImgDTOList(imgDTOList);
            }
        }

        paginationSupportDTO.copyProperties(pageInfo);
        return paginationSupportDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createTradeInfo(TradeInfoDTO tradeInfoDTO) {

        //判断行业认证
        UserPO userPO = userMapper.selectByPrimaryKey(tradeInfoDTO.getCreatorUserId());
        if(VerificationStatusEnum.NO.key == userPO.getVerificationStatus()){
            log.error(tradeInfoDTO.getCreator() + "未认证，不可发布供求消息");
            throw new ServiceException("联系我们，马上认证即可发布供求");
        }

        TradeInfoPO tradeInfoPO = BeanUtil.transformBean(tradeInfoDTO,TradeInfoPO.class);
        tradeInfoMapper.insert(tradeInfoPO);

        //保存图片
        if(CollectionUtils.isNotEmpty(tradeInfoDTO.getBusinessImgDTOList())){

            for (BusinessImgDTO businessImgDTO : tradeInfoDTO.getBusinessImgDTOList()) {
                businessImgDTO.setBusinessCode(tradeInfoPO.getId().toString());
                businessImgDTO.setBusinessType(ImgBusinessTypeEnum.NEWS.key);
                businessImgDTO.setCreator(tradeInfoDTO.getCreator());
                businessImgDTO.setCreatedTime(tradeInfoDTO.getCreatedTime());
            }
            List<BusinessImgPO> imgPOList = BeanUtil.transformList(tradeInfoDTO.getBusinessImgDTOList(),BusinessImgPO.class);
            businessImgMapper.insertList(imgPOList);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTradeInfo(Integer id) {
        //删除资讯本身
        tradeInfoMapper.deleteByPrimaryKey(id);

        //删除资讯图片
        BusinessImgPO businessImgPO = new BusinessImgPO();
        businessImgPO.setBusinessCode(id.toString());
        businessImgPO.setBusinessType(ImgBusinessTypeEnum.NEWS.key);
        businessImgMapper.delete(businessImgPO);
    }
}
