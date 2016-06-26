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

package com.intuit.autumn.metrics;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.intuit.autumn.manage.ManageModule;
import org.slf4j.Logger;

import java.io.File;
import java.net.URI;
import java.util.Properties;

import static com.google.inject.Scopes.SINGLETON;
import static com.google.inject.name.Names.named;
import static com.intuit.autumn.utils.PropertyFactory.create;
import static com.intuit.autumn.utils.PropertyFactory.getProperty;
import static java.lang.Integer.valueOf;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * An injector that includes operational metrics implemementation dependencies.
 */

public class MetricsModule extends AbstractModule {

    public static final String PROPERTY_NAME = "/metrics.properties";
    private static final Logger LOGGER = getLogger(MetricsModule.class);

    /**
     * Install module dependencies.
     */

    @Override
    protected void configure() {
        install(new ManageModule());

        LOGGER.debug("binding properties: {}", PROPERTY_NAME);

        Properties properties = create(PROPERTY_NAME, MetricsModule.class);

        bind(Boolean.class).annotatedWith(named("metrics.csv.enabled"))
                .toInstance(Boolean.valueOf(getProperty("metrics.csv.enabled", properties)));
        bind(File.class).annotatedWith(named("metrics.csv.directory"))
                .toInstance(new File(getProperty("metrics.csv.directory", properties)));
        bind(Integer.class).annotatedWith(named("metrics.csv.interval.seconds"))
                .toInstance(valueOf(getProperty("metrics.csv.interval.seconds", properties)));
        bind(Boolean.class).annotatedWith(named("metrics.graphite.enabled"))
                .toInstance(Boolean.valueOf(getProperty("metrics.graphite.enabled", properties)));
        bind(URI.class).annotatedWith(named("metrics.graphite.service"))
                .toInstance(URI.create(getProperty("metrics.graphite.service", properties)));
        bind(String.class).annotatedWith(named("metrics.graphite.service.prefix"))
                .toInstance(getProperty("metrics.graphite.service.prefix", properties));
        bind(Integer.class).annotatedWith(named("metrics.graphite.interval.seconds"))
                .toInstance(valueOf(getProperty("metrics.graphite.interval.seconds", properties)));
        bind(Boolean.class).annotatedWith(named("metrics.hystrix.enabled"))
                .toInstance(Boolean.valueOf(getProperty("metrics.hystrix.enabled", properties)));
        bind(Boolean.class).annotatedWith(named("metrics.jmx.enabled"))
                .toInstance(Boolean.valueOf(getProperty("metrics.jmx.enabled", properties)));

        bind(MetricRegistry.class).in(SINGLETON);
        bind(CsvMetricsService.class).in(SINGLETON);
        bind(GraphiteMetricsService.class).in(SINGLETON);
        bind(HystrixMetricsService.class).in(SINGLETON);
        bind(JmxMetricsService.class).in(SINGLETON);

        LOGGER.debug("bound properties: {}", PROPERTY_NAME);
    }
}
