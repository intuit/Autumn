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

package com.intuit.data.autumn.view;

import com.google.inject.AbstractModule;
import com.intuit.data.autumn.web.WebModule;
import org.slf4j.Logger;

import java.util.Properties;

import static com.google.inject.Scopes.SINGLETON;
import static com.google.inject.name.Names.named;
import static java.lang.Integer.valueOf;
import static com.intuit.data.autumn.utils.PropertyFactory.create;
import static com.intuit.data.autumn.utils.PropertyFactory.getProperty;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * An injector that includes application content provisioning implementation dependencies, e.g.: html, css, js
 */

public class ViewModule extends AbstractModule {

    public static final String PROPERTY_NAME = "/view.properties";
    private static final Logger LOGGER = getLogger(ViewModule.class);

    /**
     * Install module dependencies.
     */

    @Override
    protected void configure() {
        install(new WebModule());

        LOGGER.debug("binding properties: {}", "null");

        Properties properties = create(PROPERTY_NAME, ViewModule.class);

        bind(Integer.class).annotatedWith(named("resource.expiryTimeInSeconds"))
                .toInstance(valueOf(getProperty("resource.expiryTimeInSeconds", properties)));

        bind(Resource.class).in(SINGLETON);

        LOGGER.debug("bound properties: {}", "null");
    }
}
