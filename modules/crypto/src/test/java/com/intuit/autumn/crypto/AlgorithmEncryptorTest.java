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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AlgorithmEncryptorTest {

    private Encryptor algorithmEncryptor;
    private String testMessage = "test";

    @Before
    public void initialize() {
        algorithmEncryptor = createInjector(new CryptoModule()).getInstance(AlgorithmEncryptor.class);
    }

    @Test(expected = NullPointerException.class)
    public void testEncryptNull() {
        algorithmEncryptor.encrypt(null);
    }

    @Test
    public void testDecryptNull() {
        assertThat(algorithmEncryptor.decrypt(null), is(nullValue()));
    }

    @Test
    public void testEncrypt() {
        assertThat(algorithmEncryptor.encrypt(testMessage), is(not(testMessage)));
    }

    @Test
    public void testDecrypt() {
        String encrypted = algorithmEncryptor.encrypt(testMessage);

        assertThat(algorithmEncryptor.decrypt(encrypted), is(testMessage));
    }
}
