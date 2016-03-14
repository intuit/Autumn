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

import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.Service;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.powermock.api.support.membermodification.MemberMatcher.field;
import static org.powermock.reflect.Whitebox.getInternalState;

public class ServiceManagerTest {

    @Mock
    com.google.common.util.concurrent.ServiceManager internalServiceManager;
    private ServiceManager serviceManager;

    @Before
    public void before() {
        serviceManager = new ServiceManager();
    }

    @After
    public void after() {
        serviceManager.stop();
    }

    @Test
    public void addConfiguration() throws Exception {
        serviceManager.addConfiguration("foo");

        ServiceResource<String> configurations = getInternalState(serviceManager, "configurations");
        Collection<String> resources = configurations.getResources();

        assertThat(resources.size(), is(1));
        assertThat(resources, hasItem("foo"));
    }

    @Test
    public void addConfigurationsAsVargs() throws Exception {
        serviceManager.addConfiguration("foo", "bar");

        ServiceResource<String> configurations = getInternalState(serviceManager, "configurations");
        Collection<String> resources = configurations.getResources();

        assertThat(resources.size(), is(2));
        assertThat(resources, hasItems("foo", "bar"));
    }

    @Test
    public void addConfigurationsAsCollection() throws Exception {
        serviceManager.addConfiguration(asList("foo", "bar"));

        ServiceResource<String> configurations = getInternalState(serviceManager, "configurations");
        Collection<String> resources = configurations.getResources();

        assertThat(resources.size(), is(2));
        assertThat(resources, hasItems("foo", "bar"));
    }

    @Test
    public void addNullConfiguration() throws Exception {
        serviceManager.addConfiguration((String) null);

        ServiceResource<String> configurations = getInternalState(serviceManager, "configurations");
        Collection<String> resources = configurations.getResources();

        assertThat(resources, is(empty()));
    }

    @Test
    public void addEmptyConfiguration() throws Exception {
        serviceManager.addConfiguration();

        ServiceResource<String> configurations = getInternalState(serviceManager, "configurations");
        Collection<String> resources = configurations.getResources();

        assertThat(resources, is(empty()));
    }

    @Ignore
    @Test(expected = IllegalStateException.class)
    public void addConfigurationWhenStarted() throws Exception {
//        ServiceManager sm = new ServiceManager(TestService.class);
        field(ServiceManager.class, "serviceManager").set(serviceManager, internalServiceManager);

        serviceManager.addConfiguration("foo");
    }

    @Test
    public void addModule() throws Exception {
        serviceManager.addModules(TestModule.class);

        ServiceResource<Class<? extends Module>> modules = getInternalState(serviceManager, "modules");
        Collection<Class<? extends Module>> resources = modules.getResources();

        assertThat(resources.size(), is(1));
        assertThat(TestModule.class, isIn(resources));
    }

    @Test
    public void addModules() throws Exception {
        serviceManager.addModules(TestModule.class, AnotherTestModule.class);

        ServiceResource<Class<? extends Module>> modules = getInternalState(serviceManager, "modules");
        Collection<Class<? extends Module>> resources = modules.getResources();

        assertThat(resources.size(), is(2));
        assertThat(TestModule.class, isIn(resources));
        assertThat(AnotherTestModule.class, isIn(resources));
    }

    @Test
    public void addNullModule() throws Exception {
        serviceManager.addModules((Class<? extends Module>[]) null);

        ServiceResource<Class<? extends Module>> modules = getInternalState(serviceManager, "configurations");
        Collection<Class<? extends Module>> resources = modules.getResources();

        assertThat(resources, is(empty()));
    }

    @Test
    public void addEmptyModule() throws Exception {
        serviceManager.addModules();

        ServiceResource<Class<? extends Module>> modules = getInternalState(serviceManager, "configurations");
        Collection<Class<? extends Module>> resources = modules.getResources();

        assertThat(resources, is(empty()));
    }

    @Test
    public void addService() throws Exception {
        serviceManager.addServices(TestService.class);

        ServiceResource<Class<? extends Service>> service = getInternalState(serviceManager, "services");
        Collection<Class<? extends Service>> resources = service.getResources();

        assertThat(resources.size(), is(1));
        assertThat(TestService.class, isIn(resources));
    }

    @Test
    public void addServices() throws Exception {
        serviceManager.addServices(TestService.class, AnotherTestService.class);

        ServiceResource<Class<? extends Service>> service = getInternalState(serviceManager, "services");
        Collection<Class<? extends Service>> resources = service.getResources();

        assertThat(resources.size(), is(2));
        assertThat(TestService.class, isIn(resources));
        assertThat(AnotherTestService.class, isIn(resources));
    }

    @Test
    public void addNullService() throws Exception {
        serviceManager.addServices((Class<? extends Service>[]) null);

        ServiceResource<Class<? extends Service>> service = getInternalState(serviceManager, "services");
        Collection<Class<? extends Service>> resources = service.getResources();

        assertThat(resources, is(empty()));
    }

    @Test
    public void addEmptyService() throws Exception {
        serviceManager.addServices();

        ServiceResource<Class<? extends Service>> service = getInternalState(serviceManager, "services");
        Collection<Class<? extends Service>> resources = service.getResources();

        assertThat(resources, is(empty()));
    }

    @Test
    public void start() throws Exception {
        serviceManager.addModules(TestModule.class).addServices(TestService.class).start();
    }

    @Test
    public void stop() throws Exception {
        serviceManager.addModules(TestModule.class).addServices(TestService.class).start();
        serviceManager.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void startBeforeAddModule() throws Exception {
        serviceManager.start();
        serviceManager.addModules(TestModule.class);
    }

    @Test(expected = IllegalStateException.class)
    public void startBeforeAddService() throws Exception {
        serviceManager.start();
        serviceManager.addServices(TestService.class);
    }

    @Test(expected = IllegalStateException.class)
    public void startBeforeAddConfiguration() throws Exception {
        serviceManager.start();
        serviceManager.addConfiguration("foo");
    }

    @Test
    public void startWithAnAdditionalNullService() throws Exception {
        serviceManager.addServices(TestService.class, null).start();
    }

    static class TestService extends AbstractService {
        TestService() {
        }

        @Override
        protected void doStart() {
        }

        @Override
        protected void doStop() {
        }
    }

    class TestModule implements Module {
        @Inject
        TestModule() {
        }

        @Override
        public void configure(Binder binder) {
        }
    }

    private class AnotherTestModule extends TestModule {
    }

    private class AnotherTestService extends TestService {
    }
}