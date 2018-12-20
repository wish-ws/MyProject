package com.um.controller;

import com.um.common.exception.ServiceException;
import com.um.domain.common.BaseRequest;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.common.Response;
import com.um.domain.dto.*;
import com.um.domain.request.LogQueryRequest;
import com.um.domain.request.OrderQueryRequeset;
import com.um.domain.request.UserQueryRequest;
import com.um.service.*;
import com.um.util.DateUtil;
import com.um.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    private NewsService newsService;

    @Autowired
    private BackstageService backstageService;

    @Autowired
    private RecycleOrderService recycleOrderService;

    @Autowired
    private TransactionOrderService transactionOrderService;

    @Autowired
    private UserService userService;


    @PostMapping("/news/add")
    public Response createNews(@RequestBody NewsDTO newsDTO){
        Response response = new Response();

        try {

            if(StringUtils.isEmpty(newsDTO.getTitle()) || StringUtils.isEmpty(newsDTO.getContent())){
                log.error("新增资讯失败，标题和内容不能为空");
                response.setResult(0);
                response.setFailReason("新增资讯失败，标题和内容不能为空");
                return response;
            }

            UserDTO currentUser = super.getCurrentUser();
            newsDTO.setCreator(currentUser.getUserName() + "_" + currentUser.getAccountName());
            newsDTO.setCreatedTime(DateUtil.getCurrentDateTimeStr());
            newsService.createNews(newsDTO);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---createNews error",e);
            response.setResult(0);
            response.setFailReason("新增资讯失败");
        }
        return response;
    }


    @PostMapping("/news/modify")
    public Response modifyNews(@RequestBody NewsDTO newsDTO){
        Response response = new Response();

        try {

            if(StringUtils.isEmpty(newsDTO.getTitle()) || StringUtils.isEmpty(newsDTO.getContent()) || null == newsDTO.getId()){
                log.error("修改资讯失败，id、标题、内容不能为空");
                response.setResult(0);
                response.setFailReason("修改资讯失败，id、标题、内容不能为空");
                return response;
            }

            newsDTO.setModifier(super.getCurrentUser().getUserName());
            newsDTO.setModifiedTime(DateUtil.getCurrentDateTimeStr());
            newsService.modifyNews(newsDTO);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---modifyNews error",e);
            response.setResult(0);
            response.setFailReason("修改资讯失败");
        }
        return response;
    }


    @PostMapping("/news/delete")
    public Response deleteNews(@RequestBody Map<String,Integer> paramMap){
        Response response = new Response();

        try {
            Integer id = paramMap.get("id");
            if(null == id){
                log.error("删除资讯失败，id不能为空");
                response.setResult(0);
                response.setFailReason("删除资讯失败，id不能为空");
                return response;
            }
            UserDTO currentUser = super.getCurrentUser();
            String operator = currentUser.getUserName() + "_" + currentUser.getAccountName();
            newsService.deleteNews(id,operator);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---deleteNews error",e);
            response.setResult(0);
            response.setFailReason("删除资讯失败");
        }
        return response;
    }


    @PostMapping("/news/page")
    public Response queryNewsPage(@RequestBody BaseRequest baseRequest){
        Response response = new Response();

        try {
            PaginationSupportDTO pageInfo = newsService.queryNewsPage(baseRequest);
            response.setModel(pageInfo);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---queryNewsPage error",e);
            response.setResult(0);
            response.setFailReason("分页查询资讯失败");
        }
        return response;
    }

    @GetMapping("/news/detail/{id}")
    public Response queryNewsPage(@PathVariable Integer id){
        Response response = new Response();

        try {
            if(null == id){
                log.error("查询资讯详情失败，id不能为空");
                response.setResult(0);
                response.setFailReason("查询资讯详情失败，id不能为空");
                return response;
            }
            NewsDTO newsDTO = newsService.queryNewsDetail(id);
            response.setModel(newsDTO);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---queryNewsPage error",e);
            response.setResult(0);
            response.setFailReason("查询资讯详情失败");
        }
        return response;
    }




    @GetMapping("/statistics/info")
    public Response queryPlatformStatistics(){
        Response response = new Response();

        try {
            PlatformStatisticsDTO platformStatisticsDTO = backstageService.queryPlatformStatistics();
            response.setModel(platformStatisticsDTO);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---queryPlatformStatistics error",e);
            response.setResult(0);
            response.setFailReason("查询统计信息失败");
        }

        return response;
    }



    @PostMapping("/order/recycle/page")
    public Response queryRecycleOrderPage(@RequestBody OrderQueryRequeset orderQueryRequeset){
        Response response = new Response();

        try {
            PaginationSupportDTO pageInfo = recycleOrderService.queryRecycleOrderPage(orderQueryRequeset);
            response.setModel(pageInfo);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---queryRecycleOrderPage error",e);
            response.setResult(0);
            response.setFailReason("分页查询回收订单失败");
        }

        return response;
    }


    @GetMapping("/order/recycle/detail/{orderCode}")
    public Response queryRecycleOrderDetail(@PathVariable String orderCode){
        Response response = new Response();

        try {

            if(StringUtils.isEmpty(orderCode)){
                log.error("查询回收订单详情失败，订单编码不能为空");
                response.setResult(0);
                response.setFailReason("查询回收订单详情失败，订单编码不能为空");
                return response;
            }

            RecycleOrderDTO recycleOrderDTO = recycleOrderService.queryRecycleOrderDetail(orderCode);
            response.setModel(recycleOrderDTO);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---queryRecycleOrderDetail error",e);
            response.setResult(0);
            response.setFailReason("查询回收订单详情失败");
        }

        return response;
    }


    @PostMapping("/order/transaction/page")
    public Response queryTransactionOrderPage(@RequestBody OrderQueryRequeset orderQueryRequeset){
        Response response = new Response();

        try {

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



    @GetMapping("/order/transaction/detail/{orderCode}")
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
        } catch(Exception e) {
            log.error("---queryTransactionOrderDetail error",e);
            response.setResult(0);
            response.setFailReason("查询交易订单详情失败");
        }
        return response;
    }




    @PostMapping("/feedback/page")
    public Response queryFeedbackPage(@RequestBody BaseRequest baseRequest){
        Response response = new Response();

        try {
            PaginationSupportDTO pageInfo = backstageService.queryFeedbackPage(baseRequest);
            response.setModel(pageInfo);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---queryFeedbackPage error",e);
            response.setResult(0);
            response.setFailReason("分页查询反馈信息失败");
        }
        return response;
    }

    @PostMapping("/feedback/add")
    public Response addFeedback(@RequestBody ClientFeedbackDTO clientFeedbackDTO){

        Response response = new Response();

        try {

            if(StringUtils.isEmpty(clientFeedbackDTO.getFeedbackContent())){
                log.error("提交反馈失败,反馈内容不能为空");
                response.setResult(0);
                response.setFailReason("提交反馈失败,反馈内容不能为空");
                return response;
            }

            UserDTO currentUser = super.getCurrentUser();
            clientFeedbackDTO.setCreatorAccountName(currentUser.getAccountName());
            clientFeedbackDTO.setCreator(currentUser.getUserName());
            backstageService.addFeedback(clientFeedbackDTO);
            response.setResult(1);

        } catch (Exception e) {
            log.error("---addFeedback error",e);
            response.setResult(0);
            response.setFailReason("提交反馈失败");
        }

        return response;
    }



    @PostMapping("/user/page")
    public Response queryUserPage(@RequestBody UserQueryRequest userQueryRequest){
        Response response = new Response();

        try {

            if(!super.hasPlatformAdminPermission()){
                log.error("当前用户没有权限访问");
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            if(null == userQueryRequest.getAccountType()){
                log.error("用户类型不能为空");
                response.setResult(0);
                response.setFailReason("用户类型不能为空");
                return response;
            }

            PaginationSupportDTO pageInfo = userService.queryUserPage(userQueryRequest);
            response.setModel(pageInfo);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---queryUserPage error",e);
            response.setResult(0);
            response.setFailReason("分页查询用户失败");
        }
        return response;
    }


    @PostMapping("/user/add")
    public Response createPlatformUser(@RequestBody UserDTO userDTO){
        Response response = new Response();

        try {

            if(!super.hasPlatformAdminPermission()){
                log.error("当前用户没有权限访问");
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            StringBuffer sb = new StringBuffer();

            if(StringUtils.isEmpty(userDTO.getAccountName())){
                sb.append("登录账号不能为空;");
            }
            if(CollectionUtils.isEmpty(userDTO.getRoleCodeList())){
                sb.append("员工权限不能为空;");
            }

            if(sb.length() > 0){
                log.error("新增平台用户失败，原因：" + sb.toString());
                response.setResult(0);
                response.setFailReason("新增平台用户失败，原因：" + sb.toString());
                return response;
            }
            UserDTO currentUser = super.getCurrentUser();
            userDTO.setCreator(currentUser.getUserName());
            userDTO.setCreatedTime(DateUtil.getCurrentDateTimeStr());
            String operator = currentUser.getUserName() + "_" + currentUser.getAccountName();
            String roleCodes = StringUtil.listToString(userDTO.getRoleCodeList(),",");
            userDTO.setRoleCodes(roleCodes);
            userService.createPlatformUser(userDTO,operator);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---createUser error",e);
            response.setResult(0);
            response.setFailReason("新增平台用户失败");
        }
        return response;
    }



    @PostMapping("/user/modify")
    public Response modifyUser(@RequestBody UserDTO userDTO){
        Response response = new Response();

        try {

            if(!super.hasPlatformAdminPermission()){
                log.error("当前用户没有权限访问");
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            StringBuffer sb = new StringBuffer();

            if(null == userDTO.getUserId()){
                sb.append("用户id不能为空;");
            }
            if(CollectionUtils.isEmpty(userDTO.getRoleCodeList())){
                sb.append("员工权限不能为空;");
            }

            if(sb.length() > 0){
                log.error("修改平台用户失败，原因：" + sb.toString());
                response.setResult(0);
                response.setFailReason("修改平台用户失败，原因：" + sb.toString());
                return response;
            }
            UserDTO currentUser = super.getCurrentUser();
            userDTO.setModifier(currentUser.getUserName());
            userDTO.setModifiedTime(DateUtil.getCurrentDateTimeStr());
            String operator = currentUser.getUserName() + "_" + currentUser.getAccountName();
            String roleCodes = StringUtil.listToString(userDTO.getRoleCodeList(),",");
            userDTO.setRoleCodes(roleCodes);
            userService.modifyPlatformUser(userDTO,operator);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---modifyUser error",e);
            response.setResult(0);
            response.setFailReason("修改平台用户失败");
        }
        return response;
    }



    @PostMapping("/user/modifyEnable")
    public Response modifyUserEnable(@RequestBody UserDTO userDTO){
        Response response = new Response();

        try {

            if(!super.hasPlatformAdminPermission()){
                log.error("当前用户没有权限访问");
                response.setResult(0);
                response.setFailReason("当前用户没有权限访问");
                return response;
            }

            StringBuffer sb = new StringBuffer();

            if(null == userDTO.getUserId()){
                sb.append("用户id不能为空;");
            }
            if(null == userDTO.getStatus()){
                sb.append("启用/禁用状态不能为空;");
            }

            if(sb.length() > 0){
                log.error("启用/禁用用户失败，原因：" + sb.toString());
                response.setResult(0);
                response.setFailReason("启用/禁用用户失败，原因：" + sb.toString());
                return response;
            }
            UserDTO currentUser = super.getCurrentUser();
            userDTO.setModifier(currentUser.getUserName());
            userDTO.setModifiedTime(DateUtil.getCurrentDateTimeStr());
            userService.modifyUserEnable(userDTO);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---modifyUserEnable error",e);
            response.setResult(0);
            response.setFailReason("启用/禁用用户失败");
        }
        return response;
    }




    @PostMapping("/systemlog/page")
    public Response querySystemLogPage(@RequestBody LogQueryRequest logQueryRequest){
        Response response = new Response();

        try {

            PaginationSupportDTO pageInfo = backstageService.querySystemLogPage(logQueryRequest);
            response.setModel(pageInfo);
            response.setResult(1);
        } catch(Exception e) {
            log.error("---querySystemLogPage error",e);
            response.setResult(0);
            response.setFailReason("分页查询系统日志失败");
        }
        return response;
    }







}
