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

package com.intuit.autumn.client;

import com.sun.jersey.api.client.WebResource;

import java.util.Map;

/**
 * An abstract representation of a prototypical HTTP/S client request.
 *
 * @param <T> a typed HTTP/S response object
 */

public interface HttpCall<T> {

    /**
     * A fully supported HTTP request populated with primitive configurables.
     *
     * @param httpMethod method (e.g. GET, POST, PUT, DELETE)
     * @param url target resource url
     * @param data payload
     * @param toMap response type
     * @param headers headers
     * @param queryParams resource query string
     * @param accept response accept type
     * @param type content type
     * @param expectedStatus expected status code
     * @return materialized typed response
     */

    T makeRequest(String httpMethod, String url, Object data, Class<T> toMap, Map<String, String> headers,
                  Map<String, String> queryParams, String accept, String type, int expectedStatus);

    /**
     * A fully supported HTTP request populated with a holder configurables.
     *
     * @param httpMethod method (e.g. GET, POST, PUT, DELETE)
     * @param config configuration holder
     * @return materialized typed response
     */

    T makeRequest(String httpMethod, HttpCallConfig<T> config);

    /**
     * Header adder.
     *
     * @param webResource request resource
     * @param headers headers
     * @param accept response accept type
     * @param type content type
     * @return request builder
     */

    WebResource.Builder addHeaders(WebResource webResource, Map<String, String> headers, String accept, String type);

    /**
     * Query parameter adder.
     *
     * @param webResource request resource
     * @param queryParams query parameters
     * @return request resource
     */

    WebResource addQueryParams(WebResource webResource, Map<String, String> queryParams);

    /**
     * HTTP GET executor.
     *
     * @param url resource
     * @param toMap response type
     * @param headers headers
     * @param queryParams query parameters
     * @param accept response accept type
     * @param expectedStatus expected status code
     * @return materialized typed response
     */

    T doGet(String url, Class<T> toMap, Map<String, String> headers, Map<String, String> queryParams,
            String accept, int expectedStatus);

    /**
     * HTTP POST executor.
     *
     * @param url resource
     * @param data payload
     * @param toMap response type
     * @param headers headers
     * @param queryParams query parameters
     * @param accept response accept type
     * @param type content type
     * @param expectedStatus expected status code
     * @return materialized typed response
     */

    T doPost(String url, Object data, Class<T> toMap, Map<String, String> headers, Map<String, String> queryParams,
             String accept, String type, int expectedStatus);

    /**
     * HTTP PUT executor.
     *
     * @param url resource
     * @param data payload
     * @param toMap response type
     * @param headers headers
     * @param queryParams query parameters
     * @param accept response accept type
     * @param type content type
     * @param expectedStatus expected status code
     * @return materialized typed response
     */

    T doPut(String url, Object data, Class<T> toMap, Map<String, String> headers, Map<String, String> queryParams,
            String accept, String type, int expectedStatus);

    /**
     * HTTP Delete executor.
     *
     * @param url resource
     * @param toMap response type
     * @param headers headers
     * @param queryParams query parameters
     * @param accept response accept type
     * @param expectedStatus expected status code
     * @return materialized typed response
     */

    T doDelete(String url, Class<T> toMap, Map<String, String> headers, Map<String, String> queryParams,
               String accept, int expectedStatus);

    /**
     * HTTP GET executor.
     *
     * @param config request holder
     * @return materialized typed response
     */

    T doGet(HttpCallConfig<T> config);

    /**
     * HTTP POST executor.
     *
     * @param config request holder
     * @return materialized typed response.
     */

    T doPost(HttpCallConfig<T> config);

    /**
     * HTTP PUT executor.
     *
     * @param config request holder
     * @return materialized typed response.
     */

    T doPut(HttpCallConfig<T> config);

    /**
     * HTTP DELETE executor.
     *
     * @param config request holder
     * @return materialized typed response
     */

    T doDelete(HttpCallConfig<T> config);
}