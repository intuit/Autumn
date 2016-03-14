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

import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.name.Names.named;

@RunWith(MockitoJUnitRunner.class)
public class GraphiteMetricsServiceTest {

    private URI service = URI.create("http://bar:123");
    private String prefix = "prefix";
    private Integer interval = 10;
    @Mock
    private MetricRegistry metricRegistry;
    private GraphiteMetricsService graphiteMetricsService;

    @Before
    public void before() {
        graphiteMetricsService = createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(URI.class).annotatedWith(named("metrics.graphite.service")).toInstance(service);
                bind(String.class).annotatedWith(named("metrics.graphite.service.prefix")).toInstance(prefix);
                bind(Integer.class).annotatedWith(named("metrics.graphite.interval.seconds")).toInstance(interval);
                bind(MetricRegistry.class).toInstance(metricRegistry);
            }
        }).getInstance(GraphiteMetricsService.class);
    }

    @After
    public void after() throws Exception {
        graphiteMetricsService.shutDown();
    }

    @Test
    public void startUp() throws Exception {
        graphiteMetricsService.startUp();
    }

    @Test
    public void startUpAlreadyStarted() throws Exception {
        graphiteMetricsService.startUp();
        graphiteMetricsService.startUp();
    }

    @Test
    public void shutDown() throws Exception {
        graphiteMetricsService.shutDown();
    }

    @Test
    public void shutDownAlreadyShutDown() throws Exception {
        graphiteMetricsService.shutDown();
        graphiteMetricsService.shutDown();
    }
}
