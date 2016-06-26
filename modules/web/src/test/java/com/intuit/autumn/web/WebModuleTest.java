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

import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuit.autumn.utils.PropertyFactory;
import org.easymock.IAnswer;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Properties;

import static com.google.common.primitives.Ints.tryParse;
import static com.google.inject.Guice.createInjector;
import static com.google.inject.name.Names.named;
import static com.intuit.autumn.utils.PropertyFactory.create;
import static org.easymock.EasyMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;

public class WebModuleTest {

    @Test
    public void injector() throws Exception {
        Injector injector = createInjector(new WebModule());
        Properties properties = create(WebModule.PROPERTY_NAME);
        Properties secretsProperties = create(WebModule.SECRETS_PROPERTY_NAME);

        assertThat(injector.getInstance(new Key<Integer>(named("application.http.port")) {
        }), equalTo(tryParse(properties.getProperty("application.http.port"))));
        assertThat(injector.getInstance(new Key<Integer>(named("application.https.port")) {
        }), equalTo(tryParse(properties.getProperty("application.https.port"))));
        assertThat(injector.getInstance(new Key<Integer>(named("application.httpconfig.output.buffersize")) {
        }), equalTo(tryParse(properties.getProperty("application.httpconfig.output.buffersize"))));
        assertThat(injector.getInstance(new Key<Integer>(named("application.https.idletimeout")) {
        }), equalTo(tryParse(properties.getProperty("application.https.idletimeout"))));
        assertThat(injector.getInstance(new Key<String>(named("application.ssl.keystore.path")) {
        }), equalTo(properties.getProperty("application.ssl.keystore.path")));
        assertThat(injector.getInstance(new Key<String>(named("application.ssl.keystore.password")) {
        }), equalTo(secretsProperties.getProperty("application.ssl.keystore.password")));
        assertThat(injector.getInstance(new Key<String>(named("application.ssl.keymanager.password")) {
        }), equalTo(secretsProperties.getProperty("application.ssl.keymanager.password")));

        injector.getInstance(HttpService.class);
        injector.getInstance(HttpsService.class);
    }

    @Ignore("powermock conflicts with ManageModule")
    @Test
    public void injectorWithHttpsDisabled() throws Exception {
        final Properties baseProperties = new Properties();

        baseProperties.load(WebModuleTest.class.getResourceAsStream("/web-none-enabled.properties"));

        mockStatic(PropertyFactory.class);
        expect(PropertyFactory.create(WebModule.PROPERTY_NAME, WebModuleTest.class)).andReturn(baseProperties);
        expect(PropertyFactory.getProperty(anyString(), eq(baseProperties))).andAnswer(new IAnswer<String>() {
            @Override
            public String answer() throws Throwable {
                return baseProperties.getProperty((String) getCurrentArguments()[0]);
            }
        }).anyTimes();
        replay(PropertyFactory.class);

        Injector injector = createInjector(new WebModule());
        Properties properties = create("/web-none-enabled.properties");
//        Properties secretsProperties = create(SECRETS_PROPERTY_NAME);

        assertThat(injector.getInstance(new Key<Integer>(named("application.http.port")) {
        }), equalTo(tryParse(properties.getProperty("application.http.port"))));

        assertThat(injector.getInstance(new Key<Integer>(named("application.https.port")) {
        }), equalTo(tryParse(properties.getProperty("application.https.port"))));
        assertThat(injector.getInstance(new Key<Integer>(named("application.httpconfig.output.buffersize")) {
        }), equalTo(tryParse(properties.getProperty("application.httpconfig.output.buffersize"))));
        assertThat(injector.getInstance(new Key<Integer>(named("application.https.idletimeout")) {
        }), equalTo(tryParse(properties.getProperty("application.https.idletimeout"))));
        assertThat(injector.getInstance(new Key<String>(named("application.ssl.keystore.path")) {
        }), equalTo(properties.getProperty("application.ssl.keystore.path")));
//        assertThat(injector.getInstance(new Key<String>(named("application.ssl.keystore.password")) {
//        }), equalTo(secretsProperties.getProperty("application.ssl.keystore.password")));
//        assertThat(injector.getInstance(new Key<String>(named("application.ssl.keymanager.password")) {
//        }), equalTo(secretsProperties.getProperty("application.ssl.keymanager.password")));

        injector.getInstance(HttpService.class);
        injector.getInstance(HttpsService.class);
    }
}

