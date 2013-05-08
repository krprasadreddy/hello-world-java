package com.smartsheet.platform.cs.helloworld.api;

public class ApiException extends Exception {
	private static final long serialVersionUID = -3997258058907517538L;
	
	public ApiException() {
		super();
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiException(String message) {
		super(message);
	}

	public ApiException(Throwable cause) {
		super(cause);
	}
	
}
