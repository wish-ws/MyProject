package com.um.controller;

import com.alibaba.fastjson.JSON;
import com.um.common.enums.*;
import com.um.common.exception.ServiceException;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.common.Response;
import com.um.domain.dto.BusinessImgDTO;
import com.um.domain.dto.RecycleOrderDTO;
import com.um.domain.dto.TransactionOrderDTO;
import com.um.domain.dto.UserDTO;
import com.um.domain.request.OrderQueryRequeset;
import com.um.service.RecycleOrderService;
import com.um.service.TransactionOrderService;
import com.um.util.BeanUtil;
import com.um.util.DateUtil;
import com.um.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author : ws
 * @project : com.um
 * @description : app交易平台订单控制器
 * @date : 2018/11/14 19:29
 */
@Slf4j
@RestController
@RequestMapping("/platform/order")
public class PlatformOrderController extends BaseController{


    @Autowired
    private RecycleOrderService recycleOrderService;

    @Autowired
    private TransactionOrderService transactionOrderService;



    @PostMapping("/recycle/page")
    public Response queryRecycleOrderPage(@RequestBody OrderQueryRequeset orderQueryRequeset){
        Response response = new Response();

        try {

            if(!super.hasRecycleOrderPermission()){
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
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


    @GetMapping("/recycle/detail/{orderCode}")
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





    @PostMapping("/recycle/confirmReceive")
    public Response confirmReceiveRecycleOrder(@RequestBody Map<String,String> paramMap){
        Response response = new Response();

        try {

            if(!super.hasRecycleOrderPermission()){
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            String orderCode = paramMap.get("orderCode");

            if(StringUtils.isEmpty(orderCode)){
                log.error("确认接单失败，订单号不能为空");
                response.setResult(0);
                response.setFailReason("确认接单失败，订单号不能为空");
                return response;
            }

            RecycleOrderDTO recycleOrderDTO = new RecycleOrderDTO();
            recycleOrderDTO.setOrderCode(orderCode);
            recycleOrderDTO.setModifier(super.getCurrentUser().getUserName());
            recycleOrderDTO.setModifiedTime(DateUtil.getCurrentDateTimeStr());
            String msg = recycleOrderService.modifyRecycleOrder(recycleOrderDTO,OperateROrderType.confirmReceive);
            if(StringUtils.isEmpty(msg)){
                response.setResult(1);
            }else{
                log.error("确认接收订单失败："+msg);
                response.setResult(0);
                response.setFailReason(msg);
            }
        } catch(Exception e) {
            log.error("---queryRecycleOrderDetail error",e);
            response.setResult(0);
            response.setFailReason("确认接收订单失败");
        }
        return response;
    }


    @PostMapping("/recycle/confirmReycle")
    public Response confirmReycleRecycleOrder(@RequestBody Map<String,String> paramMap){
        Response response = new Response();

        try {

            if(!super.hasRecycleOrderPermission()){
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            String orderCode = paramMap.get("orderCode");
            if(StringUtils.isEmpty(orderCode)){
                log.error("确认回收失败，订单号不能为空");
                response.setResult(0);
                response.setFailReason("确认回收失败，订单号不能为空");
                return response;
            }
            RecycleOrderDTO recycleOrderDTO = new RecycleOrderDTO();
            recycleOrderDTO.setOrderCode(orderCode);
            recycleOrderDTO.setModifier(super.getCurrentUser().getUserName());
            recycleOrderDTO.setModifiedTime(DateUtil.getCurrentDateTimeStr());
            String msg = recycleOrderService.modifyRecycleOrder(recycleOrderDTO,OperateROrderType.confirmReycle);
            if(StringUtils.isEmpty(msg)){
                response.setResult(1);
            }else{
                log.error("确认回收失败："+msg);
                response.setResult(0);
                response.setFailReason(msg);
            }
        } catch(Exception e) {
            log.error("---queryRecycleOrderDetail error",e);
            response.setResult(0);
            response.setFailReason("确认回收失败");
        }
        return response;
    }



    @PostMapping("/recycle/confirmSettle")
    public Response confirmSettleRecycleOrder(@RequestBody Map<String,String> paramMap){
        Response response = new Response();

        try {

            if(!super.hasRecycleOrderPermission()){
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            String orderCode = paramMap.get("orderCode");
            String statementCode = paramMap.get("statementCode");

            if(StringUtils.isEmpty(orderCode) || StringUtils.isEmpty(statementCode)){
                log.error("确认结算失败，订单号和结算单号不能为空");
                response.setResult(0);
                response.setFailReason("确认结算失败，订单号和结算单号不能为空");
                return response;
            }

            RecycleOrderDTO recycleOrderDTO = new RecycleOrderDTO();
            recycleOrderDTO.setOrderCode(orderCode);
            recycleOrderDTO.setModifier(super.getCurrentUser().getUserName());
            recycleOrderDTO.setModifiedTime(DateUtil.getCurrentDateTimeStr());
            String msg = recycleOrderService.modifyRecycleOrder(recycleOrderDTO,OperateROrderType.confirmSettle);
            if(StringUtils.isEmpty(msg)){
                response.setResult(1);
            }else{
                log.error("确认结算订单失败："+msg);
                response.setResult(0);
                response.setFailReason(msg);
            }
        } catch(Exception e) {
            log.error("---queryRecycleOrderDetail error",e);
            response.setResult(0);
            response.setFailReason("确认结算订单失败");
        }
        return response;
    }



    @PostMapping("/recycle/cancel")
    public Response cancelRecycleOrder(@RequestBody Map<String,String> paramMap){
        Response response = new Response();

        try {

            if(!super.hasRecycleOrderPermission()){
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            String orderCode = paramMap.get("orderCode");

            if(StringUtils.isEmpty(orderCode)){
                log.error("取消订单失败，订单号不能为空");
                response.setResult(0);
                response.setFailReason("取消订单失败，订单号不能为空");
                return response;
            }
            RecycleOrderDTO recycleOrderDTO = new RecycleOrderDTO();
            recycleOrderDTO.setOrderCode(orderCode);
            recycleOrderDTO.setModifier(super.getCurrentUser().getUserName());
            recycleOrderDTO.setModifiedTime(DateUtil.getCurrentDateTimeStr());
            String msg = recycleOrderService.modifyRecycleOrder(recycleOrderDTO,OperateROrderType.cancel);
            if(StringUtils.isEmpty(msg)){
                response.setResult(1);
            }else{
                log.error("取消回收订单失败："+msg);
                response.setResult(0);
                response.setFailReason(msg);
            }
        } catch(Exception e) {
            log.error("---queryRecycleOrderDetail error",e);
            response.setResult(0);
            response.setFailReason("取消回收订单失败");
        }
        return response;
    }





    @PostMapping("/transaction/create")
    public Response createTransactionOrder(@RequestBody TransactionOrderDTO transactionOrderDTO){
        Response response = new Response();

        try {

            if(!super.hasTransactionOrderPermission()){
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            UserDTO currentUser = super.getCurrentUser();

            StringBuffer sb = new StringBuffer();
            if(null == transactionOrderDTO.getTradeInfoCreatorUserId()){
                sb.append("供求消息用户id不能为空;");
            }
            if(transactionOrderDTO.getTradeInfoCreatorUserId().equals(currentUser.getUserId())){
                sb.append("自己发布的供求不能直接下单;");
            }
            if (null == transactionOrderDTO.getOrderType()) {
                sb.append("订单类型不能为空;");
            }
            if (null == transactionOrderDTO.getItemType()) {
                sb.append("废品类型不能为空;");
            }
            if (null == transactionOrderDTO.getItemPrice()) {
                sb.append("废品价格不能为空;");
            }
            if (null == transactionOrderDTO.getItemWeight()) {
                sb.append("废品重量不能为空");
            }
            if (StringUtils.isEmpty(transactionOrderDTO.getOrderAmount())) {
                sb.append("订单金额不能为空");
            }

            if(sb.length() > 0){
                log.error("创建交易订单，参数异常：" + sb.toString());
                response.setResult(0);
                response.setFailReason("创建交易订单，参数异常：" + sb.toString());
                return response;
            }


            Random random = new Random();
            String orderCode = NumberUtil.createCode(Long.valueOf(random.nextInt(9998)), CodePrefitEnum.TRANSACTION.desc);
            transactionOrderDTO.setOrderCode(orderCode);
            transactionOrderDTO.setCreatorUserId(currentUser.getUserId());
            transactionOrderDTO.setCreator(currentUser.getUserName());
            transactionOrderDTO.setCreatedTime(DateUtil.getCurrentDateTimeStr());
            transactionOrderDTO.setOrderStatus(TransactionOrderStatusEnum.WAIT_FOR_RECEIVE.key);

            if(OrderTypeEnum.SALE.key == transactionOrderDTO.getOrderType()){
                transactionOrderDTO.setBuyerUserId(transactionOrderDTO.getTradeInfoCreatorUserId());
                transactionOrderDTO.setSellerUserId(currentUser.getUserId());
            }else if(OrderTypeEnum.PURCHASE.key == transactionOrderDTO.getOrderType()){
                transactionOrderDTO.setBuyerUserId(currentUser.getUserId());
                transactionOrderDTO.setSellerUserId(transactionOrderDTO.getTradeInfoCreatorUserId());
            }

            transactionOrderService.createTransactionOrder(transactionOrderDTO);
            response.setResult(1);
            response.setModel(orderCode);

        } catch (ServiceException se){
            log.error("---createTransactionOrder error",se);
            response.setResult(0);
            response.setFailReason(se.getMessage());
        } catch (Exception e) {
            log.error("---createTransactionOrder error",e);
            response.setResult(0);
            response.setFailReason("创建交易订单失败");
        }
        return response;
    }



    @PostMapping("/transaction/page")
    public Response queryTransactionOrderPage(@RequestBody OrderQueryRequeset orderQueryRequeset){
        Response response = new Response();

        try {

            if(!super.hasTransactionOrderPermission()){
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            UserDTO currentUser = super.getCurrentUser();
            Integer accountType = currentUser.getAccountType();
            String roleCodes = currentUser.getRoleCodes();
            //交易端用户查询，只查自己的单
            if (accountType.intValue() == AccountTypeEnum.PLATFORM.key) {
                orderQueryRequeset.setCreatorUserId(currentUser.getUserId());
            }
            //平台交易端操作员，只查自己的单
            if(accountType.intValue() == AccountTypeEnum.BACK_STAGE.key && roleCodes.contains(PlatformRoleCodeEnum.TRANSACTION_OPR.code)){
                orderQueryRequeset.setCreatorUserId(currentUser.getUserId());
            }

            PaginationSupportDTO pageInfo = transactionOrderService.queryTransactionOrderPage(orderQueryRequeset);
            response.setResult(1);
            response.setModel(pageInfo);

        } catch(Exception e) {
            log.error("---queryTransactionOrderPage error",e);
            response.setResult(0);
            response.setFailReason("分页查询交易订单失败");
        }
        return response;
    }




    @GetMapping("/transaction/detail/{orderCode}")
    public Response queryTransactionOrderDetail(@PathVariable String orderCode){
        Response response = new Response();

        try {

            TransactionOrderDTO transactionOrderDTO = transactionOrderService.queryTransactionOrderDetail(orderCode);

            response.setResult(1);
            response.setModel(transactionOrderDTO);

        } catch (ServiceException se) {
            log.error("---queryTransactionOrderDetail error",se);
            response.setResult(0);
            response.setFailReason(se.getMessage());
        }  catch(Exception e) {
            log.error("---queryTransactionOrderDetail error",e);
            response.setResult(0);
            response.setFailReason("查询交易订单详情失败");
        }
        return response;
    }





    @PostMapping("/transaction/confirmReceive")
    public Response confirmReceiveTransactionOrder(@RequestBody Map<String,String> paramMap){
        Response response = new Response();

        try {

            if(!super.hasTransactionOrderPermission()){
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            String orderCode = paramMap.get("orderCode");

            if(StringUtils.isEmpty(orderCode)){
                log.error("确认接单失败，订单号不能为空");
                response.setResult(0);
                response.setFailReason("确认接单失败，订单号不能为空");
                return response;
            }
            UserDTO currentUser = super.getCurrentUser();
            TransactionOrderDTO transactionOrderDTO = new TransactionOrderDTO();
            transactionOrderDTO.setOrderCode(orderCode);
            transactionOrderDTO.setModifier(currentUser.getUserName());
            transactionOrderDTO.setModifiedTime(DateUtil.getCurrentDateTimeStr());
            String msg = transactionOrderService.modifyTransactionOrder(transactionOrderDTO,OperateTOrderType.confirmReceive);
            if(StringUtils.isEmpty(msg)){
                response.setResult(1);
            }else{
                log.error("确认接收订单失败："+msg);
                response.setResult(0);
                response.setFailReason(msg);
            }
        } catch(Exception e) {
            log.error("---confirmReceiveTransactionOrder error",e);
            response.setResult(0);
            response.setFailReason("确认接收订单失败");
        }
        return response;
    }


    @PostMapping("/transaction/confirmDelivery")
    public Response confirmDelivery(@RequestBody Map<String,String> paramMap){
        Response response = new Response();

        try {

            if(!super.hasTransactionOrderPermission()){
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            String orderCode = paramMap.get("orderCode");
            if(StringUtils.isEmpty(orderCode)){
                log.error("确认交货失败，订单号不能为空");
                response.setResult(0);
                response.setFailReason("确认交货失败，订单号不能为空");
                return response;
            }
            TransactionOrderDTO transactionOrderDTO = new TransactionOrderDTO();
            transactionOrderDTO.setOrderCode(orderCode);
            transactionOrderDTO.setModifier(super.getCurrentUser().getUserName());
            transactionOrderDTO.setModifiedTime(DateUtil.getCurrentDateTimeStr());
            String msg = transactionOrderService.modifyTransactionOrder(transactionOrderDTO,OperateTOrderType.confirmDelivery);
            if(StringUtils.isEmpty(msg)){
                response.setResult(1);
            }else{
                log.error("确认交货失败："+msg);
                response.setResult(0);
                response.setFailReason(msg);
            }
        } catch(Exception e) {
            log.error("---confirmDelivery error",e);
            response.setResult(0);
            response.setFailReason("确认交货失败");
        }
        return response;
    }


    @PostMapping("/transaction/statementImg/save")
    public Response saveStatementImg(@RequestBody Map<String,Object> paramMap){
        Response response = new Response();

        try {

            if(!super.hasTransactionOrderPermission()){
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            if(null == paramMap.get("orderCode") || null == paramMap.get("businessImgDTOList")){
                response.setResult(0);
                response.setFailReason("订单号，支付凭证不能为空");
                return response;
            }

            String orderCode =  paramMap.get("orderCode").toString();
            List<BusinessImgDTO> businessImgDTOList = BeanUtil.transformList(paramMap.get("businessImgDTOList"),BusinessImgDTO.class);

            TransactionOrderDTO transactionOrderDTO = new TransactionOrderDTO();
            transactionOrderDTO.setOrderCode(orderCode);
            transactionOrderDTO.setBusinessImgDTOList(businessImgDTOList);
            transactionOrderDTO.setModifier(super.getCurrentUser().getUserName());
            transactionOrderDTO.setModifiedTime(DateUtil.getCurrentDateTimeStr());
            String msg = transactionOrderService.modifyTransactionOrder(transactionOrderDTO,OperateTOrderType.uploadStatementImg);
            if(StringUtils.isEmpty(msg)){
                response.setResult(1);
            }else{
                log.error("上传支付凭证失败："+msg);
                response.setResult(0);
                response.setFailReason(msg);
            }
        } catch(Exception e) {
            log.error("---uploadStatementImg error",e);
            response.setResult(0);
            response.setFailReason("上传支付凭证失败");
        }
        return response;
    }





    @PostMapping("/transaction/auditPayAmount")
    public Response auditPayAmount(@RequestBody Map<String,String> paramMap){
        Response response = new Response();

        try {

            if(!super.hasTransactionOrderPermission()){
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            String orderCode = paramMap.get("orderCode");


            if(StringUtils.isEmpty(orderCode) ){
                log.error("审核交付失败，订单号不能为空");
                response.setResult(0);
                response.setFailReason("审核交付失败，订单号不能为空");
                return response;
            }

            TransactionOrderDTO transactionOrderDTO = new TransactionOrderDTO();
            transactionOrderDTO.setOrderCode(orderCode);
            transactionOrderDTO.setModifier(super.getCurrentUser().getUserName());
            transactionOrderDTO.setModifiedTime(DateUtil.getCurrentDateTimeStr());
            String msg = transactionOrderService.modifyTransactionOrder(transactionOrderDTO,OperateTOrderType.auditPayAmount);
            if(StringUtils.isEmpty(msg)){
                response.setResult(1);
            }else{
                log.error("审核支付交易订单失败："+msg);
                response.setResult(0);
                response.setFailReason(msg);
            }
        } catch(Exception e) {
            log.error("---auditPayAmount error",e);
            response.setResult(0);
            response.setFailReason("审核支付交易订单失败");
        }
        return response;
    }







    @PostMapping("/transaction/cancel")
    public Response cancelTransactionOrder(@RequestBody Map<String,String> paramMap){
        Response response = new Response();

        try {

            if(!super.hasTransactionOrderPermission()){
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            String orderCode = paramMap.get("orderCode");

            if(StringUtils.isEmpty(orderCode)){
                log.error("取消订单失败，订单号不能为空");
                response.setResult(0);
                response.setFailReason("取消订单失败，订单号不能为空");
                return response;
            }
            TransactionOrderDTO transactionOrderDTO = new TransactionOrderDTO();
            transactionOrderDTO.setOrderCode(orderCode);
            transactionOrderDTO.setModifier(super.getCurrentUser().getUserName());
            transactionOrderDTO.setModifiedTime(DateUtil.getCurrentDateTimeStr());
            String msg = transactionOrderService.modifyTransactionOrder(transactionOrderDTO,OperateTOrderType.cancel);
            if(StringUtils.isEmpty(msg)){
                response.setResult(1);
            }else{
                log.error("取消交易订单失败："+msg);
                response.setResult(0);
                response.setFailReason(msg);
            }
        } catch(Exception e) {
            log.error("---cancelTransactionOrder error",e);
            response.setResult(0);
            response.setFailReason("取消交易订单失败");
        }
        return response;
    }



    public static class OperateROrderType {
        public static final Integer confirmReceive = 1;
        public static final Integer confirmReycle = 2;
        public static final Integer confirmSettle = 3;
        public static final Integer cancel = 4;
    }

    public static class OperateTOrderType {
        public static final Integer confirmReceive = 1;
        public static final Integer confirmDelivery = 2;
        public static final Integer uploadStatementImg = 3;
        public static final Integer auditPayAmount = 4;
        public static final Integer cancel = 5;
    }

}
