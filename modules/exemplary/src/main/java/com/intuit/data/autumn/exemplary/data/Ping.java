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

package com.intuit.data.autumn.exemplary.data;

import static java.lang.String.format;

/**
 * Prototypical datagram.
 */

public class Ping {

    private String id;
    private String message;

    /**
     * Empty constructor.
     *
     * note: necessary default constructor used by jackson for serialization/deserialization
     */

    public Ping() {
    }

    /**
     * Constructor with configurable state.
     *
     * @param id instance id
     * @param message instance payload
     */

    public Ping(String id, String message) {
        this.id = id;
        this.message = message;
    }

    /**
     * Get instance id.
     *
     * @return instance id
     */

    public String getId() {
        return id;
    }

    /**
     * Set instance id.
     *
     * @param id instance id
     */

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get instance payload.
     *
     * @return instance payload
     */

    public String getMessage() {
        return message;
    }

    /**
     * Set instance payload.
     *
     * @param message instance payload
     */

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Instance JSON representation.
     *
     * @return json representation
     */

    @Override
    public String toString() {
        return format("Ping{id='%s',message='%s'}", id, message);
    }
}
