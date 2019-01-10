package com.pepper.core.exception;

/**
 * 用户权限处理
 *
 * @author mrliu
 *
 */
public class NoPermissionException extends HandleableException {

	private static final long serialVersionUID = 4645560315083250463L;

	public NoPermissionException(String message) {
		super(message);
	}
}
