package com.um.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.um.cache.UserCache;
import com.um.cache.VerifyCodeCache;
import com.um.common.config.SmsConfig;
import com.um.common.enums.StatusEnum;
import com.um.common.exception.ServiceException;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.AddressDTO;
import com.um.domain.dto.UserDTO;
import com.um.domain.po.AddressPO;
import com.um.domain.po.SystemLogPO;
import com.um.domain.po.UserPO;
import com.um.domain.request.AliyunSmsRequest;
import com.um.domain.request.UserQueryRequest;
import com.um.mapper.AddressMapper;
import com.um.mapper.SystemLogMapper;
import com.um.mapper.UserMapper;
import com.um.service.AliyunSmsService;
import com.um.service.BackstageService;
import com.um.service.UserService;
import com.um.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SystemLogMapper systemLogMapper;

    @Autowired
    private AliyunSmsService aliyunSmsService;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private SmsConfig smsConfig;



    @Override
    public PaginationSupportDTO queryUserPage(UserQueryRequest userQueryRequest) {

        PaginationSupportDTO<UserDTO> paginationSupportDTO = new PaginationSupportDTO();
        PageHelper.startPage(userQueryRequest.getCurrentPage(), userQueryRequest.getPageSize());

        Example example = new Example(UserPO.class);
        example.selectProperties("userName","accountName","roleCodes","status","lastLoginTime","verificationContent","verificationStatus");
        example.setOrderByClause("created_time desc");
        Example.Criteria criteria = example.createCriteria();
        if(null != userQueryRequest.getAccountType()){
            criteria.andEqualTo("accountType",userQueryRequest.getAccountType());
        }
        if(StringUtils.isNotEmpty(userQueryRequest.getAccountName())){
            criteria.andEqualTo("accountName",userQueryRequest.getAccountName());
        }

        List<UserPO> userPOList = userMapper.selectByExample(example);

        PageInfo<UserPO> pageInfo = new PageInfo<>(userPOList);
        paginationSupportDTO.copyProperties(pageInfo,UserDTO.class);
        return paginationSupportDTO;
    }


    @Transactional
    @Override
    public void createPlatformUser(UserDTO userDTO,String operator) {
        UserPO insertUser = new UserPO();
        insertUser.setAccountName(userDTO.getAccountName());
        if(null == userDTO.getAccountType()){
            userDTO.setAccountType(1);
        }
        insertUser.setAccountType(userDTO.getAccountType());
        insertUser.setIsInit(1);
        insertUser.setVerificationStatus(0);
        insertUser.setRoleCodes(userDTO.getRoleCodes());
        insertUser.setStatus(StatusEnum.VALID.key);
        insertUser.setUserName(userDTO.getUserName());
        insertUser.setCreator(userDTO.getCreator());
        insertUser.setCreatedTime(userDTO.getCreatedTime());

        String pwd = NumberUtil.DEFAULT_PWD;
        String salt = NumberUtil.generateNumber(4);

        String encryptPwd = Md5Util.md5Encode(pwd+salt);

        insertUser.setPassword(encryptPwd);
        insertUser.setSalt(salt);


        //保存用户
        userMapper.insert(insertUser);


        //写日志
        SystemLogPO systemLogPO = new SystemLogPO();
        systemLogPO.setLogContent("新增了平台账号 \""+userDTO.getUserName()+ "_" + userDTO.getAccountName()+"\"");
        systemLogPO.setCreatedTime(userDTO.getCreatedTime());
        systemLogPO.setCreator(operator);
        systemLogPO.setCreatorAccountName(operator.split("_")[1]);
        systemLogMapper.insertSelective(systemLogPO);
    }


    @Transactional
    @Override
    public void modifyPlatformUser(UserDTO userDTO, String operator) {

        UserPO userPO = new UserPO();
        userPO.setUserId(userDTO.getUserId());
        userPO.setUserName(userDTO.getUserName());
        userPO.setRoleCodes(userDTO.getRoleCodes());
        userMapper.updateByPrimaryKeySelective(userPO);

        //移除缓存，下次重新加载
        this.removeUserCache(userDTO.getUserId());

        //写日志
        SystemLogPO systemLogPO = new SystemLogPO();
        systemLogPO.setLogContent("修改了平台账号 \""+userDTO.getUserName()+ "_" + userDTO.getAccountName()+"\"");
        systemLogPO.setCreatedTime(userDTO.getModifiedTime());
        systemLogPO.setCreator(operator);
        systemLogPO.setCreatorAccountName(operator.split("_")[1]);
        systemLogMapper.insertSelective(systemLogPO);
    }


    @Transactional
    @Override
    public void modifyUserEnable(UserDTO userDTO) {
        UserPO userPO = new UserPO();
        userPO.setUserId(userDTO.getUserId());
        userPO.setStatus(userDTO.getStatus());
        userMapper.updateByPrimaryKeySelective(userPO);
        //移除缓存，下次重新加载
        this.removeUserCache(userDTO.getUserId());
    }


    @Override
    public boolean sendVerifyCode(String accountName) {
        String code = NumberUtil.generateNumber(6);
        AliyunSmsRequest aliyunSmsRequest = new AliyunSmsRequest();
        aliyunSmsRequest.setSignName(smsConfig.getSignName());
        aliyunSmsRequest.setTemplateCode(smsConfig.getTemplateCode());
        aliyunSmsRequest.setPhoneNumbers(accountName);
        aliyunSmsRequest.setTemplateParam(String.format(smsConfig.getTemplateParam(),code));
        boolean sendSuccess = aliyunSmsService.sendSms(aliyunSmsRequest);
        if(sendSuccess){
            VerifyCodeCache.put(accountName,code);
        }
        return sendSuccess;
    }


    @Transactional
    @Override
    public void registerUser(UserDTO userDTO) {

        //1、验证手机号是否注册过
        UserPO userSelect = new UserPO();
        userSelect.setAccountName(userDTO.getAccountName());
        userSelect = userMapper.selectOne(userSelect);
        if(null != userSelect){
            log.error("注册失败，此手机号已经注册过");
            throw new ServiceException("注册失败，此手机号已经注册过");
        }

        //2、注册
        String salt = NumberUtil.generateNumber(4);
        String encryptPwd = Md5Util.md5Encode(userDTO.getPassword() + salt);
        UserPO userInsert = BeanUtil.transformBean(userDTO,UserPO.class);
        userInsert.setUserName(userDTO.getAccountName());
        userInsert.setSalt(salt);
        userInsert.setPassword(encryptPwd);
        userInsert.setStatus(StatusEnum.VALID.key);
        userInsert.setVerificationStatus(0);
        userInsert.setIsInit(1);

        userMapper.insert(userInsert);
    }


    @Transactional
    @Override
    public UserDTO login(UserDTO userDTO) {

        UserDTO userDB = new UserDTO();

        UserPO userPO = new UserPO();
        userPO.setAccountName(userDTO.getAccountName());
        userPO = userMapper.selectOne(userPO);

        if(StringUtils.isNotEmpty(userDTO.getVerifyCode())){
            String cacheVerifyCode = VerifyCodeCache.get(userDTO.getAccountName());
            if(StringUtils.isEmpty(cacheVerifyCode)){
                log.error("验证码已失效，请稍后重发");
                throw new ServiceException("验证码已失效，请稍后重发");
            }
            if(!cacheVerifyCode.equals(userDTO.getVerifyCode())){
                log.error("验证码错误");
                throw new ServiceException("验证码错误");
            }

            //判断是否注册过
            if(null == userPO){
                //没有，自动注册
                UserPO userInsert = new UserPO();
                userInsert.setAccountType(userDTO.getAccountType());
                userInsert.setAccountName(userDTO.getAccountName());
                userInsert.setUserName(userDTO.getAccountName());
                userInsert.setStatus(StatusEnum.VALID.key);
                userInsert.setCreatedTime(userDTO.getCreatedTime());
                userInsert.setCreator(userDTO.getCreator());
                userInsert.setIsInit(0);
                userInsert.setVerificationStatus(0);
                userMapper.insert(userInsert);
                userPO.setUserId(userInsert.getUserId());
            }else{
                //判断用户有效性
                if(StatusEnum.INVALID.key == userPO.getStatus()){
                    log.error("登录失败，此用户未启用");
                    throw new ServiceException("登录失败，此用户未启用");
                }
            }
        }else if(StringUtils.isNotEmpty(userDTO.getPassword())){

            //判断是否注册过
            if(null == userPO){
                log.error("登录失败，此用户不存在");
                throw new ServiceException("登录失败，此用户不存在");
            }

            //判断用户有效性
            if(StatusEnum.INVALID.key == userPO.getStatus()){
                log.error("登录失败，此用户未启用");
                throw new ServiceException("登录失败，此用户未启用");
            }

            String encryptPwdParam = Md5Util.md5Encode(userDTO.getPassword() + userPO.getSalt());

            if(!encryptPwdParam.equals(userPO.getPassword())){
                log.error("登录失败，密码错误");
                throw new ServiceException("登录失败，密码错误");
            }
        }

        //保存最后登录时间
        UserPO userUpdate = new UserPO();
        userUpdate.setUserId(userPO.getUserId());
        userUpdate.setLastLoginTime(DateUtil.getCurrentDateTimeStr());
        userMapper.updateByPrimaryKeySelective(userUpdate);

        //查询用户地址
        AddressPO addressPO = new AddressPO();
        addressPO.setUserId(userPO.getUserId());
        addressPO = addressMapper.selectOne(addressPO);

        userDB = BeanUtil.transformBean(userPO,UserDTO.class);
        userDB.setAddressDTO(BeanUtil.transformBean(addressPO,AddressDTO.class));
        userDB.setPassword(null);
        userDB.setSalt(null);
        return userDB;
    }

    @Override
    public UserDTO queryUserInfoByUserId(Integer userId) {

        Example example = new Example(UserPO.class);
        example.selectProperties("userId","userName","accountName","accountType","roleCodes","status","avatarImgPath","verificationStatus","verificationContent","isInit");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("status",StatusEnum.VALID.key);

        UserPO userPO = userMapper.selectOneByExample(example);

        if(null == userPO){
            log.error("未查询到用户信息");
            return null;
        }

        AddressPO addressPO = new AddressPO();
        addressPO.setUserId(userId);
        addressPO = addressMapper.selectOne(addressPO);
        AddressDTO addressDTO = BeanUtil.transformBean(addressPO,AddressDTO.class);

        UserDTO userDTO = BeanUtil.transformBean(userPO,UserDTO.class);
        userDTO.setAddressDTO(addressDTO);
        return userDTO;
    }


    @Transactional
    @Override
    public void modifyUser(UserDTO userDTO) {

        //修改密码，用户名，头像，行业认证……
        UserPO userPO = new UserPO();
        userPO.setUserId(userDTO.getUserId());
        userPO.setVerificationStatus(userDTO.getVerificationStatus());
        userPO.setVerificationContent(userDTO.getVerificationContent());
        userPO.setAvatarImgPath(userDTO.getAvatarImgPath());
        userPO.setUserName(userDTO.getUserName());

        //修改密码
        if(StringUtils.isNotEmpty(userDTO.getPassword())){
            String salt = NumberUtil.generateNumber(4);
            userPO.setPassword(Md5Util.md5Encode(userDTO.getPassword() + salt));
            userPO.setSalt(salt);
            userPO.setIsInit(1);
        }

        userMapper.updateByPrimaryKeySelective(userPO);

        //移除缓存，下次重新加载
        this.removeUserCache(userDTO.getUserId());
    }


    @Transactional
    @Override
    public void resetPassword(UserDTO userDTO) {

        String cacheVerifyCode = VerifyCodeCache.get(userDTO.getAccountName());
        if(StringUtils.isEmpty(cacheVerifyCode)){
            log.error("验证码已失效，请稍后重发");
            throw new ServiceException("验证码已失效，请稍后重发");
        }
        if(!cacheVerifyCode.equals(userDTO.getVerifyCode())){
            log.error("验证码错误");
            throw new ServiceException("验证码错误");
        }

        UserPO userPO = new UserPO();
        userPO.setAccountName(userDTO.getAccountName());

        String salt = NumberUtil.generateNumber(4);
        String encryptPwd = Md5Util.md5Encode(userDTO.getPassword() + salt);

        userPO.setPassword(encryptPwd);
        userPO.setSalt(salt);

        Example example = new Example(UserPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountName",userDTO.getAccountName());
        userMapper.updateByExampleSelective(userPO,example);

    }

    @Override
    public AddressDTO queryUserAddressByUserId(Integer userId) {

        AddressPO addressPO = new AddressPO();
        addressPO.setUserId(userId);
        addressPO = addressMapper.selectOne(addressPO);
        return BeanUtil.transformBean(addressPO,AddressDTO.class);
    }


    @Transactional
    @Override
    public void saveOrUpdateUserAddress(AddressDTO addressDTO) {

        AddressPO addressPO = BeanUtil.transformBean(addressDTO,AddressPO.class);
        if(null == addressDTO.getId()){
            addressMapper.insert(addressPO);
        }else{
            addressMapper.updateByPrimaryKeySelective(addressPO);
        }

        //移除缓存，下次重新加载
        this.removeUserCache(addressDTO.getUserId());
    }


    public void removeUserCache(Integer userId){
        //移除缓存，下次重新加载
        UserCache.removeUserCache(userId);
    }

}
