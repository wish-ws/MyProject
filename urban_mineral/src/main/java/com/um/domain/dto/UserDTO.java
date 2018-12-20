package com.um.domain.dto;

import com.alibaba.fastjson.JSON;
import com.um.common.enums.PlatformRoleCodeEnum;
import com.um.domain.common.BaseDTO;
import com.um.domain.po.AddressPO;
import com.um.util.BeanUtil;
import com.um.util.DateUtil;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description : 用户对象
 * @date : 2018/11/12 18:28
 */
@Data
public class UserDTO extends BaseDTO {

    private static final long serialVersionUID = -6864142651933023411L;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 登陆名
     */
    private String accountName;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 密码盐值
     */
    private String salt;

    /**
     * 账户类型
     */
    private Integer accountType;

    /**
     * 角色编码拼接
     */
    private String roleCodes;

    /**
     * 角色编码拼接前端传参
     */
    private List<String> roleCodeList;

    /**
     * 角色名称拼接
     */
    private String roleNames;

    /**
     * 状态1启用0禁用
     */
    private Integer status;

    /**
     * 头像路径
     */
    private String avatarImgPath;

    /**
     * 行业认证状态
     */
    private Integer verificationStatus;

    /**
     * 行业认证内容
     */
    private String verificationContent;


    /**
     * 地址对象
     */
    private AddressDTO addressDTO;

    /**
     * 最后登陆时间
     */
    private String lastLoginTime;

    /**
     * 是否初始化密码
     * 1是0否
     */
    private Integer isInit;

    /**
     * 注册用手机验证码
     */
    private String verifyCode;

    /**
     * 1pc
     * 2app
     */
    private Integer from;

    private String token;

    public void setLastLoginTime(String lastLoginTime) {
        if(StringUtils.isNotEmpty(lastLoginTime)){
            lastLoginTime = DateUtil.dateFormat(lastLoginTime,"yyyy-MM-dd HH:mm");
        }
        this.lastLoginTime = lastLoginTime;
    }


    public String getRoleNames() {
        String roleNames = null;
        if(StringUtils.isNotEmpty(this.roleCodes)){
            String [] roleCodeArr = this.roleCodes.split(",");
            StringBuffer sb = new StringBuffer();
            for (String s : roleCodeArr) {
                sb.append(PlatformRoleCodeEnum.getDescByCode(s)).append("、");
            }
            if(sb.length() > 0){
                roleNames = sb.substring(0,sb.length() - 1);
            }
        }
        return roleNames;
    }


    public AddressDTO getAddressDTO() {
        if(null == addressDTO){
            return new AddressDTO();
        }
        return addressDTO;
    }


}
