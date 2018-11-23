package com.um.domain.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Jwt implements Serializable {
 
	private static final long serialVersionUID = 1;
 
	/**
	 * token过期毫秒数
	 * 2*60*60*1000; 2h
	 */
	private long expiredMillisecond;

	/**
	 * 用户id
	 */
	private Integer userId;


	/**
	 * token解析异常，
	 * 返回编码
	 */
	private String parseResCode;

	/**
	 * 1成功0失败
	 */
	private int result;

}
