package com.pepper.core.exception;

/**
 * 登录异常处理
 *
 * @author mrliu
 *
 */
public class AuthorizeException extends RuntimeException {


	private static final long serialVersionUID = -8351033652132474748L;

	public AuthorizeException(String message) {
		super(message);
		throw this;
	}

}
