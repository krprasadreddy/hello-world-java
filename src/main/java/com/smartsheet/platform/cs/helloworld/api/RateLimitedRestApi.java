package com.smartsheet.platform.cs.helloworld.api;

/**
 * A RESTful API which is rate limited, throwing {@link ServiceUnavailableException} 
 * when the limit is exceeded.
 */
public interface RateLimitedRestApi {

    <T> T getForObject(String url, Class<T> responseType) throws ServiceUnavailableException;

    <T> T postForObject(String url, Object request, Class<T> responseType) throws ServiceUnavailableException;
}
