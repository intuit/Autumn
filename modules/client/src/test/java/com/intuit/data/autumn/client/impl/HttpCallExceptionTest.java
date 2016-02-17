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

package com.intuit.data.autumn.client.impl;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HttpCallExceptionTest {

    @Test
    public void constructor() throws Exception {
        new HttpCallException();
    }

    @Test
    public void constructorMessage() throws Exception {
        String message = "message";

        HttpCallException httpCallException = new HttpCallException(message);

        assertThat(httpCallException.getMessage(), is(message));
    }

    @Test
    public void constructorMessageAndThrowable() throws Exception {
        String message = "message";
        Throwable throwable = new Throwable();

        HttpCallException httpCallException = new HttpCallException(message, throwable);

        assertThat(httpCallException.getMessage(), is(message));
        assertThat(httpCallException.getCause(), is(throwable));
    }

    @Test
    public void constructorThrowable() throws Exception {
        Throwable throwable = new Throwable();

        HttpCallException httpCallException = new HttpCallException(throwable);

        assertThat(httpCallException.getCause(), is(throwable));
    }

    @Test
    public void constructorMessageAndThrowableAndEnableSuppressionAndWritableStackTrace() throws Exception {
        String message = "message";
        Throwable throwable = new Throwable();

        HttpCallException httpCallException = new HttpCallException(message, throwable, true,
                true);

        assertThat(httpCallException.getMessage(), is(message));
        assertThat(httpCallException.getCause(), is(throwable));
    }
}
