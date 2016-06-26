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

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.slf4j.Logger;

import static java.security.Security.addProvider;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A algorithmic backed cryptography utility.
 */

public class AlgorithmEncryptor implements Encryptor, java.io.Serializable {

    private static final Logger LOGGER = getLogger(AlgorithmEncryptor.class);
    private final PooledPBEStringEncryptor encryptor;

    /**
     * Constructor accepting injectable configurables.
     *
     * @param key       the key to be used by the configured implementation
     * @param algorithm the configured cryptography algorithm
     * @param poolSize  the configured cryptography implementation pool size
     */

    @Inject
    public AlgorithmEncryptor(@Named("crypto.key") final String key,
                              @Named("crypto.algorithm") final String algorithm,
                              @Named("crypto.poolsize") final int poolSize) {
        encryptor = new PooledPBEStringEncryptor();

        addProvider(new BouncyCastleProvider());

        encryptor.setProvider(new BouncyCastleProvider());
        encryptor.setAlgorithm(algorithm);
        encryptor.setPoolSize(poolSize);
        encryptor.setPassword(key);
    }

    /**
     * Encrypts provided data utilizing backing cryptography implementation.
     *
     * @param data data to be encrypted
     * @return encrypted data
     */

    @Override
    public String encrypt(final String data) {
        LOGGER.debug("encrypting data");

        requireNonNull(data, "data must not be null");

        String encryptedData = encryptor.encrypt(data);

        LOGGER.debug("encrypted data");

        return encryptedData;
    }

    /**
     * Decrypts provided data utilizing backing cryptography implementation.
     *
     * @param encryptedData data to be decrypted
     * @return decrypted data
     */

    @Override
    public String decrypt(final String encryptedData) {
        LOGGER.debug("decrypting data");

        String decryptedData = encryptor.decrypt(encryptedData);

        LOGGER.debug("decrypted data");

        return decryptedData;
    }
}
