package com.intuit.autumn.client.impl;

/**
 * Holder for HttpCall exception.
 */

public class HttpCallException extends RuntimeException {

    public HttpCallException() {
        super();
    }

    public HttpCallException(String message) {
        super(message);
    }

    public HttpCallException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpCallException(Throwable cause) {
        super(cause);
    }

    protected HttpCallException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}