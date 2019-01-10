package com.pepper.core.exception;

public  class HandleableException extends RuntimeException {
	private static final long serialVersionUID = -4552090901512143756L;

	public HandleableException() {
		super();
	}

	public HandleableException(final String message) {
		super(message);
	}

	public HandleableException(Exception e) {
		super(e);
	}

	public HandleableException(String message, Throwable cause) {
		super(message, cause);
	}

	public HandleableException(Throwable cause) {
		super(cause);
	}

}