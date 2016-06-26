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

package com.intuit.autumn.service;

import com.google.common.base.Predicates;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.Collections2.filter;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A resource holder.
 *
 * @param <T> resource type
 */

public class ServiceResource<T> {

    private static final Logger LOGGER = getLogger(ServiceResource.class);
    private final Set<T> resources = new HashSet<>();

    /**
     * Resource adder.
     *
     * @param resources to be added resources
     */

    @SafeVarargs
    public final void addResources(final T... resources) {
        addResources(asList(resources));
    }

    /**
     * Resource adder.
     *
     * @param resources to be added resources
     */
    public final void addResources(final Collection<T> resources) {
        if (resources != null) {
            LOGGER.debug("adding resources size {}", resources.size());

            this.resources.addAll(filter(resources, Predicates.<T>notNull()));

            LOGGER.debug("added resources");
        }
    }

    /**
     * Collected resources getter.
     *
     * @return resources
     */

    public final Collection<T> getResources() {
        LOGGER.debug("getting resources");

        return unmodifiableCollection(resources);
    }
}
