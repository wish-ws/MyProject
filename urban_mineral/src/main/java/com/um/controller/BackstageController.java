package com.um.controller;

import com.um.common.enums.AccountTypeEnum;
import com.um.common.enums.CodePrefitEnum;
import com.um.common.enums.RecycleOrderStatusEnum;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.common.Response;
import com.um.domain.dto.RecycleOrderDTO;
import com.um.domain.request.OrderQueryRequeset;
import com.um.service.RecycleOrderService;
import com.um.util.DateUtil;
import com.um.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

/**
 * @author : ws
 * @project : com.um
 * @description : 后台管理控制器
 * @date : 2018/11/14 19:29
 */
@Slf4j
@RestController
@RequestMapping("/backstage")
public class BackstageController extends BaseController{


    @Autowired
    private RecycleOrderService recycleOrderService;

    @PostMapping("/page")
    public Response queryRecycleOrderPage(@RequestBody OrderQueryRequeset orderQueryRequeset){
        Response response = new Response();

        try {
            Integer accountType = super.getCurrentUser().getAccountType();
            //用户端查询，只查自己的单
            if (accountType.intValue() == AccountTypeEnum.RECYCLE.key) {
                orderQueryRequeset.setCreatorUserId(super.getCurrentUser().getUserId());
            }

            PaginationSupportDTO pageInfo = recycleOrderService.queryRecycleOrderPage(orderQueryRequeset);

            response.setResult(1);
            response.setModel(pageInfo);

        } catch(Exception e) {
            log.error("---queryRecycleOrderPage error",e);
            response.setResult(0);
            response.setFailReason("分页查询回收订单失败");
        }
        return response;
    }




























    @PostMapping("/create")
    public Response createRecycleOrder(@RequestBody RecycleOrderDTO recycleOrderDTO){
        Response response = new Response();

        try {
            StringBuffer sb = new StringBuffer();
            if (null == recycleOrderDTO.getItemType()) {
                sb.append("废品类型不能为空");
            }

            if (null == recycleOrderDTO.getItemQty()) {
                sb.append("废品数量不能为空");
            }

            if (StringUtils.isEmpty(recycleOrderDTO.getAddress())) {
                sb.append("地址不能为空");
            }

            if(sb.length() > 0){
                log.error("创建回收订单，参数异常：" + sb.toString());
                response.setResult(0);
                response.setFailReason("创建回收订单，参数异常：" + sb.toString());
                return response;
            }

            Random random = new Random();
            String orderCode = NumberUtil.createCode(Long.valueOf(random.nextInt(9998)), CodePrefitEnum.RECYCLE.desc);
            recycleOrderDTO.setOrderCode(orderCode);
            recycleOrderDTO.setCreatorAccountName(super.getCurrentUser().getAccountName());
            recycleOrderDTO.setCreator(super.getCurrentUser().getUserName());
            recycleOrderDTO.setCreatedTime(DateUtil.getCurrentDateTimeStr());
            recycleOrderDTO.setCreatorUserId(super.getCurrentUser().getUserId());
            recycleOrderDTO.setOrderStatus(RecycleOrderStatusEnum.WAIT_FOR_RECEIVE.key);
            recycleOrderService.createRecycleOrder(recycleOrderDTO);

            response.setResult(1);
            response.setModel(orderCode);

        } catch(Exception e) {
            log.error("---createRecycleOrder error",e);
            response.setResult(0);
            response.setFailReason("回收订单提交失败");
        }
        return response;
    }






    @GetMapping("/detail/{orderCode}")
    public Response queryRecycleOrderDetail(@PathVariable String orderCode){
        Response response = new Response();

        try {
            RecycleOrderDTO recycleOrderDTO = recycleOrderService.queryRecycleOrderDetail(orderCode);

            response.setResult(1);
            response.setModel(recycleOrderDTO);

        } catch(Exception e) {
            log.error("---queryRecycleOrderDetail error",e);
            response.setResult(0);
            response.setFailReason("查询回收订单详情失败");
        }
        return response;
    }











}
