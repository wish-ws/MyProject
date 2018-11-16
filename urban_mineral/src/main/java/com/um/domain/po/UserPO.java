package com.um.domain.po;

import com.um.domain.common.BaseDTO;
import com.um.domain.common.BaseDTO;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : ws
 * @project : com.um
 * @description : 用户对象
 * @date : 2018/11/12 18:28
 */
@Data
@Table(name="t_user")
public class UserPO extends BaseDTO {

    private static final long serialVersionUID = -6864142651933023411L;

    /**
     * 用户id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


}
