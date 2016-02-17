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

package com.intuit.data.autumn.manage;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.rmi.RemoteException;
import static com.google.inject.Scopes.SINGLETON;
import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;
import static java.rmi.registry.LocateRegistry.createRegistry;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * An injector that includes management implementation dependencies, e.g.: jmx
 */

public class ManageModule extends AbstractModule {

    private static final Logger LOGGER = getLogger(ManageModule.class);

    // note: configure the jmx ephemeral port, needs to be done before the jmx subsystem is instantiated
    static {
        // todo: ?should we provide a means to set the back-channel port, vs one-up the public port?
        String portStr = getProperty("com.sun.management.jmxremote.port");
        int port = (portStr != null) ? parseInt(portStr) + 1 : 18099;

        try {
            createRegistry(port);
        } catch (RemoteException e) {
            LOGGER.warn("unable to create registry on port: {}, cause: {}", port, e.getMessage());

            throw new ManageException(e);
        }
    }

    /**
     * Install module dependencies.
     */

    @Override
    protected void configure() {
        bind(MBeanServer.class).toInstance(ManagementFactory.getPlatformMBeanServer());
        bind(MBeanRegistry.class).in(SINGLETON);
    }
}
