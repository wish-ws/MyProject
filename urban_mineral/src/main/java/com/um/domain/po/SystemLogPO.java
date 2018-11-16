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
 * @description : 系统日志
 * @date : 2018/11/13 17:11
 */
@Data
@Table(name="t_system_log")
public class SystemLogPO extends BaseDTO {
    private static final long serialVersionUID = -686755535523373971L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
