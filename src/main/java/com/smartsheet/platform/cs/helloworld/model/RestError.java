package com.smartsheet.platform.cs.helloworld.model;


/**
 * @author kskeem
 *	A model object to represent an error that can occur when calling the API.
 */
public class RestError {

	String error;
	String message;
	int errorCode;
	
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
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	

}
