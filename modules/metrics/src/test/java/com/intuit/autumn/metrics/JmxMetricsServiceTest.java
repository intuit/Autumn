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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.inject.Guice.createInjector;

@RunWith(MockitoJUnitRunner.class)
public class JmxMetricsServiceTest {

    @Mock
    private MetricRegistry metricRegistry;
    private JmxMetricsService jmxMetricsService;

    @Before
    public void before() {
        jmxMetricsService = createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(MetricRegistry.class).toInstance(metricRegistry);
            }
        }).getInstance(JmxMetricsService.class);
    }

    @After
    public void after() throws Exception {
        jmxMetricsService.shutDown();
    }

    @Test
    public void startUp() throws Exception {
        jmxMetricsService.startUp();
    }

    @Test
    public void startUpAlreadyStarted() throws Exception {
        jmxMetricsService.startUp();
        jmxMetricsService.startUp();
    }

    @Test
    public void shutDown() throws Exception {
        jmxMetricsService.shutDown();
    }

    @Test
    public void shutDownAlreadyShutDown() throws Exception {
        jmxMetricsService.shutDown();
        jmxMetricsService.shutDown();
    }
}
