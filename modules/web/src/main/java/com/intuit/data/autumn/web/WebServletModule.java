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

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.sun.jersey.api.core.PackagesResourceConfig.PROPERTY_PACKAGES;
import static com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static com.intuit.data.autumn.utils.PropertyFactory.create;
import static com.intuit.data.autumn.utils.PropertyFactory.getProperty;

/**
 * An injector that includes HTTP/S binding implementation dependencies, e.g.: servlet, guice
 */

public class WebServletModule extends ServletModule {

    /**
     * Inject module dependencies and bind guice filter delegates.
     */

    @Override
    protected void configureServlets() {
        bind(WebFilter.class).in(Singleton.class);
        bind(GuiceContainer.class);

        Map<String, String> params = new HashMap<>();
        Properties properties = create(WebModule.PROPERTY_NAME, WebServletModule.class);
        String providerPath = getProperty("application.jersey.provider.path", properties);

        if (isNotEmpty(providerPath)) {
            params.put(PROPERTY_PACKAGES, providerPath);
        }

        params.put(FEATURE_POJO_MAPPING, "true");

        filter("/*").through(WebFilter.class);
        serve("/*").with(GuiceContainer.class, params);
    }
}
