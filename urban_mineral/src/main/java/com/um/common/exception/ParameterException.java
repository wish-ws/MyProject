package com.um.common.exception;

/**
 * 参数异常
 * @author ws
 */
public class ParameterException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ParameterException() {
		super();
	}

	public ParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParameterException(String message) {
		super(message);
	}

	public ParameterException(Throwable cause) {
		super(cause);
	}
	
}
