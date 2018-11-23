package com.um.domain.dto;

import com.um.domain.common.BaseDTO;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : ws
 * @project : com.um
 * @description : 客户反馈
 * @date : 2018/11/13 17:28
 */
@Data
public class ClientFeedbackDTO extends BaseDTO {

    private static final long serialVersionUID = -889291211815291164L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 反馈内容
     */
    private String feedbackContent;

    /**
     * 反馈人电话
     */
    private String creatorAccountName;


}
