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

package com.intuit.data.autumn.web;

import com.google.common.util.concurrent.Service;
import org.easymock.IAnswer;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.intuit.data.autumn.utils.PropertyFactory;
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
public class WebServicesTest {

    @Test
    public void getEnabledWebServiceHttpTrue() throws Exception {
        test("/web-http-enabled.properties", HttpService.class);
    }

    private void test(final String testProperties, final Class<? extends Service>... services) throws Exception {
        final Properties properties = new Properties();

        properties.load(WebServicesTest.class.getResourceAsStream(testProperties));

        mockStatic(PropertyFactory.class);
        expect(PropertyFactory.create(WebServices.PROPERTY_NAME, WebModule.class)).andReturn(properties);
        expect(PropertyFactory.getProperty(anyString(), eq(properties))).andAnswer(new IAnswer<String>() {
            @Override
            public String answer() throws Throwable {
                return properties.getProperty((String) getCurrentArguments()[0]);
            }
        }).anyTimes();
        replay(PropertyFactory.class);

        Set<Class<? extends Service>> enabledMetricsServices = WebServices.getEnabledWebServices();

        PowerMock.verify(PropertyFactory.class);

        assertThat(enabledMetricsServices.size(), is(services != null ? services.length : 0));
        assertThat(enabledMetricsServices, hasItems(services));
    }

    @Test
    public void getEnableWebServiceHttpsTrue() throws Exception {
        test("/web-https-enabled.properties", HttpsService.class);
    }

    @Test
    public void getEnabledWebServicesAllTrue() throws Exception {
        test("/web-all-enabled.properties", HttpService.class, HttpsService.class);
    }

    @Test
    public void getEnabledMetricsServicesNoneTrue() throws Exception {
        test("/web-none-enabled.properties");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void privateConstructor() throws Exception {
        Constructor<WebServices> webServicesConstructor = WebServices.class.getDeclaredConstructor();

        webServicesConstructor.setAccessible(true);

        webServicesConstructor.newInstance();
    }
}