package com.smartsheet.platform.cs.helloworld.api;

public class SmartsheetException extends Exception {
	private static final long serialVersionUID = -3997258058907517538L;
	
	public SmartsheetException() {
		super();
	}

	public SmartsheetException(String message, Throwable cause) {
		super(message, cause);
	}

	public SmartsheetException(String message) {
		super(message);
	}

	public SmartsheetException(Throwable cause) {
		super(cause);
	}
	
}
