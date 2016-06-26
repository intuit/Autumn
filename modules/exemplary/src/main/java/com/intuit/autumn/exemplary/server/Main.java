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

package com.intuit.autumn.exemplary.server;

import com.intuit.autumn.metrics.MetricsModule;
import com.intuit.autumn.service.ServiceManager;
import org.slf4j.Logger;

import static com.intuit.autumn.metrics.MetricsServices.getEnabledMetricsServices;
import static com.intuit.autumn.web.WebServices.getEnabledWebServices;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Prototypical application instantiation implementation.
 */

public class Main {

    private static final Logger LOGGER = getLogger(MetricsModule.class);

    /**
     * Application entry point.
     *
     * @param args application arguments
     * @throws Exception unintended exception
     */

    public static void main(String[] args) throws Exception {
        LOGGER.info("starting {}", Main.class.getSimpleName());

        ServiceManager serviceManager = new ServiceManager();

        serviceManager.addModules(ExemplaryApiModule.class, MetricsModule.class)
                .addServices(getEnabledWebServices())
                .addServices(getEnabledMetricsServices());

        serviceManager.start();

        LOGGER.info("started {}", Main.class.getSimpleName());
    }
}
