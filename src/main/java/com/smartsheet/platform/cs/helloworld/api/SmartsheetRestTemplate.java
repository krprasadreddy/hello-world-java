package com.smartsheet.platform.cs.helloworld.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.smartsheet.platform.cs.helloworld.model.AccessToken;
import com.smartsheet.platform.cs.helloworld.model.RestError;
import com.smartsheet.platform.cs.helloworld.service.AccessTokenResolver;

/**
 * @author kskeem
 *
 *	This class extends {@link RestTemplate} and provides some default behavior, such as adding the Authorization header and error handling. 
 *	This includes checking the expiration of the AccessToken and refreshing if necessary. Also, if a request fails because the AccessToken 
 * 	has expired, the token will be refreshed automatically and the call will be retried.
 */
@Service
@Scope("request")
public class SmartsheetRestTemplate extends RestTemplate {
	private static final Logger logger = Logger.getLogger(SmartsheetRestTemplate.class.toString());
	ResponseErrorHandler secondaryErrorHandler;
	
	@Autowired
	AccessTokenResolver accessTokenResolver;
	
	RestError error;
	
	boolean doAutoRefresh = true;
	boolean isAuthenticated = true;
	
	@Autowired
	SmartsheetAPI ssApi;

	@Autowired
	ObjectMapper objectMapper;
		
	public SmartsheetRestTemplate() {
		//Use the default error handler and intercepter. Note that these can be overridden after construction.
		setErrorHandler(new TokenRefreshResponseErrorHandler());
		setInterceptors(Arrays.asList(new ClientHttpRequestInterceptor[]{new AccessTokenRequestInterceptor()}));
	}
	
	private class TokenRefreshResponseErrorHandler implements ResponseErrorHandler {
		@Override
		public void handleError(ClientHttpResponse resp) throws IOException {
			logger.severe("Error found: " + resp.getBody());
			error = objectMapper.readValue(resp.getBody(), RestError.class);
		}
		@Override
		public boolean hasError(ClientHttpResponse resp) throws IOException {
			return resp.getStatusCode() != HttpStatus.OK;
		}
		
	}

	private class AccessTokenRequestInterceptor implements ClientHttpRequestInterceptor {

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
			if (isAuthenticated) {
				if (accessTokenResolver.getToken().getExpires().before(new Date())) {
					try {
						ssApi.refreshAccessToken(accessTokenResolver.getToken());
					} catch (ApiException e) {
						throw new IOException(e.getMessage(), e);
					}
				}
				request.getHeaders().add("Authorization", "Bearer " + accessTokenResolver.getToken().getToken());
			}
			
			ClientHttpResponse response = execution.execute(request, body);
			
			if (doAutoRefresh && response.getStatusCode() == HttpStatus.FORBIDDEN) {
				//Token has expired, refresh the Access Token.
				try {
					ssApi.refreshAccessToken(accessTokenResolver.getToken());
				} catch (ApiException e) {
					throw new IOException(e.getMessage(), e);
				}
				request.getHeaders().remove("Authorization");
				request.getHeaders().add("Authorization", "Bearer " + accessTokenResolver.getToken());
				response = execution.execute(request, body);
			}
			return response;
		}
		
	}
	
	/**
	 * If an error occurred on the REST call, a {@link RestError} will be returned.
	 * 
	 * @return the {@link RestError} or null if no error.
	 */
	public RestError getError() {
		return error;
	}

	public boolean isDoAutoRefresh() {
		return doAutoRefresh;
	}

	/**
	 * If an {@link AccessToken} has expired, by default, it will auto refresh as needed.
	 * Set this to false to prevent this from happening.
	 * @param doAutoRefresh
	 */
	public void setDoAutoRefresh(boolean doAutoRefresh) {
		this.doAutoRefresh = doAutoRefresh;
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	/**
	 * Sets whether the {@link AccessToken} is automatically added as a header. Set to false
	 * if the request is to be Unauthenticated. Default is true.
	 * @param isAuthenticated
	 */
	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}
}
