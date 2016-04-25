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

import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.intuit.data.autumn.utils.PropertyFactory.create;
import static com.intuit.data.autumn.utils.PropertyFactory.getProperty;
import static com.intuit.data.autumn.web.WebModule.PROPERTY_NAME;
import static com.sun.jersey.api.core.PackagesResourceConfig.PROPERTY_PACKAGES;
import static com.sun.jersey.api.core.ResourceConfig.*;
import static com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trimToNull;

/**
 * An injector that includes HTTP/S binding implementation dependencies, e.g.: servlet, guice
 */

public class WebServletModule extends ServletModule {

    private ImmutableMap<String, KeyValue<String>> keys = ImmutableMap.<String, KeyValue<String>>builder()
            .put("application.jersey.provider.paths", new KeyValue<>(PROPERTY_PACKAGES, ""))
            .put("application.jersey.pojo.enabled", new KeyValue<>(FEATURE_POJO_MAPPING, TRUE.toString()))
            .put("application.jersey.request.filters", new KeyValue<>(PROPERTY_CONTAINER_REQUEST_FILTERS, ""))
            .put("application.jersey.response.filters", new KeyValue<>(PROPERTY_CONTAINER_RESPONSE_FILTERS, ""))
            .put("application.jersey.wadl.enabled", new KeyValue<>(FEATURE_DISABLE_WADL, TRUE.toString()))
            .build();

    /**
     * Inject module dependencies and bind guice filter delegates.
     */

    @Override
    protected void configureServlets() {
        bind(WebFilter.class).in(Singleton.class);
        bind(GuiceContainer.class);

        Map<String, String> params = new HashMap<>();
        Properties properties = create(PROPERTY_NAME, WebServletModule.class);

        for (Map.Entry<String, KeyValue<String>> entry : keys.entrySet()) {
            String key = entry.getKey();
            String jerseyProperty = trimToNull(getProperty(key, properties, entry.getValue().getValue()));

            if (isNotBlank(jerseyProperty)) {
                params.put(entry.getValue().getKey(), jerseyProperty);
            }
        }

        filter("/*").through(WebFilter.class);
        serve("/*").with(GuiceContainer.class, params);
    }

    private class KeyValue<T> {
        private T key;
        private T value;

        KeyValue(final T key, final T value) {
            this.key = key;
            this.value = value;
        }

        T getKey() {
            return key;
        }

        T getValue() {
            return value;
        }
    }
}