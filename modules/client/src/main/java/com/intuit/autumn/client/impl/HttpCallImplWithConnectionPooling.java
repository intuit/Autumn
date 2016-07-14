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

package com.intuit.autumn.client.impl;

import com.google.inject.Inject;
import com.intuit.autumn.client.HttpCall;
import com.intuit.autumn.client.HttpCallConfig;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandler;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.ApacheHttpClientHandler;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.slf4j.Logger;

import java.util.Map;

import static com.sun.jersey.client.apache.config.ApacheHttpClientConfig.PROPERTY_PROXY_URI;
import static java.lang.String.format;
import static javax.ws.rs.HttpMethod.*;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * An HTTP/S client implementation that includes connection pooling.
 *
 * @param <T> a typed HTTP/S request response object
 */

// FIXME: merge with HttpCallImpl

public class HttpCallImplWithConnectionPooling<T> implements HttpCall<T> {

    private static final Logger LOGGER = getLogger(HttpCall.class);
    private Client client;

    /**
     * Constructor.
     */

    @Inject
    public HttpCallImplWithConnectionPooling() {
    }

    @Override
    public T makeRequest(final String httpMethod, final String url, final Object data, final Class<T> toMap,
                         final Map<String, String> headers, final Map<String, String> queryParams,
                         final String accept, final String type, final int expectedStatus) {
        return (T) makeRequest(httpMethod, HttpCallConfig.Builder.aHttpCallConfig()
                .withUrl(url)
                .withData(data)
                .withToMap(toMap)
                .withHeaders(headers)
                .withQueryParams(queryParams)
                .withAccept(accept)
                .withType(type)
                .withExpectedStatus(expectedStatus).build());
    }

    @Override
    public T makeRequest(final String httpMethod, final HttpCallConfig<T> config) {
        ClientResponse response = getResponse(httpMethod, config);

        //check for a successful verification
        if (response == null) {
            String msg = format("data.autumn: HttpCalls: error for the request with following parameters: %s",
                    config.toString());

            LOGGER.error(msg);

            throw new UnsupportedOperationException(msg);
        }

        if (response.getStatus() != config.getExpectedStatus()) {
            String msg = format("data.autumn: HttpCalls: error, get call to %s returned status was %s expecting status = %s",
                    config.getUrl(), response.getStatus(), config.getExpectedStatus());

            LOGGER.error(msg);

            // Note that this is a required step to free the thread when there is a response
            response.close();

            throw new HttpCallException(msg);
        }

        // Typically response.getEntity takes care of closing the response and freeing the thread.
        return (ClientResponse.class == config.getToMap() || config.getToMap() == null) ? (T) response : response.getEntity(config.getToMap());

    }

    private ClientResponse getResponse(final String httpMethod, final HttpCallConfig<T> config) {
        // Set up the webresource object to make the REST call by obtaining the client object with or without
        // proxy as specified.
        client = getClient(config);

        WebResource webResource = client.resource(config.getUrl());
        webResource = addQueryParams(webResource, config.getQueryParams());
        WebResource.Builder builder = addHeaders(webResource, config.getHeaders(), config.getAccept(), config.getType());
        ClientResponse response = null;

        try {
            //make the rest call
            switch (httpMethod) {
                case GET:
                    response = builder.get(ClientResponse.class);

                    break;
                case POST:
                    response = (config.getData().isPresent()) ?
                            builder.post(ClientResponse.class, config.getData().get()) :
                            builder.post(ClientResponse.class);

                    break;
                case PUT:
                    response = (config.getData().isPresent()) ?
                            builder.put(ClientResponse.class, config.getData().get()) :
                            builder.put(ClientResponse.class);

                    break;
                case DELETE:
                    response = builder.delete(ClientResponse.class);

                    break;
                default:
                    String msg = format("data.autumn: HttpCalls: error, unsupported HTTP method called %s",
                            httpMethod);

                    LOGGER.error(msg);

                    throw new UnsupportedOperationException(msg);
            }
        } catch (Exception e) {
            LOGGER.error("data.autumn: HttpCall error", e);
        } finally {
            if (!config.getUseConnectionPooling()) {
                client.destroy();
            }
        }

        return response;
    }

