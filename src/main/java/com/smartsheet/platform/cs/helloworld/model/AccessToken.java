package com.smartsheet.platform.cs.helloworld.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author kskeem
 *
 *	A Model class that represents the AccessToken both from the API and as it exists in the
 *	database.
 */
@Entity
public class AccessToken {

	@Id
	private String id;
	
	@JsonProperty("access_token")
	private String token;
	
	private String provider;
	
	@JsonProperty("refresh_token")
	private String refreshToken;
	
	@JsonProperty("expires_in")
	@Transient
	private Long expiresIn;

	@JsonProperty("token_type")
	@Transient
	private String tokenType;

	private Date expires;
	
	private Date lastLogin;

	public AccessToken() {

	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public void updateExpires() {
		setExpires(new Date(System.currentTimeMillis() + (expiresIn * 1000)));
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}
}
