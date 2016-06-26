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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.util.concurrent.Service;
import org.slf4j.Logger;

import java.util.Properties;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static com.intuit.autumn.utils.PropertyFactory.create;
import static com.intuit.autumn.utils.PropertyFactory.getProperty;
import static org.apache.commons.lang.BooleanUtils.toBoolean;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A provider for enabled web services.
 */

public class WebServices {

    public static final String PROPERTY_NAME = "/web.properties";
    private static final ImmutableMap<String, Class<? extends Service>> WEB_SERVICES =
            new Builder<String, Class<? extends Service>>()
                    .put("application.http.enabled", HttpService.class)
                    .put("application.https.enabled", HttpsService.class)
                    .build();
    private static final Logger LOGGER = getLogger(WebServices.class);

    private WebServices() {
        throw new UnsupportedOperationException();
    }

    /**
     * Http services configuration utility.
     *
     * @return collection of enabled http services.
     */

    public static Set<Class<? extends Service>> getEnabledWebServices() {
        LOGGER.debug("getting enabled web services");

        Properties properties = create(PROPERTY_NAME, WebModule.class);
        Set<Class<? extends Service>> webServices = newHashSet();

        loadIfEnabled(properties, webServices, "application.http.enabled");
        loadIfEnabled(properties, webServices, "application.https.enabled");

        LOGGER.debug("got enabled web services count: {}", webServices.size());

        return webServices;
    }

    private static void loadIfEnabled(final Properties properties, Set<Class<? extends Service>> services,
                                      final String key) {
        if (toBoolean(getProperty(key, properties))) {
            services.add(WEB_SERVICES.get(key));
        }
    }
}
