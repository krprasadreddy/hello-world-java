package com.smartsheet.platform.cs.helloworld.api;

/**
 * A Smartsheet-specific implementation of the {@link RateLimitedRestApi} interface.
 */
public class SmartsheetRateLimitedRestApi implements RateLimitedRestApi {

    private final SmartsheetRestTemplate restTemplate;

    public SmartsheetRateLimitedRestApi(SmartsheetRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

	@Override
	public <T> T getForObject(String url, Class<T> responseType) throws ServiceUnavailableException {
		try {
			return restTemplate.getForObject(url, responseType);
			
		} catch (ApiException e) {
			throw mapApiException(url, e);
		}
	}

    @Override
    public <T> T postForObject(String url, Object request, Class<T> responseType) throws ServiceUnavailableException {
		try {
			return restTemplate.postForObject(url, request, responseType);
		
		} catch (ApiException e) {
			throw mapApiException(url, e);
		}
    }

	/**
	 * Maps an {@link ApiException} to a {@link ServiceUnavailableException} 
	 * if appropriate, otherwise returning the original {@link ApiException}. 
	 */
	private static RuntimeException mapApiException(String url, ApiException apiException) {
		if (apiException.getHttpStatusCode() == ServiceUnavailableException.SERVICE_UNAVAILABLE_CODE)
			return new ServiceUnavailableException(url);
		
		return apiException;
	}
}
