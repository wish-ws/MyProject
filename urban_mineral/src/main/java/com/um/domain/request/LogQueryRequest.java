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
public class LogQueryRequest extends BaseRequest {


    private static final long serialVersionUID = 7224089974457216393L;
    /**
     * 登录名
     */
    private String accountName;


}
