/**
 * This class handles the HTTP communication between your Web Application and the Smartsheet API.
 * If you use new API methods from the Smartsheet API list, you can add those methods here, so that all the stuff is in one place here. 
 * If a large number of API calls are added, consider breaking them out into separate classes based on logical relationships.
 * 
 */
package com.smartsheet.platform.cs.helloworld.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import com.smartsheet.platform.cs.helloworld.model.AccessToken;
import com.smartsheet.platform.cs.helloworld.model.Sheet;
import com.smartsheet.platform.cs.helloworld.model.User;
import com.smartsheet.platform.cs.helloworld.service.AccessTokenResolver;
import com.smartsheet.platform.cs.helloworld.service.AccessTokenService;

@Service
@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
public class SmartsheetAPIImpl implements SmartsheetAPI {

	private static final String AUTHORIZATION_HEADER = "Authorization";

	private static final String SMARTSHEET_PROVIDER = "Smartsheet";

	@Autowired
	SmartsheetRestTemplate restTemplate;
	
	@Autowired 
	AccessTokenService tokenService;
	
	@Autowired
	AccessTokenResolver tokenResolver;
	
	/* (non-Javadoc)
	 * @see com.smartsheet.platform.cs.helloworld.api.SmartsheetAPI#getAccessToken(java.lang.String)
	 */
	@Override
	public AccessToken getAccessToken(String code) throws SmartsheetException {

		try {
			String doHash = SmartsheetProperties.getClientSecret() + "|" + code;
			MessageDigest md;
			md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(doHash.getBytes("UTF-8"));
			String hex = Hex.encodeHexString(digest);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("client_id", SmartsheetProperties.getClientId());
			map.add("hash", hex);
			map.add("redirect_uri", SmartsheetProperties.getRedirectURI());
			map.add("grant_type", SmartsheetProperties.GRANT_TYPE_AUTHORIZATION);
			map.add("code", code);
			restTemplate.setAuthenticated(false);
			restTemplate.setDoAutoRefresh(false);
			// Send the request.
			AccessToken accessToken = restTemplate.postForObject(SmartsheetProperties.getTokenUrl(), map, AccessToken.class);
			long userId = getUserIdentity(accessToken.getToken());
			// Use the user's id (which is unique) as the token persistent id (and cookie value).
			accessToken.setId(String.valueOf(userId));
			accessToken.setExpires(new Date(System.currentTimeMillis() + (accessToken.getExpiresIn() * 1000)));
			accessToken.setProvider(SMARTSHEET_PROVIDER);
			// Would prefer not to have a DB call here, but leaving it for simplicity's sake.
			// Note if token record already exists, overwritten with new data.
			tokenService.saveToken(accessToken);
			return accessToken;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private long getUserIdentity(String accessToken) throws SmartsheetException {
		HttpHeaders headers = new HttpHeaders();
		headers.add(AUTHORIZATION_HEADER, "Bearer " + accessToken);
		HttpEntity<Void> request = new HttpEntity<Void>(headers);

		try {
			User user = restTemplate.exchange(
				SmartsheetProperties.getUserMeUrl(), HttpMethod.GET, request, User.class).getBody();
			return user.getId();

		} catch (RestClientException e) {
			throw new SmartsheetException(e);
		}
	}

	
	/* (non-Javadoc)
	 * @see com.smartsheet.platform.cs.helloworld.api.SmartsheetAPI#refreshAccessToken(com.smartsheet.platform.cs.helloworld.model.AccessToken)
	 */
	@Override
	public AccessToken refreshAccessToken(AccessToken accessToken) {

		try {

			String doHash = SmartsheetProperties.getClientSecret() + "|" + accessToken.getRefreshToken();
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(doHash.getBytes("UTF-8"));
			String hex = Hex.encodeHexString(digest);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("client_id", SmartsheetProperties.getClientId());
			map.add("hash", hex);
			map.add("redirect_uri", SmartsheetProperties.getRedirectURI());
			map.add("grant_type", SmartsheetProperties.GRANT_TYPE_REFRESH);
			map.add("refresh_token", accessToken.getRefreshToken());
			
			// Send the request
			restTemplate.setAuthenticated(false);
			restTemplate.setDoAutoRefresh(false);
			AccessToken newAccessToken = restTemplate.postForObject(SmartsheetProperties.getTokenUrl(), map, AccessToken.class);
			accessToken.setToken(newAccessToken.getToken());
			accessToken.setRefreshToken(newAccessToken.getRefreshToken());
			accessToken.setExpiresIn(newAccessToken.getExpiresIn());
			accessToken.setProvider(SMARTSHEET_PROVIDER);
			//Again, would prefer not to have a DB call here, but leaving it for simplicity's sake.
			tokenService.saveToken(accessToken);
			tokenResolver.setToken(accessToken);
			return accessToken;
		} catch (IOException ex) {
			//Only happens if the md.digest fails. Probably won't. 
			throw new RuntimeException(ex.getMessage());
		} catch (NoSuchAlgorithmException e) {
			//Only happens if the SHA-256 is not valued. Probably is valid. 
			throw new RuntimeException(e.getMessage());
		}
	}

	
	/* (non-Javadoc)
	 * @see com.smartsheet.platform.cs.helloworld.api.SmartsheetAPI#getSheetList()
	 */
	@Override
	public List<Sheet> getSheetList() throws SmartsheetException {
		List<Sheet> sheets = restTemplate.getForObject(SmartsheetProperties.getSheetsUrl(), Sheet.SheetList.class);
		return sheets;
	}

}
