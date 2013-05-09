package com.smartsheet.platform.cs.helloworld.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpRequest;
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
		//Use the default intercepter. Note that this cannot be overridden after construction.
		//This interceptor is intrinsic to how this class must function, so we do not rely on Spring
		//to provide it.
		super.setInterceptors(Arrays.asList(new ClientHttpRequestInterceptor[]{new TokenRefreshingRequestInterceptor()}));
		setErrorHandler(new ResponseErrorHandler() {
			public boolean hasError(ClientHttpResponse response) throws IOException {
				return false;
			}
			public void handleError(ClientHttpResponse response) throws IOException {
			}
		});
	}

	@Override
	public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
		//No-op.
	}
	
	private class TokenRefreshingRequestInterceptor implements ClientHttpRequestInterceptor {

		private static final String AUTHORIZATION_HEADER = "Authorization";

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
			System.out.println("Made it here.");
			if (isAuthenticated) {
				if (accessTokenResolver.getToken().getExpires().before(new Date())) {
					ssApi.refreshAccessToken(accessTokenResolver.getToken());
				}
				request.getHeaders().add(AUTHORIZATION_HEADER, "Bearer " + accessTokenResolver.getToken().getToken());
			}
			
			ClientHttpResponse response = null;
			response = execution.execute(request, body);
			;
			if (response.getRawStatusCode() != 200) {
				error = objectMapper.readValue(response.getBody(), RestError.class);
				logger.severe("************" + error.getMessage() + " " + error.getErrorCode());
				if (doAutoRefresh && error.getErrorCode() == 1003) {
					//Token has expired, refresh the Access Token.
					logger.warning("refreshing token:");
					ssApi.refreshAccessToken(accessTokenResolver.getToken());
					request.getHeaders().remove(AUTHORIZATION_HEADER);
					request.getHeaders().add(AUTHORIZATION_HEADER, "Bearer " + accessTokenResolver.getToken().getToken());
					response = execution.execute(request, body); 
					if (response.getRawStatusCode() != 200) {
						error = objectMapper.readValue(response.getBody(), RestError.class);
						throw new ApiException(error);
					}
				} else {
					throw new ApiException(error);
				}
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
