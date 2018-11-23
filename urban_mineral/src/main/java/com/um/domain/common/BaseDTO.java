package com.um.domain.common;

import com.um.util.DateUtil;
import com.um.util.StringUtil;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : ws
 * @project : com.um
 * @description : DTO父类
 * @date : 2018/7/11 14:57
 */
@Data
public class BaseDTO implements Serializable {


    private static final long serialVersionUID = -440294803194744306L;

    private String creator;

    private String createdTime;

    private String modifier;

    private String modifiedTime;


    public void setCreatedTime(String createdTime) {
        if(StringUtils.isNotEmpty(createdTime)){
            createdTime = DateUtil.dateFormat(createdTime,DateUtil.hour_format);
        }
        this.createdTime = createdTime;
    }

    public void setModifiedTime(String modifiedTime) {
        if(StringUtils.isNotEmpty(modifiedTime)){
            modifiedTime = DateUtil.dateFormat(modifiedTime,DateUtil.hour_format);
        }
        this.modifiedTime = modifiedTime;
    }
}
