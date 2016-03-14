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

package com.intuit.data.autumn.view;

import com.google.inject.Injector;
import com.google.inject.Key;
import org.junit.Test;

import java.util.Properties;

import static com.google.common.primitives.Ints.tryParse;
import static com.google.inject.Guice.createInjector;
import static com.google.inject.name.Names.named;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static com.intuit.data.autumn.utils.PropertyFactory.create;

public class ViewModuleTest {

    @Test
    public void injectable() throws Exception {
        Injector injector = createInjector(new ViewModule());
        Properties properties = create(ViewModule.PROPERTY_NAME);
        int resourceExpiryTimeInSeconds = tryParse(properties.getProperty("resource.expiryTimeInSeconds"));

        assertThat(injector.getInstance(new Key<Integer>(named("resource.expiryTimeInSeconds")) {
        }), equalTo(resourceExpiryTimeInSeconds));

        injector.getInstance(Resource.class);
    }
}
