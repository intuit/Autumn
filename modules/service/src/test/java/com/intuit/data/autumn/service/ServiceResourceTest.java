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

package com.intuit.data.autumn.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class ServiceResourceTest {

    private ServiceResource<String> serviceResource;

    @Before
    public void before() {
        serviceResource = new ServiceResource<String>();
    }

    @Test
    public void addResource() throws Exception {
        serviceResource.addResources("foo");

        Collection<String> resources = serviceResource.getResources();

        assertThat(resources.size(), is(1));
        assertThat(resources, hasItem("foo"));
    }

    @Test
    public void addResourceCollection() throws Exception {
        serviceResource.addResources(asList("foo"));

        Collection<String> resources = serviceResource.getResources();

        assertThat(resources.size(), is(1));
        assertThat(resources, hasItem("foo"));
    }

    @Test
    public void addEmptyResource() throws Exception {
        serviceResource.addResources();

        assertThat(serviceResource.getResources(), is(empty()));
    }

    @Test
    public void addNoResource() throws Exception {
        serviceResource.addResources();

        assertThat(serviceResource.getResources(), is(empty()));
    }

    @Test
    public void addNullResource() throws Exception {
        serviceResource.addResources((String) null);

        assertThat(serviceResource.getResources(), is(empty()));
    }

    @Test
    public void addResources() throws Exception {
        serviceResource.addResources("foo", "bar");

        Collection<String> resources = serviceResource.getResources();

        assertThat(resources.size(), is(2));
        assertThat(serviceResource.getResources(), hasItems("foo", "bar"));
    }

    @Test
    public void addResourcesAsCollection() throws Exception {
        serviceResource.addResources(asList("foo", "bar"));

        Collection<String> resources = serviceResource.getResources();

        assertThat(resources.size(), is(2));
        assertThat(serviceResource.getResources(), hasItems("foo", "bar"));
    }

    @Test
    public void addNullResourceCollection() throws Exception {
        serviceResource.addResources((Collection<String>) null);

        assertThat(serviceResource.getResources(), is(empty()));
    }
}
