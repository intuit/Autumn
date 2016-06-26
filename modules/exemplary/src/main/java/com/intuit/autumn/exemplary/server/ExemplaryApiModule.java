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

import com.google.inject.AbstractModule;
import com.intuit.autumn.api.ApiModule;

import static com.google.inject.Scopes.SINGLETON;

/**
 * An injector that includes prototypical application implementation dependencies.
 */

public class ExemplaryApiModule extends AbstractModule {

    /**
     * Install module dependencies.
     */

    @Override
    protected void configure() {
        install(new ApiModule());
        bind(PingService.class).in(SINGLETON);
    }
}
