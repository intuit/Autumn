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

import com.google.common.util.concurrent.Service;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.netflix.governator.configuration.CompositeConfigurationProvider;
import com.netflix.governator.configuration.PropertiesConfigurationProvider;
import com.netflix.governator.configuration.SystemConfigurationProvider;
import com.netflix.governator.guice.BootstrapBinder;
import com.netflix.governator.guice.BootstrapModule;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.lifecycle.LifecycleManager;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static com.intuit.data.autumn.utils.PropertyFactory.create;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A lifecycle manager that allows for dependency instantiation and service instantiation.  All configuration must be
 * done before start lifecycle manager.
 */

public class ServiceManager {

    private static final String MESSAGE_ALREADY_STARTED = "application is already started";
    private static final Logger LOGGER = getLogger(ServiceManager.class);
    private final ServiceResource<String> configurations = new ServiceResource<>();
    private final ServiceResource<Class<? extends Module>> modules = new ServiceResource<>();
    private final ServiceResource<Class<? extends Service>> services = new ServiceResource<>();
    private LifecycleManager lifecycleManager;
    private com.google.common.util.concurrent.ServiceManager internalServiceManager;

    /**
     * Configurations adder.
     *
     * @param configurations configurations to be added
     * @return services instance
     */

    public final ServiceManager addConfiguration(@Nullable final String... configurations) {
        addConfiguration(configurations != null ? asList(configurations) : EMPTY_LIST);

        return this;
    }

    /**
     * Configuration collection adder.
     *
     * @param configuration configurations collection to be added
     * @return services instance
     */

    public final ServiceManager addConfiguration(final Collection<String> configuration) {
        if (internalServiceManager != null) {
            throw new IllegalStateException(MESSAGE_ALREADY_STARTED);
        }

        this.configurations.addResources(configuration);

        return this;
    }

    /**
     * Modules adder.
     *
     * @param modules modules to be added
     * @return services instance
     */

    @SafeVarargs
    public final ServiceManager addModules(final Class<? extends Module>... modules) {
        addModules(modules != null ? asList(modules) : EMPTY_LIST);

        return this;
    }

    /**
     * Modules collection adder.
     *
     * @param modules modules collection to be added
     * @return services instance
     */

    public final ServiceManager addModules(final Collection<Class<? extends Module>> modules) {
        if (internalServiceManager != null) {
            throw new IllegalStateException(MESSAGE_ALREADY_STARTED);
        }

        this.modules.addResources(modules);

        return this;
    }

    /**
     * Services adder.
     *
     * @param services services to be added
     * @return services instance
     */

    @SafeVarargs
    public final ServiceManager addServices(final Class<? extends Service>... services) {
        addServices(services != null ? asList(services) : EMPTY_LIST);

        return this;
    }

    /**
     * Services collection to be added.
     *
     * @param services serivces collection to be added
     * @return services instance
     */

    public final ServiceManager addServices(final Collection<Class<? extends Service>> services) {
        if (internalServiceManager != null) {
            throw new IllegalStateException(MESSAGE_ALREADY_STARTED);
        }

        this.services.addResources(services);

        return this;
    }

    /**
     * Start configured services.
     *
     * @throws ServiceManagerException unintended exception
     */

    public void start() throws ServiceManagerException {
        if (internalServiceManager != null) {
            LOGGER.debug(MESSAGE_ALREADY_STARTED);

            return;
        }

        LOGGER.debug("starting application");

        Injector injector = getInjector();
        Set<Service> servicesSet = newHashSet();

        for (Class<? extends Service> service : this.services.getResources()) {
            LOGGER.debug("instantiating service: " + service.getSimpleName());

            servicesSet.add(injector.getInstance(service));

            LOGGER.debug("instantiated service: " + service.getSimpleName());
        }

        internalServiceManager = new com.google.common.util.concurrent.ServiceManager(servicesSet);

        LOGGER.debug("starting {} services", servicesSet.size());

        internalServiceManager.startAsync();

        LOGGER.debug("started application");
    }

    private Injector getInjector() throws ServiceManagerException {
        Injector injector = LifecycleInjector
                .builder()
                .withBootstrapModule(new BootstrapModule() {
                    @Override
                    public void configure(BootstrapBinder binder) {
                        binder.bindConfigurationProvider().toInstance(new SystemConfigurationProvider());
                    }
                })
                .withAdditionalBootstrapModules(new BootstrapModule() {
                    @Override
                    public void configure(BootstrapBinder binder) {
                        CompositeConfigurationProvider compositeConfigurationProvider = new CompositeConfigurationProvider();

                        for (Properties property : getConfigurations()) {
                            compositeConfigurationProvider.add(new PropertiesConfigurationProvider(property));
                        }

                        binder.bindConfigurationProvider().toInstance(compositeConfigurationProvider);
                    }
                })
                .withModuleClasses(modules.getResources())
                .build()
                .createInjector();

        lifecycleManager = injector.getInstance(LifecycleManager.class);

        try {
            lifecycleManager.start();
        } catch (Exception e) {
            throw new ServiceManagerException("unable to start the lifecycle manager", e);
        }

        return injector;
    }

    private Set<Properties> getConfigurations() {
        Set<Properties> configs = newHashSet();

        for (String configurationResource : this.configurations.getResources()) {
            configs.add(create(configurationResource));
        }

        return configs;
    }

    /**
     * Stop configured services.
     */

    public void stop() {
        if (internalServiceManager == null) {
            LOGGER.debug("already stopped");

            return;
        }

        LOGGER.debug("stopping {} services", services.getResources().size());

        internalServiceManager.stopAsync();
        lifecycleManager.close();
        internalServiceManager = null;

        LOGGER.debug("stopped {} services", services.getResources().size());
    }
}
