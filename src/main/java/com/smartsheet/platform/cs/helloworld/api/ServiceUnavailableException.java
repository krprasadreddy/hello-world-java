package com.smartsheet.platform.cs.helloworld.api;

/**
 * Thrown when a HTTP request returns status code 503 (Service Unavailable).
 */
public class ServiceUnavailableException extends RuntimeException {

    public static final int SERVICE_UNAVAILABLE_CODE = 503;

    private static final long serialVersionUID = 1L;

    private final String url;

    public ServiceUnavailableException(String url) {
        super(SERVICE_UNAVAILABLE_CODE + " (Service Unavailable) accessing " + url);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
