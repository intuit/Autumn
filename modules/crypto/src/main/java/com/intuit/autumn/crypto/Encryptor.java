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

/**
 * A general purpose encryption interface that is algorithm implementation agnostic.
 */

public interface Encryptor {

    /**
     *
     * @param data unencrypted data
     * @return encrypted data
     */

    String encrypt(String data);

    /**
     *
     * @param encryptedData encrypted data
     * @return unencrypted data
     */

    String decrypt(String encryptedData);
}
