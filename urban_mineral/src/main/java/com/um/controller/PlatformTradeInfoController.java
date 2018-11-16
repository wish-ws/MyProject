package com.um.controller;

import com.um.common.enums.AccountTypeEnum;
import com.um.common.enums.CodePrefitEnum;
import com.um.common.enums.RecycleOrderStatusEnum;
import com.um.common.enums.StatusEnum;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.common.Response;
import com.um.domain.dto.RecycleOrderDTO;
import com.um.domain.dto.TradeInfoDTO;
import com.um.domain.request.OrderQueryRequeset;
import com.um.domain.request.TradeInfoQueryRequest;
import com.um.service.RecycleOrderService;
import com.um.service.TradeInfoService;
import com.um.util.DateUtil;
import com.um.util.NumberUtil;
import com.um.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

/**
 * @author : ws
 * @project : com.um
 * @description : app交易平台供求信息控制器
 * @date : 2018/11/14 19:29
 */
@Slf4j
@RestController
@RequestMapping("/platform/tradeInfo")
public class PlatformTradeInfoController extends BaseController{


    @Autowired
    private TradeInfoService tradeInfoService;

    @PostMapping("/page")
    public Response queryTradeInfoPage(@RequestBody TradeInfoQueryRequest tradeInfoQueryRequest){
        Response response = new Response();

        try {

            PaginationSupportDTO pageInfo = tradeInfoService.queryTradeInfoPage(tradeInfoQueryRequest);
            response.setResult(1);
            response.setModel(pageInfo);
        } catch(Exception e) {
            log.error("---queryTradeInfoPage error",e);
            response.setResult(0);
            response.setFailReason("分页查询供求信息失败");
        }
        return response;
    }



    @PostMapping("/create")
    public Response createTradeInfo(@RequestBody TradeInfoDTO tradeInfoDTO){
        Response response = new Response();

        try {

            StringBuffer sb = new StringBuffer();

            if (null == tradeInfoDTO.getItemType()) sb.append("废品类型不能为空");
            if (null == tradeInfoDTO.getTradeType()) sb.append("供求类型不能为空");
            if (null == tradeInfoDTO.getContent()) sb.append("信息内容不能为空");

            if (sb.length() > 0) {
                log.error("发布供求信息失败，原因：" + sb.toString());
                response.setResult(0);
                response.setFailReason("发布供求信息失败，原因：" + sb.toString());
                return response;
            }

            tradeInfoDTO.setStatus(StatusEnum.VALID.key);
            tradeInfoDTO.setCreator(super.getCurrentUser().getUserName());
            tradeInfoDTO.setCreatorUserId(super.getCurrentUser().getUserId());
            tradeInfoDTO.setCreatedTime(DateUtil.getCurrentDateTimeStr());
            tradeInfoService.createTradeInfo(tradeInfoDTO);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---createTradeInfo error",e);
            response.setResult(0);
            response.setFailReason("发布供求信息失败");
        }
        return response;
    }

}
