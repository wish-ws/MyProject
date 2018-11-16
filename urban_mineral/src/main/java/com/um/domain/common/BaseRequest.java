package com.um.domain.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : Owen
 * @desctiption :
 * @time : 2018-07-19.
 */
@Data
public class BaseRequest implements Serializable {


    private static final long serialVersionUID = 5610880452041438211L;

    /**
     * 当前页 默认1
     */
    private int currentPage = 1;

    /**
     * 页面记录数
     */
    private int pageSize = 20;
}
