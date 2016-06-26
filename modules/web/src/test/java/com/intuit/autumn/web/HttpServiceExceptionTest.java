/*
 * Copyright 2016 Intuit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intuit.autumn.web;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HttpServiceExceptionTest {

    @Test
    public void constructor() throws Exception {
        new HttpServiceException();
    }

    @Test
    public void constructorMessage() throws Exception {
        String message = "message";

        HttpServiceException httpServiceException = new HttpServiceException(message);

        assertThat(httpServiceException.getMessage(), is(message));
    }

    @Test
    public void constructorMessageAndThrowable() throws Exception {
        String message = "message";
        Throwable throwable = new Throwable();

        HttpServiceException httpServiceException = new HttpServiceException(message, throwable);

        assertThat(httpServiceException.getMessage(), is(message));
        assertThat(httpServiceException.getCause(), is(throwable));
    }

    @Test
    public void constructorThrowable() throws Exception {
        Throwable throwable = new Throwable();

        HttpServiceException httpServiceException = new HttpServiceException(throwable);

        assertThat(httpServiceException.getCause(), is(throwable));
    }

    @Test
    public void constructorMessageAndThrowableAndEnableSuppressionAndWritableStackTrace() throws Exception {
        String message = "message";
        Throwable throwable = new Throwable();

        HttpServiceException httpServiceException = new HttpServiceException(message, throwable, true,
                true);

        assertThat(httpServiceException.getMessage(), is(message));
        assertThat(httpServiceException.getCause(), is(throwable));
    }
}
