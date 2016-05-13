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

/**
 * A no-op encrytor implementation.
 */

public class NoopEncryptor implements Encryptor, java.io.Serializable {

    /**
     *
     * @param data unencrypted data
     * @return encrypted data
     */

    @Override
    public String encrypt(final String data) {
        return data;
    }

    /**
     *
     * @param encryptedData enrypted data
     * @return unencrypted data
     */

    @Override
    public String decrypt(final String encryptedData) {
        return encryptedData;
    }
}
