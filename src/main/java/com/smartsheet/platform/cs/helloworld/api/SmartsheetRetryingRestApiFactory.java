package com.smartsheet.platform.cs.helloworld.api;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * A factory which creates an instance of the {@link RateLimitedRestApi}
 * interface which implements the <i>wait and retry</i> algorithm recommended by
 * Smartsheet on rate limit exceeded errors from the Smartsheet API.
 */
public class SmartsheetRetryingRestApiFactory {

	public static RateLimitedRestApi createSmartsheetRetryingRestApi(RateLimitedRestApi delegate) {
		// Wrap the RateLimitedRestApi delegate in a proxy which implements
		// wait and retry on rate limit exceeded errors.
		return (RateLimitedRestApi) Proxy.newProxyInstance(
			delegate.getClass().getClassLoader(),
			new Class[] {RateLimitedRestApi.class},
			new RetryingRateLimitedRestApi(delegate));
	}

	private static class RetryingRateLimitedRestApi implements InvocationHandler {

		private static final Logger logger = Logger.getLogger(RetryingRateLimitedRestApi.class.toString());

		private static final int MAX_RETRIES = 5;
		private static final int WAIT_INTERVAL_SECS = 5;

		private final RateLimitedRestApi delegate;

		public RetryingRateLimitedRestApi(RateLimitedRestApi delegate) {
			this.delegate = delegate;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Throwable finalException = null;

			for (int i = 0; i <= MAX_RETRIES; i++) {
				if (i > 0)
					notifyIfRetry(i);

				try {
					return method.invoke(delegate, args);

				} catch (InvocationTargetException e) {
					Throwable cause = e.getCause();

					if (cause instanceof ServiceUnavailableException) {
						if (i < MAX_RETRIES)
							sleepForDefinedInterval(i+1);
						else
							finalException = cause;

					} else if (cause != null) // legitimate error (not related to rate limiting)
						throw cause;
					else // cause unknown
						throw e;
				}
			} // for

			// exhausted retries, give up
			throw finalException;
		}

		private static void sleepForDefinedInterval(int retryNumber) throws InterruptedException {
			int sleepSecs = retryNumber * WAIT_INTERVAL_SECS;
			logger.info("503 (Service Unavailable) received - sleep " + sleepSecs + " secs before retry...");
			Thread.sleep(TimeUnit.SECONDS.toMillis(sleepSecs));
		}

		private static void notifyIfRetry(int i) {
			logger.info("--- retry #" + i);
		}
	}
}
