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
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.netflix.hystrix.contrib.codahalemetricspublisher.HystrixCodaHaleMetricsPublisher;
import org.slf4j.Logger;

import static com.netflix.hystrix.strategy.HystrixPlugins.getInstance;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A lifecycle provider for a Hystrix to Graphite operational metrics reporter.
 */

public class HystrixMetricsService extends AbstractIdleService {

    private static final Logger LOGGER = getLogger(HystrixMetricsService.class);
    private final MetricRegistry metricRegistry;
    private HystrixCodaHaleMetricsPublisher hystrixCodaHaleMetricsPublisher;

    /**
     * Hystrix client java-metrics constructor with configurable state.
     *
     * @param metricRegistry java-metrics registry
     */

    @Inject
    public HystrixMetricsService(final MetricRegistry metricRegistry) {
        LOGGER.debug("instantiating {}", serviceName());

        this.metricRegistry = metricRegistry;

        LOGGER.debug("instantiated {}", serviceName());
    }

    /**
     * Instance lifecycle start hook.
     *
     * @throws Exception unintended exception
     */

    @Override
    protected void startUp() throws Exception {
        if (hystrixCodaHaleMetricsPublisher != null) {
            LOGGER.info("already started {}", serviceName());

            return;
        }

        LOGGER.info("starting {}", serviceName());

        hystrixCodaHaleMetricsPublisher = new HystrixCodaHaleMetricsPublisher(metricRegistry);

        getInstance().registerMetricsPublisher(hystrixCodaHaleMetricsPublisher);

        LOGGER.info("started {}", serviceName());
    }

    /**
     * Instance lifecycle stop hook.
     *
     * @throws Exception
     */

    @Override
    protected void shutDown() throws Exception {
        if (hystrixCodaHaleMetricsPublisher == null) {
            LOGGER.info("already stopped {}", serviceName());

            return;
        }

        LOGGER.info("stopping {}", serviceName());

        // todo: ?deregister?
        // HystrixPlugins.getInstance()...
        hystrixCodaHaleMetricsPublisher = null;

        LOGGER.info("stopped {}", serviceName());
    }
}
