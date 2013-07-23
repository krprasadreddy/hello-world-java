package com.smartsheet.platform.cs.helloworld.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityUtil {
	
	public static final String TOKEN_COOKIE_NAME = "tc";
	public static final String OAUTH_STATE = "state";
	
	private static final String TOKEN_KEY = "tokenKey";

	public static boolean isRememberMe(HttpServletRequest request) {
		return getTokenId(request) != null;
	}

	public static void clearRememberMe(HttpServletRequest request, HttpServletResponse response) {
		setTokenId(request, null);
		request.getSession().setAttribute(OAUTH_STATE, null);
		Cookie killerCookie = new Cookie(TOKEN_COOKIE_NAME, null);
		killerCookie.setPath("/");
		killerCookie.setMaxAge(0);
		response.addCookie(killerCookie);
	}

	public static String getTokenId(HttpServletRequest request) {
		return (String) request.getAttribute(TOKEN_KEY);
	}

	public static void setTokenId(HttpServletRequest request, String tokenId) {
		request.setAttribute(TOKEN_KEY, tokenId);
	}
}
