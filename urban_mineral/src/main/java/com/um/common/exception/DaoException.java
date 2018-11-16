package com.um.common.exception;

/**
 * Dao层异常
 * @author ws
 */
public class DaoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DaoException() {
		super();
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}
	

}
