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
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;

import java.net.URI;

import static com.codahale.metrics.MetricFilter.ALL;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A lifecycle provider for a Graphite operational metrics reporter.
 */

public class GraphiteMetricsService extends AbstractIdleService {

    private static final Logger LOGGER = getLogger(GraphiteMetricsService.class);
    private final URI service;
    private final String prefix;
    private final int interval;
    private final MetricRegistry metricRegistry;
    private GraphiteReporter reporter;

    /**
     * Graphite client java-metrics constructor with configurable state.
     *
     * @param service graphite service endpoint
     * @param prefix graphite datagram prefix
     * @param interval the frequency with which to emit graphite metrics
     * @param metricRegistry java-metrics registry
     */

    @Inject
    public GraphiteMetricsService(@Named("metrics.graphite.service") final URI service,
                                  @Named("metrics.graphite.service.prefix") final String prefix,
                                  @Named("metrics.graphite.interval.seconds") final int interval,
                                  final MetricRegistry metricRegistry) {
        LOGGER.debug("instantiating {}, service: {}, prefix: {}, interval: {}", serviceName(), service, prefix,
                interval);

        this.service = service;
        this.prefix = prefix;
        this.interval = interval;
        this.metricRegistry = metricRegistry;

        LOGGER.debug("instantiated {}, service: {}, prefix: {}, interval: {}", serviceName(), service, prefix,
                interval);
    }

    /**
     * Instance lifecycle start hook.
     *
     * @throws Exception unintended exception
     */

    @Override
    protected void startUp() throws Exception {
        if (reporter != null) {
            LOGGER.info("already started {}, service: {}, prefix: {}, interval: {}", serviceName(), service, prefix,
                    interval);

            return;
        }

        LOGGER.info("starting {}, service: {}, prefix: {}, interval: {}", serviceName(), service, prefix, interval);

        Graphite graphite = new Graphite(service.getHost(), service.getPort());

        reporter = GraphiteReporter.forRegistry(metricRegistry)
                .prefixedWith(prefix)
                .convertRatesTo(SECONDS)
                .convertDurationsTo(MILLISECONDS)
                .filter(ALL)
                .build(graphite);

        reporter.start(interval, SECONDS);

        LOGGER.info("started {}, service: {}, prefix: {}, interval: {}", serviceName(), service, prefix, interval);
    }

    /**
     * Instance lifecycle stop hook.
     *
     * @throws Exception unintended exception
     */

    @Override
    protected void shutDown() throws Exception {
        if (reporter == null) {
            LOGGER.info("already stopped {}, service: {}, prefix: {}, interval: {}", serviceName(), service, prefix,
                    interval);

            return;
        }

        LOGGER.info("stopping {}, service: {}, prefix: {}, interval: {}", serviceName(), service, prefix, interval);

        reporter.stop();
        reporter = null;

        LOGGER.info("stopped {}, service: {}, prefix: {}, interval: {}", serviceName(), service, prefix, interval);
    }
}
