package com.smartsheet.platform.cs.helloworld.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smartsheet.platform.cs.helloworld.api.SmartsheetAPI;
import com.smartsheet.platform.cs.helloworld.api.SmartsheetException;
import com.smartsheet.platform.cs.helloworld.model.AccessToken;
import com.smartsheet.platform.cs.helloworld.model.Sheet;
import com.smartsheet.platform.cs.helloworld.security.SecurityUtil;
import com.smartsheet.platform.cs.helloworld.service.AccessTokenService;

@Controller
public class HomeController {
	
	@Autowired
	private SmartsheetAPI api;

	@Autowired
	private AccessTokenService tokenService;
	
	/**
	 * Takes you to the home page, which is currently just a list of sheets.
	 * 
	 * @return
	 * @throws SmartsheetException
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(HttpServletRequest request, Model model) throws SmartsheetException {
		List<Sheet> sheets = api.getSheetList();
		model.addAttribute("sheetsList", sheets);

		Date lastLogin = getLastLogin(request);
		model.addAttribute("lastLoginTime", lastLogin);

		return "home";
	}

	private Date getLastLogin(HttpServletRequest request) {
		String tokenId = SecurityUtil.getTokenId(request);
		AccessToken token = tokenService.getToken(tokenId);
		// Note: To have gotten here, access token must be available, since the API was called successfully.
		Date lastLogin = token.getLastLogin();
		if (lastLogin == null) {
			// For old token records created prior to the addition of lastLogin, use current time.
			lastLogin = new Date();
			token.setLastLogin(lastLogin);
			tokenService.saveToken(token);
		}
		return new Date(lastLogin.getTime()); // convert from SQL Timestamp to Java Date
	}
}
