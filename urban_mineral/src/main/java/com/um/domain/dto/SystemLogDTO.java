package com.um.domain.dto;

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
 * @description : 系统日志
 * @date : 2018/11/13 17:11
 */
@Data
public class SystemLogDTO extends BaseDTO {
    private static final long serialVersionUID = -686755535523373971L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 操作内容
     */
    private String logContent;

    /**
     * 操作人
     */
    private Integer userId;



}
