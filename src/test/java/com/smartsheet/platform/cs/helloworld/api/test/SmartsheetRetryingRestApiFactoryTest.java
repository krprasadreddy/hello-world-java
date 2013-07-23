package com.smartsheet.platform.cs.helloworld.api.test;

import static com.smartsheet.platform.cs.helloworld.api.SmartsheetRetryingRestApiFactory.createSmartsheetRetryingRestApi;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.smartsheet.platform.cs.helloworld.api.ApiException;
import com.smartsheet.platform.cs.helloworld.api.RateLimitedRestApi;
import com.smartsheet.platform.cs.helloworld.api.ServiceUnavailableException;
import com.smartsheet.platform.cs.helloworld.api.SmartsheetRetryingRestApiFactory;
import com.smartsheet.platform.cs.helloworld.model.RestError;

/**
 * Tests the retrying {@link RateLimitedRestApi} instance created by
 * {@link SmartsheetRetryingRestApiFactory}.
 */
public class SmartsheetRetryingRestApiFactoryTest {

	private static final String TEST_API_URL = "https://api.smartsheet.com/1.1/sheets";
    private static final Class<?> TEST_API_RESPONSE_TYPE = String.class;
	private static final String TEST_API_REQUEST = "{\"request\": \"cas\"}";
	private static final String TEST_API_RESPONSE = "{\"response\": \"bar\"}";

    @Test
    public void recoversFromServiceUnavailableExceptionByRetrying() {
    	printTestHeader("recoversFromServiceUnavailableExceptionsByRetrying");

		// Wrap an implementation of RateLimitedRestApi which throws
		// ServiceUnavailableException the first time a method is called and
    	// then recovers on the next call of that method - i.e., recovers from a
    	// rate limit exceeded (503) error.
    	RateLimitedRestApi retryingRestApi = createSmartsheetRetryingRestApi(
			new RateLimitedRestApi() {

				private boolean firstGetCall = true;
				private boolean firstPostCall = true;

				@Override
				@SuppressWarnings("unchecked")
				public <T> T getForObject(String url, Class<T> responseType) throws ServiceUnavailableException {
					if (firstGetCall) {
						firstGetCall = false;
						throw new ServiceUnavailableException(url);
					}

					return (T) TEST_API_RESPONSE;
				}

				@Override
				@SuppressWarnings("unchecked")
				public <T> T postForObject(String url, Object request, Class<T> responseType) throws ServiceUnavailableException {
					if (firstPostCall) {
						firstPostCall = false;
						throw new ServiceUnavailableException(url);
					}

					return (T) TEST_API_RESPONSE;
				}});

    	// Verify GET & POST both succeed on wait & retry.
		assertEquals(TEST_API_RESPONSE, retryingRestApi.getForObject(TEST_API_URL, TEST_API_RESPONSE_TYPE));
		testLog("GET succeeded");

		assertEquals(TEST_API_RESPONSE, retryingRestApi.postForObject(TEST_API_URL, TEST_API_REQUEST, TEST_API_RESPONSE_TYPE));
		testLog("POST succeeded");
    }

    @Test
    public void retriesOnServiceUnavailableExceptionsUntilMaxRetries() {
    	printTestHeader("retriesOnServiceUnavailableExceptionsUntilMaxRetries");

		// Wrap an implementation of RateLimitedRestApi which always throws
		// ServiceUnavailableException - i.e., NEVER recovers from a rate limit
    	// exceeded (503) error.
    	RateLimitedRestApi retryingRestApi = createSmartsheetRetryingRestApi(
			new RateLimitedRestApi() {

				@Override
				public <T> T getForObject(String url, Class<T> responseType) throws ServiceUnavailableException {
					throw new ServiceUnavailableException(url);
				}

				@Override
				public <T> T postForObject(String url, Object request, Class<T> responseType) throws ServiceUnavailableException {
					throw new ServiceUnavailableException(url);
				}});

    	// Verify GET fails with ServiceUnavailableException in spite of wait & retry.
    	try {
    		retryingRestApi.getForObject(TEST_API_URL, TEST_API_RESPONSE_TYPE);
    		fail("GET should not succeed");

    	} catch (Exception e) {
    		assertTrue(e instanceof ServiceUnavailableException);
    		testLog("GET failed as expected with " + ServiceUnavailableException.SERVICE_UNAVAILABLE_CODE);
    	}

    	// Verify POST fails with ServiceUnavailableException in spite of wait & retry.
    	try {
    		retryingRestApi.postForObject(TEST_API_URL, TEST_API_REQUEST, TEST_API_RESPONSE_TYPE);
    		fail("POST should not succeed");

    	} catch (Exception e) {
    		assertTrue(e instanceof ServiceUnavailableException);
    		testLog("POST failed as expected with " + ServiceUnavailableException.SERVICE_UNAVAILABLE_CODE);
    	}
    }

