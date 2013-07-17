package com.smartsheet.platform.cs.helloworld.api;

import com.smartsheet.platform.cs.helloworld.model.RestError;

public class ApiException extends RuntimeException {
	private static final long serialVersionUID = -3997258058907517538L;
	
	private final int httpStatusCode;
	private final RestError restError;

	public ApiException(int httpStatusCode, RestError restError, Throwable t) {
		super(restError.getMessage(), t);
		this.httpStatusCode = httpStatusCode;
		this.restError = restError;
	}

	public ApiException(int httpStatusCode, RestError restError) {
		this(httpStatusCode, restError, null);
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public RestError getRestError() {
		return restError;
	}
}
