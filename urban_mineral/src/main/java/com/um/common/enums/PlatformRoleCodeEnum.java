package com.um.common.enums;

import com.um.util.StringUtil;
import org.apache.commons.lang.StringUtils;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/21 14:56
 */
public enum PlatformRoleCodeEnum {

    ADMIN("R001","管理员"),
    BACKSTAGE_OPR("R002","后台操作员"),
    TRANSACTION_OPR("R003","交易端操作员");

    public String code;
    public String desc;

    PlatformRoleCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static String getDescByCode(String code){
        String desc = null;
        if(StringUtils.isEmpty(code)){
            return desc;
        }
        for (PlatformRoleCodeEnum platformRoleCodeEnum : PlatformRoleCodeEnum.values()) {
            if(platformRoleCodeEnum.code.equals(code)){
                desc = platformRoleCodeEnum.desc;
            }
        }
        return desc;
    }
}
