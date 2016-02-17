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

import com.google.inject.Injector;
import com.google.inject.Key;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Properties;

import static com.google.common.primitives.Ints.tryParse;
import static com.google.inject.Guice.createInjector;
import static com.google.inject.name.Names.named;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static com.intuit.data.autumn.metrics.MetricsModule.PROPERTY_NAME;
import static com.intuit.data.autumn.utils.PropertyFactory.create;

public class MetricsModuleTest {

    @Test
    public void injector() throws Exception {
        Injector injector = createInjector(new MetricsModule());
        Properties properties = create(PROPERTY_NAME);

        assertThat(injector.getInstance(new Key<File>(named("metrics.csv.directory")) {
        }), equalTo(new File(properties.getProperty("metrics.csv.directory"))));
        assertThat(injector.getInstance(new Key<Integer>(named("metrics.csv.interval.seconds")) {
        }), equalTo(tryParse(properties.getProperty("metrics.csv.interval.seconds"))));
        assertThat(injector.getInstance(new Key<URI>(named("metrics.graphite.service")) {
        }), equalTo(URI.create(properties.getProperty("metrics.graphite.service"))));
        assertThat(injector.getInstance(new Key<String>(named("metrics.graphite.service.prefix")) {
        }), equalTo(properties.getProperty("metrics.graphite.service.prefix")));
        assertThat(injector.getInstance(new Key<Integer>(named("metrics.graphite.interval.seconds")) {
        }), equalTo(tryParse(properties.getProperty("metrics.graphite.interval.seconds"))));

        injector.getInstance(CsvMetricsService.class);
        injector.getInstance(HystrixMetricsService.class);
        injector.getInstance(GraphiteMetricsService.class);
        injector.getInstance(JmxMetricsService.class);
    }
}
