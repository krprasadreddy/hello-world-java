package com.smartsheet.platform.cs.helloworld.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smartsheet.platform.cs.helloworld.controller.RememberMeInterceptor;

/**
 * @author kskeem
 * This {@link Filter} is used to only allow access to the "protected" parts of the app
 * if a particular cookie is present. Validation of the cookie happens with the {@link RememberMeInterceptor}
 * class. This is only a rudimentary implementation and should be fleshed out much more thoroughly. <br/> <br/>
 * 
 * Note that the configuration of the "insecureTargets" is done through web.xml
 * 
 */
public class SecurityFilter implements Filter {
	private static final String INSECURE_TARGETS = "insecureTargets";
	
	Set<String> insecureTargets = new HashSet<String>();
    /**
     * Default constructor. 
     */
    public SecurityFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		boolean cookieFound = false;
		Cookie[] cookies = httpRequest.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(SecurityUtil.TOKEN_COOKIE_NAME) && cookie.getValue() != null && cookie.getValue().length() > 0) {
					cookieFound = true;
					break;
				}
			}
		}
		
		String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
		if (!cookieFound && !insecureTargets.contains(path)) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendRedirect("/");
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		String targets = fConfig.getInitParameter(INSECURE_TARGETS);
		if (targets != null) {
			String[] split = targets.split(",");
			for (String str: split) {
				insecureTargets.add(str);
			}
			
		}
	}

}
