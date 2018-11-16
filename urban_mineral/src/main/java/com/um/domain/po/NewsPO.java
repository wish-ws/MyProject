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
 * @description : 行业资讯
 * @date : 2018/11/13 17:30
 */
@Data
@Table(name = "t_news")
public class NewsPO extends BaseDTO {

    private static final long serialVersionUID = 6858741213448367183L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资讯标题
     */
    private String title;

    /**
     * 资讯内容
     */
    private String content;
}
