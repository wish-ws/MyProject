package com.um.domain.request;

import com.um.domain.common.BaseRequest;
import lombok.Data;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/21 12:01
 */
@Data
public class UserQueryRequest extends BaseRequest {

    private static final long serialVersionUID = -8827072721778116512L;

    /**
     * 登录名
     */
    private String accountName;

    /**
     * 用户类型
     */
    private Integer accountType;

}