    private Client getClient(final HttpCallConfig<T> config) {
        // If either use connection pooling
        if (!config.getUseConnectionPooling() || client == null) {
            MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

            if (config.getMaxConnectionPerHost().or(-1) > 0) {
                connectionManager.getParams().setDefaultMaxConnectionsPerHost(config.getMaxConnectionPerHost().get());
            } else {
                connectionManager.getParams().setDefaultMaxConnectionsPerHost(20);
            }

            HttpClient httpClient = new HttpClient(connectionManager);
            ApacheHttpClientHandler clientHandler = new ApacheHttpClientHandler(httpClient);
            ClientHandler root = new ApacheHttpClient(clientHandler);
            ClientConfig config1 = new DefaultApacheHttpClientConfig();

            if (config.getProxyURL().isPresent()) {
                String proxyURLWithPort = "http://" + config.getProxyURL() + ':' + config.getProxyPort();

                config1.getProperties().put(PROPERTY_PROXY_URI, proxyURLWithPort);
            }

            client = new Client(root, config1);

            //Setting connection timeouts and read timeouts when present and doing it in the getClient method
            if (config.getConnectionTimeout().or(-1) > 0) {
                client.setConnectTimeout(config.getConnectionTimeout().get());
            }

            if (config.getReadTimeOut().or(-1) > 0) {
                client.setReadTimeout(config.getReadTimeOut().get());
            }

            //Logging the client properties
            LOGGER.info("Created a new client with the following properties set by the application {}",
                    client.getProperties().toString());
        }

        return client;
    }

    @Override
    public WebResource.Builder addHeaders(final WebResource webResource, final Map<String, String> headers,
                                          final String accept, final String type) {
        WebResource.Builder builder = webResource.getRequestBuilder();

        if (accept != null) {
            builder.accept(accept);
        }

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }

        if (type != null) {
            builder.type(type);
        }

        return builder;
    }

    @Override
    public WebResource addQueryParams(final WebResource webResource, Map<String, String> queryParams) {
        WebResource resource = webResource;

        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                resource = resource.queryParam(entry.getKey(), entry.getValue());
            }
        }

        return resource;
    }

    @Override
    public T doGet(final String url, final Class<T> toMap, final Map<String, String> headers,
                   final Map<String, String> queryParams, final String accept, final int expectedStatus) {
        return makeRequest(GET, url, null, toMap, headers, queryParams, accept, null, expectedStatus);
    }

    @Override
    public T doPost(final String url, final Object data, final Class<T> toMap, final Map<String, String> headers,
                    final Map<String, String> queryParams, final String accept, final String type,
                    final int expectedStatus) {
        return makeRequest(POST, url, data, toMap, headers, queryParams, accept, type, expectedStatus);
    }

    @Override
    public T doPost(final HttpCallConfig<T> httpCallConfig) {
        return makeRequest(POST, httpCallConfig);
    }

    @Override
    public T doGet(final HttpCallConfig<T> httpCallConfig) {
        return makeRequest(GET, httpCallConfig);
    }

    @Override
    public T doPut(final HttpCallConfig<T> httpCallConfig) {
        return makeRequest(PUT, httpCallConfig);
    }

    @Override
    public T doDelete(final HttpCallConfig<T> httpCallConfig) {
        return makeRequest(DELETE, httpCallConfig);
    }

    @Override
    public T doPut(final String url, final Object data, final Class<T> toMap, final Map<String, String> headers,
                   final Map<String, String> queryParams, final String accept, final String type,
                   final int expectedStatus) {
        return makeRequest(PUT, url, data, toMap, headers, queryParams, accept, type, expectedStatus);
    }

    @Override
    public T doDelete(final String url, final Class<T> toMap, final Map<String, String> headers,
                      final Map<String, String> queryParams, final String accept, final int expectedStatus) {
        return makeRequest(DELETE, url, null, toMap, headers, queryParams, accept, null, expectedStatus);
    }
}