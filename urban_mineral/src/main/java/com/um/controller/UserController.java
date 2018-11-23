package com.um.controller;

import com.um.cache.TokenCache;
import com.um.common.enums.StatusEnum;
import com.um.common.exception.ServiceException;
import com.um.domain.common.Response;
import com.um.domain.dto.AddressDTO;
import com.um.domain.dto.ClientFeedbackDTO;
import com.um.domain.dto.UserDTO;
import com.um.service.UserService;
import com.um.util.DateUtil;
import com.um.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author : ws
 * @project : com.um
 * @description : 用户
 * @date : 2018/11/14 19:29
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public Response registerUser(@RequestBody UserDTO userDTO){

        Response response = new Response();

        try {
            if(StringUtils.isEmpty(userDTO.getAccountName())
                    || StringUtils.isEmpty(userDTO.getPassword())
                    || StringUtils.isEmpty(userDTO.getVerifyCode())
                    || null == userDTO.getAccountType()){
                log.error("注册失败，登录名、密码、验证码、用户类型不能为空");
                response.setResult(0);
                response.setFailReason("注册失败，登录名、密码、验证码、用户类型不能为空");
                return response;
            }

            userDTO.setCreator(userDTO.getAccountName());
            userDTO.setCreatedTime(DateUtil.getCurrentDateTimeStr());
            userService.registerUser(userDTO);
            response.setResult(1);
        } catch (ServiceException se) {
            log.error("注册用户失败",se);
            response.setResult(0);
            response.setFailReason(se.getMessage());
        } catch (Exception e) {
            log.error("注册用户失败",e);
            response.setResult(0);
            response.setFailReason("注册用户失败");
        }
        return response;
    }



    @GetMapping("/verifycode/send/{accountName}")
    public Response sendVerifyCode(@PathVariable String accountName){

        Response response = new Response();

        try {

            if(StringUtils.isEmpty(accountName)){
                log.error("发送短信验证码失败，登录名不能为空");
                response.setResult(0);
                response.setFailReason("发送短信验证码失败，登录名不能为空");
                return response;
            }

            boolean flag = userService.sendVerifyCode(accountName);
            if(flag){
                response.setResult(1);
            }else{
                response.setResult(0);
                response.setFailReason("发送失败，请稍后重试!");
            }

        } catch (Exception e) {
            log.error("发送验证码失败",e);
            response.setResult(0);
            response.setFailReason("发送验证码失败");
        }

        return response;
    }




    @PostMapping("/login")
    public Response login(@RequestBody UserDTO userDTO){

        Response response = new Response();

        try {
            if(StringUtils.isEmpty(userDTO.getAccountName()) || null == userDTO.getAccountType()
                    || (StringUtils.isEmpty(userDTO.getPassword()) && StringUtils.isEmpty(userDTO.getVerifyCode()))
                    ){
                log.error("登陆失败，登录名不能为空,密码或验证码其中一个不能为空");
                response.setResult(0);
                response.setFailReason("登陆失败，登录名不能为空,密码或验证码其中一个不能为空");
                return response;
            }

            UserDTO user = userService.login(userDTO);
            response.setResult(1);
            response.setModel(user);
        } catch (ServiceException se) {
            log.error(se.getMessage());
            response.setResult(0);
            response.setFailReason(se.getMessage());
        } catch (Exception e) {
            log.error("登陆失败",e);
            response.setResult(0);
            response.setFailReason("登陆失败");
        }
        return response;
    }


    @PostMapping("/password/reset")
    public Response resetPassword(@RequestBody UserDTO userDTO){

        Response response = new Response();

        try {

            if(StringUtils.isEmpty(userDTO.getAccountName())
                    || StringUtils.isEmpty(userDTO.getPassword())
                    || StringUtils.isEmpty(userDTO.getVerifyCode())
                    || null == userDTO.getAccountType()){
                log.error("重置密码失败，登录名、密码、验证码不能为空");
                response.setResult(0);
                response.setFailReason("重置密码失败，登录名、密码、验证码不能为空");
                return response;
            }

            userService.resetPassword(userDTO);

        } catch (ServiceException se) {
            log.error("重置密码失败",se);
            response.setResult(0);
            response.setFailReason(se.getMessage());
        } catch (Exception e) {
            log.error("重置密码失败",e);
            response.setResult(0);
            response.setFailReason("重置密码失败");
        }


        return response;
    }



    @GetMapping("/info/{userId}")
    public Response queryUserInfoByAccountName(@PathVariable Integer userId){

        Response response = new Response();

        try {

            UserDTO userDTO = userService.queryUserInfoByUserId(userId);

            response.setResult(1);
            response.setModel(userDTO);

        } catch (Exception e) {
            log.error("查询用户信息失败",e);
            response.setResult(0);
            response.setFailReason("查询用户信息失败");
        }

        return response;
    }



    @PostMapping("/modify")
    public Response modifyUser(@RequestBody UserDTO userDTO){

        Response response = new Response();

        try {

            if(null == userDTO.getUserId()){
                log.error("修改用户信息失败，用户id不能为空");
                response.setResult(0);
                response.setFailReason("修改用户信息失败，用户id不能为空");
                return response;
            }

            userService.modifyUser(userDTO);
            response.setResult(1);

        } catch (Exception e) {
            log.error("修改用户信息失败",e);
            response.setResult(0);
            response.setFailReason("修改用户信息失败");
        }

        return response;
    }




    @GetMapping("/address/info/{userId}")
    public Response queryUserAddressByUserId(@PathVariable Integer userId){

        Response response = new Response();

        try {

            AddressDTO addressDTO = userService.queryUserAddressByUserId(userId);
            response.setResult(1);
            response.setModel(addressDTO);

        } catch (Exception e) {
            log.error("查询用户地址失败",e);
            response.setResult(0);
            response.setFailReason("查询用户地址失败");
        }

        return response;
    }


    @PostMapping("/address/saveOrUpdate")
    public Response saveOrUpdateUserAddress(@RequestBody AddressDTO addressDTO){

        Response response = new Response();

        try {
            StringBuffer sb = new StringBuffer();

            if(StringUtils.isEmpty(addressDTO.getProvinceName())) sb.append("省份不能为空;");
            if(StringUtils.isEmpty(addressDTO.getCityCode()) || StringUtils.isEmpty(addressDTO.getCityName())) sb.append("城市不能为空;");
            if(StringUtils.isEmpty(addressDTO.getRegionName())) sb.append("区不能为空;");
            if(StringUtils.isEmpty(addressDTO.getContactName()) || StringUtils.isEmpty(addressDTO.getContactTel())) sb.append("联系人、联系电话不能为空;");
            if(StringUtils.isEmpty(addressDTO.getAddressDetail())) sb.append("详细地址不能为空;");

            if(sb.length() > 0){
                log.error("修改地址失败，原因：" + sb.toString());
                response.setResult(0);
                response.setFailReason("修改地址失败，原因：" + sb.toString());
                return response;
            }

            UserDTO currentUser = super.getCurrentUser();
            addressDTO.setUserId(currentUser.getUserId());
            if(null == addressDTO.getId()){
                addressDTO.setCreator(currentUser.getUserName());
                addressDTO.setCreatedTime(DateUtil.getCurrentDateTimeStr());
            }else{
                addressDTO.setModifier(currentUser.getUserName());
                addressDTO.setModifiedTime(DateUtil.getCurrentDateTimeStr());
            }
            userService.saveOrUpdateUserAddress(addressDTO);
            response.setResult(1);

        } catch (Exception e) {
            log.error("修改地址失败",e);
            response.setResult(0);
            response.setFailReason("修改地址失败");
        }

        return response;
    }






}
