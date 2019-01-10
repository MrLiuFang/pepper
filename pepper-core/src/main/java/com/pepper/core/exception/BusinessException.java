package com.pepper.core.exception;

/**
 * dubbo服务层异常处理类
 *
 * @author mrliu
 *
 */
public class BusinessException extends  HandleableException {

	private static final long serialVersionUID = 7670720348542447804L;

	public BusinessException(Exception e) {
		super(e);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message) {
		super(message);
	}
}
