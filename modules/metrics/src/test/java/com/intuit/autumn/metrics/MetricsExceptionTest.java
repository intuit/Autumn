package com.intuit.autumn.metrics;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MetricsExceptionTest {

    @Test
    public void constructor() throws Exception {
        new MetricsException();
    }

    @Test
    public void constructorMessage() throws Exception {
        String message = "message";

        MetricsException metricsException = new MetricsException(message);

        assertThat(metricsException.getMessage(), is(message));
    }

    @Test
    public void constructorMessageAndThrowable() throws Exception {
        String message = "message";
        Throwable throwable = new Throwable();

        MetricsException metricsException = new MetricsException(message, throwable);

        assertThat(metricsException.getMessage(), is(message));
        assertThat(metricsException.getCause(), is(throwable));
    }

    @Test
    public void constructorThrowable() throws Exception {
        Throwable throwable = new Throwable();

        MetricsException metricsException = new MetricsException(throwable);

        assertThat(metricsException.getCause(), is(throwable));
    }

    @Test
    public void constructorMessageAndThrowableAndEnableSuppressionAndWritableStackTrace() throws Exception {
        String message = "message";
        Throwable throwable = new Throwable();

        MetricsException metricsException = new MetricsException(message, throwable, true,
                true);

        assertThat(metricsException.getMessage(), is(message));
        assertThat(metricsException.getCause(), is(throwable));
    }
}