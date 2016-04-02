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

package com.intuit.data.autumn.web;

import com.google.inject.AbstractModule;
import com.intuit.data.autumn.manage.ManageModule;
import org.slf4j.Logger;

import java.util.Properties;

import static com.google.inject.Scopes.SINGLETON;
import static com.google.inject.name.Names.named;
import static java.lang.Integer.valueOf;
import static com.intuit.data.autumn.utils.PropertyFactory.create;
import static com.intuit.data.autumn.utils.PropertyFactory.getProperty;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * An injector that includes wire protocol implementation dependencies, e.g.: Jetty (HTTP/S)
 */

public class WebModule extends AbstractModule {

    public static final String PROPERTY_NAME = "/web.properties";
    public static final String SECRETS_PROPERTY_NAME = "/web-secrets.properties";
    private static final Logger LOGGER = getLogger(WebModule.class);

    /**
     * Inject module dependencies.
     */

    @Override
    protected void configure() {
        install(new WebServletModule());
        install(new ManageModule());

        LOGGER.debug("binding properties: {}", PROPERTY_NAME);

        Properties properties = create(PROPERTY_NAME, WebModule.class);
        Properties secretsProperties = create(SECRETS_PROPERTY_NAME, WebModule.class);

        bind(Integer.class).annotatedWith(named("application.http.port"))
                .toInstance(valueOf(getProperty("application.http.port", properties)));
        bind(String.class).annotatedWith(named("application.http.context.path"))
                .toInstance(String.valueOf(getProperty(
                        "application.http.context.path", properties)));

        Boolean isHttpsOn = Boolean.valueOf(getProperty("application.https.enabled",
                properties, "false"));
        bind(Boolean.class).annotatedWith(named("application.https.enabled"))
                .toInstance(isHttpsOn);
        bind(Boolean.class).annotatedWith(named("application.http.enabled"))
                .toInstance(Boolean.valueOf(getProperty("application.http.enabled", properties, "false")));
        bind(Integer.class).annotatedWith(named("application.https.port"))
                .toInstance(isHttpsOn ? Integer.parseInt(getProperty(
                        "application.https.port", properties)) : -1);
        bind(Integer.class).annotatedWith(named("application.http.idletimeout"))
                .toInstance(isHttpsOn ? Integer.parseInt(getProperty(
                        "application.http.idletimeout", properties)) : -1);
        bind(Integer.class).annotatedWith(
                named("application.httpconfig.output.buffersize"))
                .toInstance(isHttpsOn ? Integer.parseInt(getProperty(
                        "application.httpconfig.output.buffersize", properties)) : -1);
        bind(Integer.class).annotatedWith(named("application.https.idletimeout")).
                toInstance(isHttpsOn ? Integer.parseInt(getProperty(
                        "application.https.idletimeout", properties)) : -1);
        bind(String.class).annotatedWith(named("application.ssl.keystore.path"))
                .toInstance(isHttpsOn ? String.valueOf(getProperty(
                        "application.ssl.keystore.path", properties)) : "");

        bind(String.class).annotatedWith(named("application.ssl.keystore.password")).
                toInstance(isHttpsOn ? String.valueOf(getProperty(
                        "application.ssl.keystore.password", secretsProperties)) : "");
        bind(String.class).annotatedWith(named("application.ssl.keymanager.password")).
                toInstance(isHttpsOn ? String.valueOf(getProperty(
                        "application.ssl.keymanager.password", secretsProperties)) : "");

        bind(HttpHeader.class).in(SINGLETON);
        bind(HttpService.class).in(SINGLETON);
        bind(HttpsService.class).in(SINGLETON);

        LOGGER.debug("bound properties: {}", PROPERTY_NAME);
    }
}
