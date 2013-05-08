package com.smartsheet.platform.cs.helloworld.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smartsheet.platform.cs.helloworld.security.SecurityUtil;

@Controller
public class IndexController {
	
	/**
	 * This simply returns the welcome page index.jsp, present in
	 * src\main\webapp\WEB-INF\jsp folder. It will redirect to /home
	 * if the user is currently logged in.
	 * 
	 * @param locale
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response) {
		String target = "index";
		// check if existing cookie
		if (SecurityUtil.isRememberMe(request)) {
			target = "redirect:/home";
		}
		return target;
	}
	
	
}