    @Test
    public void failsOnLegitimateError() {
    	printTestHeader("failsOnLegitimateError");

		// Wrap an implementation of RateLimitedRestApi which always throws a
		// "legitimate" exception - i.e., not related to rate limiting, hence
    	// not necessitating any wait & retry attempts.
    	RateLimitedRestApi retryingRestApi = createSmartsheetRetryingRestApi(
			new RateLimitedRestApi() {

				@Override
				public <T> T getForObject(String url, Class<T> responseType) throws ServiceUnavailableException {
					throw new ApiException(404, new RestError());
				}

				@Override
				public <T> T postForObject(String url, Object request, Class<T> responseType) throws ServiceUnavailableException {
					throw new ApiException(404, new RestError());
				}});

    	// Verify GET fails with ApiException 404.
    	try {
    		retryingRestApi.getForObject(TEST_API_URL, TEST_API_RESPONSE_TYPE);
    		fail("GET should not succeed");

    	} catch (Exception e) {
    		assertTrue(e instanceof ApiException);
    		assertEquals(404, ((ApiException)e).getHttpStatusCode());
    		testLog("GET failed as expected with 404");
    	}

    	// Verify POST fails with ApiException 404.
    	try {
    		retryingRestApi.postForObject(TEST_API_URL, TEST_API_REQUEST, TEST_API_RESPONSE_TYPE);
    		fail("POST should not succeed");

    	} catch (Exception e) {
    		assertTrue(e instanceof ApiException);
    		assertEquals(404, ((ApiException)e).getHttpStatusCode());
    		testLog("POST failed as expected with 404");
    	}
    }

    @Test
    public void succeedsWhenRateLimitNotExceeded() {
    	printTestHeader("succeedsWhenRateLimitNotExceeded");

    	// Wrap an implementation of RateLimitedRestApi which always succeeds
    	// - i.e., not exceeding the API rate limit, and hence not
    	// necessitating any wait & retry attempts.
    	RateLimitedRestApi retryingRestApi = createSmartsheetRetryingRestApi(
			new RateLimitedRestApi() {

				@Override
				@SuppressWarnings("unchecked")
				public <T> T getForObject(String url, Class<T> responseType) throws ServiceUnavailableException {
					return (T) TEST_API_RESPONSE;
				}

				@Override
				@SuppressWarnings("unchecked")
				public <T> T postForObject(String url, Object request, Class<T> responseType) throws ServiceUnavailableException {
					return (T) TEST_API_RESPONSE;
				}});


    	// Verify GET & POST both succeed on wait & retry.
		assertEquals(TEST_API_RESPONSE, retryingRestApi.getForObject(TEST_API_URL, TEST_API_RESPONSE_TYPE));
		testLog("GET succeeded");

		assertEquals(TEST_API_RESPONSE, retryingRestApi.postForObject(TEST_API_URL, TEST_API_REQUEST, TEST_API_RESPONSE_TYPE));
		testLog("POST succeeded");
    }

	// helpers

	private static void printTestHeader(String testName) {
		waitForAppLogsToCatchUp();
		System.out.println("-------------------- TEST: " + testName  + " --------------------");
	}

	private static void testLog(String message) {
		waitForAppLogsToCatchUp();
		System.out.println(">>> TEST: " + message);
	}

	private static void waitForAppLogsToCatchUp() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
}
