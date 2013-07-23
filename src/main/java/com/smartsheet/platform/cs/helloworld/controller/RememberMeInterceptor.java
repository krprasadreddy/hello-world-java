package com.smartsheet.platform.cs.helloworld.controller;

import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.smartsheet.platform.cs.helloworld.model.AccessToken;
import com.smartsheet.platform.cs.helloworld.security.SecurityUtil;
import com.smartsheet.platform.cs.helloworld.service.AccessTokenService;


/**
 * This class handles checking for the remember-me cookie and validates that it is a valid {@link AccessToken#id}
 * 
 * Note: we look the {@link AccessToken} up from the database to validate it. We will look it up a second time any
 * time we need to use it for a REST call. Eventually, this should be optimized to save the AccessToken object in 
 * the request and only do this once.
 * 
 * @author kskeem
 *
 */
public class RememberMeInterceptor implements HandlerInterceptor {
	private static final Logger logger = Logger.getLogger(RememberMeInterceptor.class.toString());
	
	@Autowired
	AccessTokenService tokenService;
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Cookie[] cookies = request.getCookies();
		String tokenId = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(SecurityUtil.TOKEN_COOKIE_NAME) && cookie.getValue() != null && cookie.getValue().length() > 0) {
					tokenId = cookie.getValue();
					SecurityUtil.setTokenId(request, tokenId);
					break;
				}
			}
		}
		if (tokenId != null) {
			logger.info("TokenID:" + tokenId);
			AccessToken token = tokenService.getToken(tokenId);
			if (token == null) {
				logger.warning("Token not found. Clearing cookies:" + tokenId);
				SecurityUtil.clearRememberMe(request, response); 
				response.sendRedirect("/");
				return false;
			}
			
		}
		return true;
	}

}
