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

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.intuit.data.autumn.utils.PropertyFactory;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Properties;

import static com.google.inject.Guice.createInjector;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static com.intuit.data.autumn.web.WebModule.PROPERTY_NAME;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PropertyFactory.class)
public class WebServletModuleTest {

    @Test
    public void injectable() throws Exception {
        Properties properties = new Properties();

        properties.load(WebServletModule.class.getResourceAsStream(PROPERTY_NAME));

//        mockStatic(PropertyFactory.class);
//        expect(PropertyFactory.create(PROPERTY_NAME, WebServletModule.class)).andReturn(properties);
//        expect(PropertyFactory.getProperty(anyString(), properties, anyString())).andReturn("foo");
//        replay(PropertyFactory.class);

        Injector injector = createInjector(new WebServletModule());

        assertThat(injector.getInstance(GuiceFilter.class), notNullValue());
        assertThat(injector.getInstance(WebFilter.class), notNullValue());
        assertThat(injector.getInstance(GuiceContainer.class), notNullValue());
    }

    @Test
    public void injectableWithNoProviderPath() throws Exception {
        Properties properties = new Properties();

        properties.load(WebServletModule.class.getResourceAsStream(PROPERTY_NAME));

//        mockStatic(PropertyFactory.class);
//        expect(PropertyFactory.create(PROPERTY_NAME, WebServletModule.class)).andReturn(properties);
//        expect(PropertyFactory.getProperty(anyString(), properties, anyString())).andReturn(null);
//        replay(PropertyFactory.class);

        Injector injector = createInjector(new WebServletModule());

        assertThat(injector.getInstance(GuiceFilter.class), notNullValue());
        assertThat(injector.getInstance(WebFilter.class), notNullValue());
        assertThat(injector.getInstance(GuiceContainer.class), notNullValue());
    }
}
