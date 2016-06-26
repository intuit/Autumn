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

import com.google.common.util.concurrent.Service;
import com.intuit.autumn.utils.PropertyFactory;
import org.easymock.IAnswer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Constructor;
import java.util.Properties;
import java.util.Set;

import static org.easymock.EasyMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PropertyFactory.class)
public class MetricsServicesTest {

    @Test
    public void getEnabledMetricsServiceCsvTrue() throws Exception {
        test("/metrics-cvs-enabled.properties", CsvMetricsService.class);
    }

    private void test(final String testProperties, final Class<? extends Service>... services) throws Exception {
        final Properties properties = new Properties();

        properties.load(MetricsServicesTest.class.getResourceAsStream(testProperties));

        mockStatic(PropertyFactory.class);
        expect(PropertyFactory.create(MetricsModule.PROPERTY_NAME, MetricsModule.class)).andReturn(properties);
        expect(PropertyFactory.getProperty(anyString(), eq(properties))).andAnswer(new IAnswer<String>() {
            @Override
            public String answer() throws Throwable {
                return properties.getProperty((String) getCurrentArguments()[0]);
            }
        }).anyTimes();
        replay(PropertyFactory.class);

        Set<Class<? extends Service>> enabledMetricsServices = MetricsServices.getEnabledMetricsServices();

        PowerMock.verify(PropertyFactory.class);

        assertThat(enabledMetricsServices.size(), is(services != null ? services.length : 0));
        assertThat(enabledMetricsServices, hasItems(services));
    }

    @Test
    public void getEnabledMetricsServiceGraphiteTrue() throws Exception {
        test("/metrics-graphite-enabled.properties", GraphiteMetricsService.class);
    }

    @Test
    public void getEnabledMetricsServiceHystrixTrue() throws Exception {
        test("/metrics-hystrix-enabled.properties", HystrixMetricsService.class, GraphiteMetricsService.class);
    }

    @Test
    public void getEnabledMetricsServiceJmxTrue() throws Exception {
        test("/metrics-jmx-enabled.properties", JmxMetricsService.class);
    }

    @Test
    public void getEnabledMetricsServicesAllTrue() throws Exception {
        test("/metrics-all-enabled.properties", CsvMetricsService.class, GraphiteMetricsService.class,
                HystrixMetricsService.class, JmxMetricsService.class);
    }

    @Test
    public void getEnabledMetricsServicesNoneTrue() throws Exception {
        test("/metrics-none-enabled.properties");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void privateConstructor() throws Exception {
        Constructor<MetricsServices> metricsServicesConstructor = MetricsServices.class.getDeclaredConstructor();

        metricsServicesConstructor.setAccessible(true);

        metricsServicesConstructor.newInstance();
    }
}