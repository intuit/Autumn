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
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.name.Names.named;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CsvMetricsServiceTest {

    @Mock
    private File directory;
    private Integer interval = 10;
    @Mock
    private MetricRegistry metricRegistry;
    private CsvMetricsService csvMetricsService;

    @Test
    public void startUp() throws Exception {
        TemporaryFolder temporaryFolder = new TemporaryFolder();

        temporaryFolder.create();

        directory = temporaryFolder.getRoot();
        csvMetricsService = createService();

        // todo: test the service started

        temporaryFolder.delete();
    }

    private CsvMetricsService createService() {
        return createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(File.class).annotatedWith(named("metrics.csv.directory")).toInstance(directory);
                bind(Integer.class).annotatedWith(named("metrics.csv.interval.seconds")).toInstance(interval);
                bind(MetricRegistry.class).toInstance(metricRegistry);
            }
        }).getInstance(CsvMetricsService.class);
    }

    @Test
    public void startUpDirectoryDoesNotExist() throws Exception {
        when(directory.exists()).thenReturn(false);
        when(directory.mkdirs()).thenReturn(true);

        csvMetricsService = createService();
        csvMetricsService.startUp();

        // todo: test the service started
    }

    @Test
    public void startUpDirectoryExists() throws Exception {
        TemporaryFolder temporaryFolder = new TemporaryFolder();

        temporaryFolder.create();

        directory = temporaryFolder.getRoot();
        csvMetricsService = createService();

        csvMetricsService.startUp();

        // todo: test the service started

        temporaryFolder.delete();
    }

    @Test(expected = MetricsException.class)
    public void startUpDirectoryDoesNotExistCanNotMkdir() throws Exception {
        when(directory.exists()).thenReturn(false);
        when(directory.mkdirs()).thenReturn(false);

        csvMetricsService = createService();

        csvMetricsService.startUp();
    }

    @Test
    public void startUpAlreadyStarted() throws Exception {
        when(directory.exists()).thenReturn(false);
        when(directory.mkdirs()).thenReturn(true);

        csvMetricsService = createService();

        csvMetricsService.startUp();

        // todo: test the service started

        csvMetricsService.startUp();
    }

    @Test
    public void shutdown() throws Exception {
        when(directory.exists()).thenReturn(false);
        when(directory.mkdirs()).thenReturn(true);

        csvMetricsService = createService();

        csvMetricsService.startUp();

        // todo: test the service started

        csvMetricsService.shutDown();
    }

    @Test
    public void shutDownAlreadyShutDown() throws Exception {
        when(directory.exists()).thenReturn(false);
        when(directory.mkdirs()).thenReturn(true);

        csvMetricsService = createService();

        csvMetricsService.shutDown();
        csvMetricsService.shutDown();
    }
}
