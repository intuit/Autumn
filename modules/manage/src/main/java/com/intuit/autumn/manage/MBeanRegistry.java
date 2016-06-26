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

package com.intuit.autumn.manage;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import org.slf4j.Logger;

import javax.management.*;
import java.util.Hashtable;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A MessageBean object registration utility.
 */

public class MBeanRegistry {

    private static final Logger LOGGER = getLogger(MBeanRegistry.class);
    private final MBeanServer server;

    /**
     * Constructor with configurable state.
     *
     * @param server registry server
     */

    @Inject
    public MBeanRegistry(MBeanServer server) {
        this.server = server;
    }

    /**
     * MBean instance registration service.
     *
     * @param o    mbean instance
     * @param name mbean name
     */

    public void register(final Object o, final String name) {
        String packageName = o.getClass().getPackage().getName();
        ImmutableMap<String, String> attributes = new ImmutableMap.Builder<String, String>()
                .put("name", name)
                .put("type", o.getClass().getSimpleName()).build();

        try {
            ObjectName objectName = new ObjectName(packageName, new Hashtable<>(attributes));

            server.registerMBean(o, objectName);
        } catch (MalformedObjectNameException e) {
            LOGGER.warn(format("invalid object name: %s, cause: %s", name, e.getMessage()), e);
        } catch (NotCompliantMBeanException e) {
            LOGGER.warn(format("non-compliant mxbean: %s, cause: %s", name, e.getMessage()), e);
        } catch (InstanceAlreadyExistsException e) {
            LOGGER.warn(format("mxbean already exists by name: %s, cause: %s", name, e.getMessage()), e);
        } catch (MBeanRegistrationException e) {
            LOGGER.warn(format("unable to register mxbean name: %s, cause: %s", name, e.getMessage()), e);
        }
    }
}
