package com.smartsheet.platform.cs.helloworld.api;

import com.smartsheet.platform.cs.helloworld.model.RestError;

public class ApiException extends RuntimeException {
	private static final long serialVersionUID = -3997258058907517538L;
	RestError restError;
	
	public ApiException() {
		super();
	}

	public ApiException(RestError restError, Throwable t) {
		super(restError.getMessage(), t);
		this.restError = restError;
	}

	public ApiException(RestError restError) {
		super(restError.getMessage());
		this.restError = restError;
	}

	public RestError getRestError() {
		return restError;
	}
	
}
