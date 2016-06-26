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

import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;

import java.io.File;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.Locale.US;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.io.FileUtils.moveDirectory;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A lifecycle provider for a CVS operational metrics reporter.
 */

public class CsvMetricsService extends AbstractIdleService {

    private static final Logger LOGGER = getLogger(CsvMetricsService.class);
    private final File directory;
    private final int interval;
    private final MetricRegistry metricRegistry;
    private CsvReporter reporter;

    /**
     * CVS java-metrics reporter constructor with configurable state.
     *
     * @param directory      local directory with which persist the CVS report
     * @param interval       the frequency with which to persist the CVS report
     * @param metricRegistry java-metrics registry
     */

    @Inject
    public CsvMetricsService(@Named("metrics.csv.directory") final File directory,
                             @Named("metrics.csv.interval.seconds") final int interval,
                             final MetricRegistry metricRegistry) {
        LOGGER.debug("instantiating {}, directory: {}, interval: {}", serviceName(), directory, interval);

        this.directory = directory;
        this.interval = interval;
        this.metricRegistry = metricRegistry;

        LOGGER.debug("instantiated {}, directory: {}, interval: {}", serviceName(), directory, interval);
    }

    /**
     * Instance lifecycle start hook.
     *
     * @throws Exception unintended exception
     */

    @Override
    protected void startUp() throws Exception {
        if (reporter != null) {
            LOGGER.info("already started {}, directory: {}, interval: {}", serviceName(), directory, interval);

            return;
        }

        LOGGER.info("starting {}, directory: {}, interval: {}", serviceName(), directory, interval);

        if (directory.exists()) {
            moveDirectory(directory, new File(directory.getAbsolutePath() + "." + currentTimeMillis()));
        }

        if (directory.mkdirs()) {
            reporter = CsvReporter.forRegistry(metricRegistry)
                    .formatFor(US)
                    .convertRatesTo(SECONDS)
                    .convertDurationsTo(MILLISECONDS)
                    .build(directory);

            reporter.start(interval, SECONDS);
            LOGGER.info("started {}, directory: {}, interval: {}", serviceName(), directory, interval);
        } else {
            LOGGER.error("Can't create directory: {}", directory);

            throw new MetricsException(format("unable to make directory: %s", directory));
        }
    }

    /**
     * Instance lifecycle stop hook.
     *
     * @throws Exception unintended exception
     */

    @Override
    protected void shutDown() throws Exception {
        if (reporter == null) {
            LOGGER.info("already stopped {}", serviceName());

            return;
        }

        LOGGER.info("stopping {}", serviceName());

        reporter.stop();
        reporter = null;

        LOGGER.info("stopped {}", serviceName());
    }
}
