package com.intuit.autumn.metrics;

/**
 * Holder for Metrics exception.
 */

public class MetricsException extends RuntimeException {

    public MetricsException() {
        super();
    }

    public MetricsException(String message) {
        super(message);
    }

    public MetricsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetricsException(Throwable cause) {
        super(cause);
    }

    protected MetricsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
