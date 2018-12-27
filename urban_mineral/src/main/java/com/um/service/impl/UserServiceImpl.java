package com.um.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.um.cache.TokenCache;
import com.um.cache.UserCache;
import com.um.cache.VerifyCodeCache;
import com.um.common.config.SmsConfig;
import com.um.common.enums.AccountTypeEnum;
import com.um.common.enums.PlatformRoleCodeEnum;
import com.um.common.enums.StatusEnum;
import com.um.common.exception.ServiceException;
import com.um.domain.common.Jwt;
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
        example.selectProperties("userId","userName","accountName","roleCodes","status","lastLoginTime","verificationContent","verificationStatus");
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


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createPlatformUser(UserDTO userDTO,String operator) {
        UserPO insertUser = new UserPO();
        insertUser.setAccountName(userDTO.getAccountName());
        if(null == userDTO.getAccountType()){
            userDTO.setAccountType(1);
        }
        insertUser.setAccountType(userDTO.getAccountType());
        insertUser.setIsInit(1);
        //平台账户，默认已认证
        insertUser.setVerificationStatus(1);
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


    @Transactional(rollbackFor = Exception.class)
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


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void modifyUserEnable(UserDTO userDTO) {
        UserPO userPO = new UserPO();
        userPO.setUserId(userDTO.getUserId());
        userPO.setStatus(userDTO.getStatus());
        userMapper.updateByPrimaryKeySelective(userPO);

        if(StatusEnum.INVALID.key == userDTO.getStatus()){
            //禁用用户，立即清除token，避免访问系统
            TokenCache.remove(userDTO.getUserId());
        }

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
//        boolean sendSuccess = aliyunSmsService.sendSms(aliyunSmsRequest);
//        if(sendSuccess){
//            VerifyCodeCache.put(accountName,code);
//        }

        //测试代码
        boolean sendSuccess = true;
        if(sendSuccess){
            VerifyCodeCache.put(accountName,"123456");
        }
        //测试代码


        return sendSuccess;
    }


    @Transactional(rollbackFor = Exception.class)
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

        String cacheVerifyCode = VerifyCodeCache.get(userDTO.getAccountName());

        if(StringUtils.isEmpty(cacheVerifyCode)){
            log.error("验证码已失效，请稍后重发");
            throw new ServiceException("验证码已失效，请稍后重发");
        }
        if(!cacheVerifyCode.equals(userDTO.getVerifyCode())){
            log.error("验证码错误");
            throw new ServiceException("验证码错误");
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


    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserDTO login(UserDTO userDTO) {

        UserPO userPO = new UserPO();
        userPO.setAccountName(userDTO.getAccountName());
        userPO = userMapper.selectOne(userPO);

        if(null != userPO){
            //判断账户类型和登陆介质
            String errorMsg = this.judgeAccountTypeAndLoginMedium(userPO,userDTO.getAccountType());
            if(StringUtils.isNotEmpty(errorMsg)){
                log.error("userId:" + userPO.getUserId() + errorMsg);
                throw new ServiceException(errorMsg);
            }

            //判断用户有效性
            if(StatusEnum.INVALID.key == userPO.getStatus()){
                log.error("userId:" + userPO.getUserId() + "登录失败，此用户未启用");
                throw new ServiceException("登录失败，此用户未启用");
            }

            //密码登陆，校验密码
            if(StringUtils.isNotEmpty(userDTO.getPassword())){

                String encryptPwdParam = Md5Util.md5Encode(userDTO.getPassword() + userPO.getSalt());

                if(!encryptPwdParam.equals(userPO.getPassword())){
                    log.error("userId:" + userPO.getUserId() + "登录失败，密码错误");
                    throw new ServiceException("登录失败，密码错误");
                }
            }
            //验证码登陆，校验验证码
            else if(StringUtils.isNotEmpty(userDTO.getVerifyCode())){

                String cacheVerifyCode = VerifyCodeCache.get(userDTO.getAccountName());

                if(StringUtils.isEmpty(cacheVerifyCode)){
                    log.error("userId:" + userPO.getUserId() + "验证码已失效，请稍后重发");
                    throw new ServiceException("验证码已失效，请稍后重发");
                }
                if(!cacheVerifyCode.equals(userDTO.getVerifyCode())){
                    log.error("userId:" + userPO.getUserId() + "验证码错误");
                    throw new ServiceException("验证码错误");
                }
            }

        }else{
            //密码登陆，抛异常
            if(StringUtils.isNotEmpty(userDTO.getPassword())){
                if(null == userPO){
                    log.error("userId:" + userPO.getUserId() + "登录失败，此用户不存在");
                    throw new ServiceException("登录失败，此用户不存在");
                }
            }
            //验证码登陆，走自动登陆流程
            else if(StringUtils.isNotEmpty(userDTO.getVerifyCode())){
                //后台--验证码登陆不自动注册
                if(AccountTypeEnum.BACK_STAGE.key == userDTO.getAccountType()){
                    log.error("userId:" + userPO.getUserId() + "登录失败，此用户不存在");
                    throw new ServiceException("登录失败，此用户不存在");
                }

                //自动注册
                userPO = this.registerFromLogin(userDTO);
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

        UserDTO userDB = BeanUtil.transformBean(userPO,UserDTO.class);
        userDB.setAddressDTO(BeanUtil.transformBean(addressPO,AddressDTO.class));
        userDB.setPassword(null);
        userDB.setSalt(null);

        //生成token
        Jwt jwt = new Jwt();
        jwt.setUserId(userPO.getUserId());
        if(1 == userDTO.getFrom()){
            //pc    2天有效期
            jwt.setExpiredMillisecond(2*24*60*60*1000);
        }else if(2 == userDTO.getFrom()){
            //app   90天有效期
            jwt.setExpiredMillisecond(90*24*60*60*1000);
        }

        String token = null;
        try {
            token = JwtUtil.createJWT(jwt);
        } catch (Exception e) {
            log.error("登陆异常，token创建异常",e);
            throw new ServiceException("登陆失败，请重试");
        }

        //保存用户缓存
        UserCache.putUserToCache(userPO.getUserId(),userDB);
        userDB.setToken(token);
        //保存token缓存
        TokenCache.put(userPO.getUserId(),token);
        return userDB;
    }

    /**
     * 判断账户类型和登陆介质
     */
    public String judgeAccountTypeAndLoginMedium(UserPO userPO,Integer accountType){

        String errorMsg = null;

        if(AccountTypeEnum.BACK_STAGE.key == accountType){
            //平台登陆，需要管理员/后台操作员
            if(!userPO.getAccountType().equals(accountType)){
                errorMsg = "登录失败，无效的用户";
                return errorMsg;
            }
            if (StringUtils.isEmpty(userPO.getRoleCodes()) ||
                    (!userPO.getRoleCodes().contains(PlatformRoleCodeEnum.ADMIN.code)
                            && !userPO.getRoleCodes().contains(PlatformRoleCodeEnum.BACKSTAGE_OPR.code))) {
                errorMsg = "登录失败，此用户无后台管理操作权限";
                return errorMsg;
            }
        }else if(AccountTypeEnum.RECYCLE.key == accountType){
            //回收端登陆，需要回收端用户
            if(!userPO.getAccountType().equals(accountType)){
                errorMsg = "登录失败，无效的用户";
                return errorMsg;
            }
        }else if(AccountTypeEnum.PLATFORM.key == accountType){
            //交易端登陆，需要交易端用户/管理员/交易端操作员
            if (AccountTypeEnum.BACK_STAGE.key == userPO.getAccountType()) {
                if (StringUtils.isEmpty(userPO.getRoleCodes()) ||
                        (!userPO.getRoleCodes().contains(PlatformRoleCodeEnum.ADMIN.code)
                                && !userPO.getRoleCodes().contains(PlatformRoleCodeEnum.TRANSACTION_OPR.code))) {
                    errorMsg = "登录失败，此用户无交易端操作权限";
                    return errorMsg;
                }
            } else if (AccountTypeEnum.RECYCLE.key == userPO.getAccountType()) {
                errorMsg = "登录失败，无效的用户";
                return errorMsg;
            }
        }
        return errorMsg;
    }

    public UserPO registerFromLogin(UserDTO userDTO){
        UserPO userInsert = new UserPO();
        userInsert.setAccountType(userDTO.getAccountType());
        userInsert.setAccountName(userDTO.getAccountName());
        userInsert.setUserName(userDTO.getAccountName());
        userInsert.setStatus(StatusEnum.VALID.key);
        userInsert.setCreatedTime(DateUtil.getCurrentDateTimeStr());
        userInsert.setCreator(userDTO.getAccountName());
        userInsert.setIsInit(0);
        userInsert.setVerificationStatus(0);

        //设置默认密码，因为数据库密码不为空，但依然首次登陆需要初始化密码
        String pwd = NumberUtil.DEFAULT_PWD;
        String salt = NumberUtil.generateNumber(4);
        String encryptPwd = Md5Util.md5Encode(pwd+salt);
        userInsert.setPassword(encryptPwd);
        userInsert.setSalt(salt);

        userMapper.insert(userInsert);
        return userInsert;
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


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void modifyUser(UserDTO userDTO) {

        //修改密码，用户名，头像，行业认证……
        UserPO userPO = new UserPO();
        userPO.setUserId(userDTO.getUserId());
        userPO.setVerificationStatus(userDTO.getVerificationStatus());
        userPO.setVerificationContent(userDTO.getVerificationContent());
        userPO.setAvatarImgPath(userDTO.getAvatarImgPath());
        userPO.setUserName(userDTO.getUserName());
        userPO.setModifier(userDTO.getModifier());
        userPO.setModifiedTime(userDTO.getModifiedTime());

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


    @Transactional(rollbackFor = Exception.class)
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
        userPO.setIsInit(1);

        Example example = new Example(UserPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountName",userDTO.getAccountName());
        userMapper.updateByExampleSelective(userPO,example);

        //清除用户token缓存，下次重新登陆
        UserPO userSelect = new UserPO();
        userSelect.setAccountName(userDTO.getAccountName());
        userSelect = userMapper.selectOne(userSelect);
        TokenCache.remove(userSelect.getUserId());
    }

    @Override
    public AddressDTO queryUserAddressByUserId(Integer userId) {

        AddressPO addressPO = new AddressPO();
        addressPO.setUserId(userId);
        addressPO = addressMapper.selectOne(addressPO);
        return BeanUtil.transformBean(addressPO,AddressDTO.class);
    }


    @Transactional(rollbackFor = Exception.class)
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
