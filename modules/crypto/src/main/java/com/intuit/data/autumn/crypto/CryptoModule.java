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

package com.intuit.data.autumn.crypto;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.MapBinder;

import java.util.Properties;

import static com.google.inject.name.Names.named;
import static com.intuit.data.autumn.utils.PropertyFactory.create;
import static com.intuit.data.autumn.utils.PropertyFactory.getProperty;
import static java.lang.Boolean.TRUE;

/**
 * An injector that includes Cryptography implementation details.
 */

public class CryptoModule extends AbstractModule {

    public static final String PROPERTY_NAME = "/crypto.properties";
    public static final String SECRETS_PROPERTY_NAME = "/crypto-secrets.properties";
    public static final String DECRYPTION_KEYS_PROPERTY_NAME = "/decryption-keys.properties";

    /**
     * Install module dependencies.
     */

    @Override
    protected void configure() {
        Properties properties = create(PROPERTY_NAME, CryptoModule.class);
        Properties secretProperties = create(SECRETS_PROPERTY_NAME, CryptoModule.class);
        Properties decryptionProperties = create(DECRYPTION_KEYS_PROPERTY_NAME, CryptoModule.class);

        boolean cryptoEnabled = Boolean.valueOf(getProperty("crypto.enabled", properties, TRUE.toString()));

        bind(String.class).annotatedWith(named("crypto.key"))
                .toInstance(getProperty("crypto.key", secretProperties));
        bind(String.class).annotatedWith(named("crypto.algorithm"))
                .toInstance(getProperty("crypto.algorithm", secretProperties));
        bind(Integer.class).annotatedWith(named("crypto.poolsize"))
                .toInstance(Integer.valueOf(getProperty("crypto.poolsize", properties)));
        bind(Encryptor.class).to(cryptoEnabled ? AlgorithmEncryptor.class : NoopEncryptor.class).in(Singleton.class);

        MapBinder<String, Encryptor> mapBinder
                = MapBinder.newMapBinder(binder(), String.class, Encryptor.class);

        for (String version : decryptionProperties.stringPropertyNames()) {
            Encryptor encryptor;
            if (cryptoEnabled) {
                encryptor = new AlgorithmEncryptor(decryptionProperties.getProperty(version),
                        getProperty("crypto.algorithm", secretProperties),
                        Integer.valueOf(getProperty("crypto.poolsize", properties)));
            } else {
                encryptor = new NoopEncryptor();
            }
            mapBinder.addBinding(version).toInstance(encryptor);
        }
    }
}
