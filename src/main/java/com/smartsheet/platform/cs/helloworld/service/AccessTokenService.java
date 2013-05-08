package com.smartsheet.platform.cs.helloworld.service;

import com.smartsheet.platform.cs.helloworld.model.AccessToken;

/**
 * Interface for {@link AccessToken} Database operations.
 */
public interface AccessTokenService {

	public void saveToken(AccessToken oAuthToken);
	public AccessToken getToken(String tokenId);
}
