package com.um.common.config;


import com.um.common.enums.ErrorCodeEnum;
import com.um.domain.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常捕获
 */
@Slf4j
@ControllerAdvice
public class GlobalDefultExceptionHandler {

	//声明要捕获的异常
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Response defultExcepitonHandler(HttpServletRequest request, Exception e) {
		Response res = new Response();
		res.setResult(0);
		res.setFailCode(ErrorCodeEnum.SYSTEM_EXCEPTION.errorCode);
		res.setFailReason(ErrorCodeEnum.SYSTEM_EXCEPTION.errorDesc);
		log.error("系统异常",e);
		return res;
	}
}
