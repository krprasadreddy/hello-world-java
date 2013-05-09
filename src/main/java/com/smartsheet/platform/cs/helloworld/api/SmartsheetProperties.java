/**
 * This class contains important constant values for the Smartsheet API. Some values are constant, others must be provided in 
 * smartsheet.properties. See smartsheet.properties.sample for an example.
 */
package com.smartsheet.platform.cs.helloworld.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SmartsheetProperties {

	public static final String BASE_API_URL = "http://kyans.smartsheet.com:8088/dev2/rest/1.1";
//	public static final String BASE_API_URL = "https://api.smartsheet.com/1.1";
	public static final String TOKEN_URL = BASE_API_URL + "/token";
	public static final String SHEETS_URL = BASE_API_URL + "/sheets";
//	public static final String AUTHORIZE_URL = "https://www.smartsheet.com/b/authorize";
	public static final String AUTHORIZE_URL = "http://kyans.smartsheet.com:8080/dev2/authorize";
	
	public static final String GRANT_TYPE_AUTHORIZATION = "authorization_code";
	public static final String GRANT_TYPE_REFRESH = "refresh_token";

	
	private static final String CLIENT_ID ="client_id";
	private static final String CLIENT_SECRET ="client_secret";
	private static final String REDIRECT_URI ="redirect_uri";
	
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
	
	private static void initProps() {
		if (props == null) {
			InputStream is = SmartsheetProperties.class.getClassLoader().getResourceAsStream("smartsheet.properties");
			if (is == null) {
				throw new RuntimeException("Unable to locate smartsheet.properties file. Please use smartsheet.properties.sample as a guide.");
			}
			props = new Properties();
			try {
				props.load(is);
			} catch (IOException e) {
				throw new RuntimeException("An error occured while reading smartsheet.properties: ", e);
			}
		}
	}
	
}
