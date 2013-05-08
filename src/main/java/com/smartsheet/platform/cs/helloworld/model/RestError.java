package com.smartsheet.platform.cs.helloworld.model;


/**
 * @author kskeem
 *	A model object to represent an error that can occur when calling the API.
 */
public class RestError {

	String error;
	String message;
	String errorCode;
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	

}
