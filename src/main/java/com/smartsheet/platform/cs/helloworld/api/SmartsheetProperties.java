/**
 * This class contains important constant values for the Smartsheet API. Some values are constant, others must be provided in 
 * smartsheet.properties. See smartsheet.properties.sample for an example.
 */
package com.smartsheet.platform.cs.helloworld.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class SmartsheetProperties {
	private static final Logger logger = Logger.getLogger(SmartsheetProperties.class.toString());
	private static final String SMARTSHEET_PROPERTIES = "smartsheet.properties";
	private static final String TOKEN_URL = "/token";
	private static final String SHEETS_URL =  "/sheets";
	
	public static final String GRANT_TYPE_AUTHORIZATION = "authorization_code";
	public static final String GRANT_TYPE_REFRESH = "refresh_token";

	
	private static final String BASE_API_URL = "base_api_url";
	private static final String AUTHORIZE_URL = "authorize_url";
	private static final String CLIENT_ID ="client_id";
	private static final String CLIENT_SECRET ="client_secret";
	private static final String REDIRECT_URI ="redirect_uri";
	
	private static String baseUrl;
	private static String authorizeUrl;
	
	
	private static Properties props;
	
	public static String getClientId() {
		initProps();
		return props.getProperty(CLIENT_ID);
	}
	public static String getClientSecret() {
		initProps();
		return props.getProperty(CLIENT_SECRET);
	}
	public static String getRedirectURI() {
		initProps();
		return props.getProperty(REDIRECT_URI);
	}
	
	public static String getAuthorizeUrl() {
		initProps();
		return authorizeUrl;
	}

	public static String getTokenUrl() {
		initProps();
		return baseUrl + TOKEN_URL;
	}
	public static String getSheetsUrl() {
		initProps();
		return baseUrl + SHEETS_URL;
	}
	
	private static void initProps() {
		if (props == null) {
			String user = System.getProperty("user.name"); 
			String filename = user + "." + SMARTSHEET_PROPERTIES;
			InputStream is = SmartsheetProperties.class.getClassLoader().getResourceAsStream(filename);
			if (is == null) {
				filename = SMARTSHEET_PROPERTIES;
				is = SmartsheetProperties.class.getClassLoader().getResourceAsStream(filename);
			}
			if (is == null) {
				throw new RuntimeException("Unable to locate smartsheet.properties file. Please use smartsheet.properties.sample as a guide.");
			}
			logger.info("using property file: " + filename);
			props = new Properties();
			try {
				props.load(is);
				baseUrl = props.getProperty(BASE_API_URL);
				authorizeUrl = props.getProperty(AUTHORIZE_URL);
			} catch (IOException e) {
				throw new RuntimeException("An error occured while reading smartsheet.properties: ", e);
			}
		}
	}
	
}
