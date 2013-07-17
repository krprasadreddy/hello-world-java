package com.smartsheet.platform.cs.helloworld.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.smartsheet.platform.cs.helloworld.model.AccessToken;
import com.smartsheet.platform.cs.helloworld.security.SecurityUtil;

/**
 * @author kskeem
 * This class resolves the cookie value (which has been placed in the request by now) to 
 * the {@link AccessToken} associated with it.
 * 
 */
@Component
@Scope("request")
public class AccessTokenResolver {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private AccessTokenService tokenService;
	
	private AccessToken token;
	
	public AccessToken getToken() {
		if (token == null) {
			String tokenId = (String) request.getAttribute(SecurityUtil.TOKEN_KEY);
			if (tokenId == null) {
				return null;
			}
			token = tokenService.getToken(tokenId);
		}
		return token;
	}
	
	public void setToken(AccessToken token) {
		this.token = token;
	}
}
