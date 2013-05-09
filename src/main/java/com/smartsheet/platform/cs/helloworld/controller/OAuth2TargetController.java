package com.smartsheet.platform.cs.helloworld.controller;

import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.smartsheet.platform.cs.helloworld.api.SmartsheetException;
import com.smartsheet.platform.cs.helloworld.api.SmartsheetAPI;
import com.smartsheet.platform.cs.helloworld.model.AccessToken;
import com.smartsheet.platform.cs.helloworld.security.SecurityUtil;

@Controller
public class OAuth2TargetController {
	private static final Logger logger = Logger.getLogger(SmartsheetController.class.toString());
	
	@Autowired
	private SmartsheetAPI api;
	
	/**
	 * This is the endpoint invoked by the Smartsheet OAuth module when you try
	 * to request the OAuth Access Code from the login page. If there is an
	 * error i.e. the User Denied Access to their Smartsheet data, then we will
	 * simply go to the error.jsp page. If the flow was successful, we will
	 * set a cookie to remember the user and redirect to home.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/target")
	public String target(HttpServletRequest request, HttpServletResponse response) throws SmartsheetException {
		// Extract out the data
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		String error = request.getParameter("error");
		
		String expectedState = (String) request.getSession().getAttribute(SecurityUtil.OAUTH_STATE);

		// Retrieve token from the database if exists
		if (error != null) {
			logger.severe("Error: " + error);
			throw new SmartsheetException(error);
		} else if (expectedState == null || state == null ||  !expectedState.equals(state)) {
			logger.info("State mismatch: expected:" + expectedState + " but found: " + state);
			//The provided state did not match what we have in the session:
			throw new SmartsheetException("Unaccptable state found. Please try again.");
		} else {
			logger.info("Success");
			AccessToken accessToken = api.getAccessToken(code);
			Cookie cookie = new Cookie(SecurityUtil.TOKEN_COOKIE_NAME, accessToken.getId());
			cookie.setMaxAge(Integer.MAX_VALUE);
			cookie.setPath("/");
			response.addCookie(cookie);
			return "redirect:/home";
		}
	}

}
