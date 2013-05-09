/**
 * This class handles the HTTP communication between your Web Application and the Smartsheet API.
 * If you use new API methods from the Smartsheet API list, you can add those methods here, so that all the stuff is in one place here. 
 * 
 * Refer to com.smartsheet.platform.cs.helloworld.api.SmartsheetProperties for the URL values for the REST calls. 
 */
package com.smartsheet.platform.cs.helloworld.api;

import java.util.List;

import com.smartsheet.platform.cs.helloworld.model.AccessToken;
import com.smartsheet.platform.cs.helloworld.model.Sheet;

public interface SmartsheetAPI {

	/**
	 * Method to retrieve the Access Code, given the Code. This is the second part of the OAuth 2 dance.
	 * 
	 * @param code the code returned by the authorization flow.
	 * @return
	 * @throws Exception
	 */
	
	public AccessToken getAccessToken(String code) throws SmartsheetException;
	
	/**
	 * Method to refresh the Access Code. The refreshed {@link AccessToken} is saved to the database.
	 * 
	 * @param accessToken
	 * @return
	 * @throws SmartsheetException
	 */
	public AccessToken refreshAccessToken(AccessToken accessToken);
	
	/**
	 * Method to retrieve the Sheets for the user.
	 * 
	 * @param OAuthToken
	 * @return
	 * @throws SmartsheetException 
	 * @throws Exception
	 */
	public List<Sheet> getSheetList() throws SmartsheetException;

}
