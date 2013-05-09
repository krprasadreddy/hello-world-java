package com.smartsheet.platform.cs.helloworld.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smartsheet.platform.cs.helloworld.api.SmartsheetException;
import com.smartsheet.platform.cs.helloworld.api.SmartsheetAPI;
import com.smartsheet.platform.cs.helloworld.model.Sheet;

@Controller
public class HomeController {
	
	@Autowired
	SmartsheetAPI api;
	
	/**
	 * Takes you to the home page, which is currently just a list of sheets.
	 * 
	 * @return
	 * @throws SmartsheetException
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) throws SmartsheetException {
		List<Sheet> sheets = api.getSheetList();
		model.addAttribute("sheetsList", sheets);
		return "sheets";

	}
}
