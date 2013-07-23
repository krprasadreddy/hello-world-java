package com.smartsheet.platform.cs.helloworld.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * A model class to represent the User returned by the Smartsheet API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	private long id;
	private String email;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
