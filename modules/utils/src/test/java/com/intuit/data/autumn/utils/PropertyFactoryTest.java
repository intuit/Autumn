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

package com.intuit.data.autumn.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PropertyFactoryTest {

    @Before
    public void before() {
        // clear all properties set
        System.clearProperty("key1");
    }

    @After
    public void after() {
        // clear all properties set
        System.clearProperty("key1");
    }


    @Test
    public void properties() throws Exception {
        Properties properties = PropertyFactory.create("/test.properties");

        assertThat(properties.size(), is(2));
        assertThat(properties.keySet(), hasItems((Object) "key1", "key2"));
        assertThat(properties.get("key1"), is((Object) "value1"));
        assertThat(properties.get("key2"), is((Object) "value2"));
    }

    @Test
    public void getProperty() throws Exception {
        Properties properties = PropertyFactory.create("/test.properties");

        assertThat(PropertyFactory.getProperty("key1", properties), is("value1"));
        assertThat(PropertyFactory.getProperty("key2", properties), is("value2"));
        assertThat(PropertyFactory.getProperty("key3", properties), is(nullValue()));
    }

    @Test
    public void getPropertyWithDefault() throws Exception {
        Properties properties = PropertyFactory.create("/test.properties");

        assertThat(PropertyFactory.getProperty("key1", properties, "value3"), is("value1"));
        assertThat(PropertyFactory.getProperty("key2", properties, "value3"), is("value2"));
        assertThat(PropertyFactory.getProperty("key3", properties, "value3"), is("value3"));
    }

    @Test
    public void getJavaPropertyWithDefault() throws Exception {
        System.setProperty("key1", "javaValue1");
        Properties properties = PropertyFactory.create("/test.properties");

        assertThat(PropertyFactory.getProperty("key1", properties, "value3"), is("javaValue1"));
        assertThat(PropertyFactory.getProperty("key2", properties, "value3"), is("value2"));
        assertThat(PropertyFactory.getProperty("key3", properties, "value3"), is("value3"));
    }

    @Test(expected = InvocationTargetException.class)
    public void privateConstructor() throws Exception {
        Constructor<PropertyFactory> propertyFactoryConstructor = PropertyFactory.class.getDeclaredConstructor();

        propertyFactoryConstructor.setAccessible(true);

        propertyFactoryConstructor.newInstance();
    }

    //TODO: figure out how to test System.genenv

    // todo: can't think of a way to test a classpath override
}
