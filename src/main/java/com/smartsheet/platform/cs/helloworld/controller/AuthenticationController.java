package com.smartsheet.platform.cs.helloworld.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smartsheet.platform.cs.helloworld.api.SmartsheetProperties;
import com.smartsheet.platform.cs.helloworld.security.SecurityUtil;

@Controller
public class AuthenticationController {
	
	/**
	 * Redirects the user to the Smartsheet authorization URL.
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request) {
		String state = UUID.randomUUID().toString();
		StringBuilder builder = new StringBuilder("redirect:");
		builder.append(SmartsheetProperties.getAuthorizeUrl());
		builder.append("?response_type=code&client_id=").append(SmartsheetProperties.getClientId());
		builder.append("&redirect_uri=").append(SmartsheetProperties.getRedirectURI());
		builder.append("&scope=READ_SHEETS&state=").append(state);
		request.getSession().setAttribute(SecurityUtil.OAUTH_STATE, state);
		
		return builder.toString();
	}
	
	/**
	 * Logs the user out by clearing the cookie and returning the user back to /index
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		SecurityUtil.clearRememberMe(request, response);
		return "index";
	}
}
