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

package com.intuit.autumn.crypto;

import org.junit.Before;
import org.junit.Test;

import static com.google.inject.Guice.createInjector;
import static java.lang.Boolean.FALSE;
import static java.lang.System.setProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class NoOpCryptoModuleTest {

    private Encryptor encryptor;

    @Before
    public void initialize() {
        setProperty("crypto.enabled", FALSE.toString());

        encryptor = createInjector(new CryptoModule()).getInstance(Encryptor.class);
    }

    @Test
    public void testNoOpEncryptor() {
        assertThat(encryptor, is(instanceOf(NoopEncryptor.class)));
    }
}
