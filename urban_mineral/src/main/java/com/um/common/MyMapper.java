package com.um.common;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;


/**
 * @class_name: MyMapper
 * @description: 被继承的Mapper，一般业务Mapper继承它
 * @author: ws
 * @date: 2018/11/12
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
    // 特别注意，该接口不能被扫描到，否则会出错
}