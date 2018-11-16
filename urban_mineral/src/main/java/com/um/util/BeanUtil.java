package com.um.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @description 使用开源fastjson来转换bean
 * @author WS
 */
public class BeanUtil {


    /*
     * 转换单个bean的属性
     */
    public static <T,S> S transformBean(T t,Class<S> s){
        return JSONObject.parseObject(JSONObject.toJSONString(t),s);
    }


    /**
     * 转换List的属性
     */
    public static <T,S> List<S> transformList(List<T> t,Class<S> s){
        return JSONArray.parseArray(JSONArray.toJSONString(t),s);
    }


    /**
     * 转换object类型list的数据
     */
    public static <S> List<S> transformList(Object t,Class<S> s){
        return JSONArray.parseArray(JSON.toJSONString(t),s);
    }

}
