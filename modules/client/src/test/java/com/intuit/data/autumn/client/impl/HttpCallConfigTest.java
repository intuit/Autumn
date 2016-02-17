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
import com.intuit.data.autumn.client.HttpCallConfig;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HttpCallConfigTest {

    @Test
    public void testProxyPort() throws Exception {
        HttpCallConfig httpCallConfig = new HttpCallConfig<>();

        httpCallConfig.setProxyPort(5);

        assertThat(httpCallConfig.getProxyPort(), is(5));
    }

    @Test
    public void testHttpMethod() throws Exception {
        HttpCallConfig httpCallConfig = new HttpCallConfig<>();

        httpCallConfig.setHttpMethod("foo");

        assertThat(httpCallConfig.getHttpMethod(), is("foo"));
    }

    @Test
    public void testBuilder() throws Exception {
        HttpCallConfig.Builder httpCallConfigBuilder = HttpCallConfig.Builder.aHttpCallConfig();
        HttpCallConfig.Builder builder = httpCallConfigBuilder.withHttpMethod("foo")
                .withConnectionTimeout(5)
                .withReadTimeOut(6)
                .withProxyURL("bar")
                .withProxyPort(7)
                .withUseConnectionPooling(FALSE)
                .withMaxConnectionPerHost(8)
                .but();
        HttpCallConfig httpCallConfig = builder.build();

        assertThat(httpCallConfig.getConnectionTimeout().isPresent(), is(TRUE));
        assertThat(httpCallConfig.getConnectionTimeout().get().equals(5), is(TRUE));
        assertThat(httpCallConfig.getReadTimeOut().isPresent(), is(TRUE));
        assertThat(httpCallConfig.getReadTimeOut().get().equals(6), is(TRUE));
        assertThat(httpCallConfig.getProxyURL().isPresent(), is(TRUE));
        assertThat(httpCallConfig.getProxyURL().get().equals("bar"), is(TRUE));
        assertThat(httpCallConfig.getProxyPort(), is(7));
        assertThat(httpCallConfig.getUseConnectionPooling(), is(FALSE));
        assertThat(httpCallConfig.getMaxConnectionPerHost().isPresent(), is(TRUE));
        assertThat(httpCallConfig.getMaxConnectionPerHost().get().equals(8), is(TRUE));
    }
}
