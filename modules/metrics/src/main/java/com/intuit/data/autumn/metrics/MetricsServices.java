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

package com.intuit.data.autumn.metrics;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.util.concurrent.Service;
import org.slf4j.Logger;

import java.util.Properties;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.lang.BooleanUtils.toBoolean;
import static com.intuit.data.autumn.utils.PropertyFactory.create;
import static com.intuit.data.autumn.utils.PropertyFactory.getProperty;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A utility providing a list of enabled operational metrics reporters.
 */

public class MetricsServices {

    public static final String PROPERTY_NAME = "/metrics.properties";
    private static final String METRICS_GRAPHITE_ENABLED_KEY = "metrics.graphite.enabled";
    private static final ImmutableMap<String, Class<? extends Service>> METRICS_SERVICES =
            new Builder<String, Class<? extends Service>>()
                    .put("metrics.csv.enabled", CsvMetricsService.class)
                    .put(METRICS_GRAPHITE_ENABLED_KEY, GraphiteMetricsService.class)
                    .put("metrics.hystrix.enabled", HystrixMetricsService.class)
                    .put("metrics.jmx.enabled", JmxMetricsService.class)
                    .build();
    private static final Logger LOGGER = getLogger(MetricsServices.class);

    private MetricsServices() {
        throw new UnsupportedOperationException();
    }

    /**
     * Metrics services configuration utility.
     *
     * @return collection of enabled metrics services.
     */

    // todo: ?do better?
    public static Set<Class<? extends Service>> getEnabledMetricsServices() {
        LOGGER.debug("getting enabled metrics services");

        Properties properties = create(PROPERTY_NAME, MetricsModule.class);
        Set<Class<? extends Service>> metricsServices = newHashSet();

        addIfEnabled(properties, metricsServices, "metrics.csv.enabled");
        addIfEnabled(properties, metricsServices, METRICS_GRAPHITE_ENABLED_KEY);
        // note: graphite is needed for hystrix ( don't be alarmed )
        addIfEnabled(properties, metricsServices, "metrics.hystrix.enabled", METRICS_GRAPHITE_ENABLED_KEY);
        addIfEnabled(properties, metricsServices, "metrics.jmx.enabled");

        LOGGER.debug("got enabled metrics services count: {}", metricsServices.size());

        return metricsServices;
    }

    private static void addIfEnabled(final Properties properties, Set<Class<? extends Service>> services,
                                     final String... keys) {
        int keyCounter = 0;
        boolean isEnabled = false;

        for (String key : keys) {
            if (++keyCounter == 1) {
                isEnabled = toBoolean(getProperty(key, properties));
            }

            if (isEnabled) {
                services.add(METRICS_SERVICES.get(key));
            }
        }
    }
}
