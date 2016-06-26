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

import com.google.common.base.Optional;

import java.util.Map;

import static com.google.common.base.Optional.fromNullable;
/**
 * An HTTP/S client request builder.
 *
 * @param <T> a typed HTTP/S request response object
 */

public class HttpCallConfig<T> {

    // HTTP method, e.g.: GET, POST, PUT, DELETE
    private String httpMethod;
    // resource url
    private String url;
    // payload
    private Object data;
    // response type
    private Class<T> toMap;
    // HTTP headers
    private Map<String, String> headers;
    // HTTP query parameters
    private Map<String, String> queryParams;
    // accept type
    private String accept;
    // content type
    private String type;
    // expected response status code
    private int expectedStatus;
    // connection timeout
    private Integer connectionTimeout;
    // read timeout
    private Integer readTimeOut;
    // proxy URL
    private String proxyURL;
    // proxy port
    private int proxyPort;
    // use connection pooling
    private boolean useConnectionPooling;
    // maximum connection per hosts
    private Integer maxConnectionPerHost;

    /**
     * useConnectionPooling getter.
     *
     * @return useConnectionPooling value
     */

    public boolean getUseConnectionPooling() {
        return useConnectionPooling;
    }

    /**
     * useConnectionPooling setter.
     *
     * @param useConnectionPooling value
     */

    public void setUseConnectionPooling(boolean useConnectionPooling) {
        this.useConnectionPooling = useConnectionPooling;
    }

    /**
     * maxConnectionPerHost getter.
     *
     * @return maxConnectionPerHost value
     */

    public Optional<Integer> getMaxConnectionPerHost() {
        return fromNullable(maxConnectionPerHost);
    }

    /**
     * maxConnectionPerHost setter.
     *
     * @param maxConnectionPerHost value
     */

    public void setMaxConnectionPerHost(Integer maxConnectionPerHost) {
        this.maxConnectionPerHost = maxConnectionPerHost;
    }

    /**
     * proxyURL getter.
     *
     * @return proxyURL value
     */

    public Optional<String> getProxyURL() {
        return fromNullable(proxyURL);
    }

    /**
     * proxyURL setter.
     *
     * @param proxyURL value
     */

    public void setProxyURL(String proxyURL) {
        this.proxyURL = proxyURL;
    }

    /**
     * proxyPort getter.
     *
     * @return proxyPort value
     */

    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * proxyPort setter.
     *
     * @param proxyPort value
     */

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * connectionTimeout getter.
     *
     * @return connectionTimeout value
     */

    public Optional<Integer> getConnectionTimeout() {
        return fromNullable(connectionTimeout);
    }

    /**
     * connectionTimeout setter.
     *
     * @param connectionTimeout value
     */

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * readTimeout getter.
     *
     * @return readTimeout value
     */

    public Optional<Integer> getReadTimeOut() {
        return fromNullable(readTimeOut);
    }

    /**
     * readTimeout setter.
     *
     * @param readTimeOut value
     */

    public void setReadTimeOut(Integer readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    /**
     * httpMethod getter.
     *
     * @return httpMethod value
     */

    public String getHttpMethod() {
        return httpMethod;
    }

    /**
     * httpMethod setter.
     *
     * @param httpMethod value
     */

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * url getter.
     *
     * @return url value
     */

    public String getUrl() {
        return url;
    }

    /**
     * url setter.
     *
     * @param url value
     */

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * data getter.
     *
     * @return data value
     */

    public Optional<Object> getData() {
        return fromNullable(data);
    }

    /**
     * data setter.
     *
     * @param data value
     */

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * toMap getter.
     *
     * @return toMap value
     */

    public Class<T> getToMap() {
        return toMap;
    }

    /**
     * toMap setter.
     *
     * @param toMap value
     */

    public void setToMap(Class<T> toMap) {
        this.toMap = toMap;
    }

    /**
     * headers getter.
     *
     * @return headers value
     */

    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * headers setter.
     *
     * @param headers value
     */

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * queryParameters getter.
     *
     * @return queryParameters value
     */

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    /**
     * queryParameters setter.
     *
     * @param queryParams value
     */

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    /**
     * accept getter.
     *
     * @return accept value
     */

    public String getAccept() {
        return accept;
    }

    /**
     * accept setter.
     *
     * @param accept value
     */

    public void setAccept(String accept) {
        this.accept = accept;
    }

    /**
     * type getter.
     *
     * @return type value
     */

    public String getType() {
        return type;
    }

    /**
     * type setter.
     *
     * @param type value
     */

    public void setType(String type) {
        this.type = type;
    }

    /**
     * expectedStatus getter.
     *
     * @return expectedStatus value
     */

    public int getExpectedStatus() {
        return expectedStatus;
    }

    /**
     * expectedStatus setter.
     *
     * @param expectedStatus value
     */

    public void setExpectedStatus(int expectedStatus) {
        this.expectedStatus = expectedStatus;
    }

    /**
     * Typed HttpCallConfig builder utility.
     *
     * @param <T>
     */

    public static class Builder<T> {

        private String httpMethod;
        private String url;
        private Object data;
        private Class<T> toMap;
        private Map<String, String> headers;
        private Map<String, String> queryParams;
        private String accept;
        private String type;
        private int expectedStatus;
        private Integer connectionTimeout;
        private Integer readTimeOut;
        private String proxyURL;
        private int proxyPort;
        private boolean useConnectionPooling;
        private Integer maxConnectionPerHost;

        private Builder() {
        }

        /**
         * Create a new Builder instance.
         *
         * @return Builder
         */

        public static Builder aHttpCallConfig() {
            return new Builder();
        }

        /**
         * Set httpMethod.
         *
         * @param httpMethod http method
         * @return builder instance
         */

        public Builder withHttpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        /**
         * Set URL.
         *
         * @param url resource url
         * @return builder instance
         */

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * Set payload data.
         *
         * @param data payload data
         * @return builder instance
         */

        public Builder withData(Object data) {
            this.data = data;
            return this;
        }

        /**
         * Set typed response.
         *
         * @param toMap typed response
         * @return builder instance
         */

        public Builder withToMap(Class<T> toMap) {
            this.toMap = toMap;
            return this;
        }

        /**
         * Set headers.
         *
         * @param headers headers
         * @return builder instance
         */

        public Builder withHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        /**
         * Set query parameters.
         *
         * @param queryParams query parameters.
         * @return builder instance
         */

        public Builder withQueryParams(Map<String, String> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        /**
         * Set accept type.
         *
         * @param accept accept type
         * @return builder instance
         */

        public Builder withAccept(String accept) {
            this.accept = accept;
            return this;
        }

        /**
         * Set content type.
         *
         * @param type content type
         * @return builder instance
         */

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        /**
         * Set expected response status code.
         *
         * @param expectedStatus expected response status code.
         * @return builder instance
         */

        public Builder withExpectedStatus(int expectedStatus) {
            this.expectedStatus = expectedStatus;
            return this;
        }

        /**
         * Set connection timeout.
         *
         * @param connectionTimeout connection timeout
         * @return builder instance
         */

        public Builder withConnectionTimeout(Integer connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        /**
         * Set read timeout.
         *
         * @param readTimeOut read timeout.
         * @return builder instance
         */

        public Builder withReadTimeOut(Integer readTimeOut) {
            this.readTimeOut = readTimeOut;
            return this;
        }

        /**
         * Set proxy URL.
         *
         * @param proxyURL proxy URL
         * @return builder instance
         */

        public Builder withProxyURL(String proxyURL) {
            this.proxyURL = proxyURL;
            return this;
        }

        /**
         * Set proxy port.
         *
         * @param proxyPort proxy port
         * @return builder instance
         */

        public Builder withProxyPort(int proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        /**
         * Set use connection pooling.
         *
         * @param useConnectionPooling use connection pooling
         * @return builder instance
         */

        public Builder withUseConnectionPooling(boolean useConnectionPooling) {
            this.useConnectionPooling = useConnectionPooling;
            return this;
        }

        /**
         * Set maximum connections per host.
         *
         * @param maxConnectionPerHost maximum connections per host
         * @return builder instance
         */

        public Builder withMaxConnectionPerHost(Integer maxConnectionPerHost) {
            this.maxConnectionPerHost = maxConnectionPerHost;
            return this;
        }

        /**
         * Builder builder.
         *
         * @return materialized Builder
         */

        public Builder but() {
            return aHttpCallConfig().withHttpMethod(httpMethod).withUrl(url).withData(data).withToMap(toMap)
                    .withHeaders(headers).withQueryParams(queryParams).withAccept(accept).withType(type)
                    .withExpectedStatus(expectedStatus).withConnectionTimeout(connectionTimeout)
                    .withReadTimeOut(readTimeOut).withProxyURL(proxyURL).withProxyPort(proxyPort)
                    .withUseConnectionPooling(useConnectionPooling).withMaxConnectionPerHost(maxConnectionPerHost);
        }

        /**
         * HttpCallConfig builder.
         *
         * @return materialized HttpCallConfig
         */

        public HttpCallConfig build() {
            HttpCallConfig httpCallConfig = new HttpCallConfig();

            httpCallConfig.setHttpMethod(httpMethod);
            httpCallConfig.setUrl(url);
            httpCallConfig.setData(data);
            httpCallConfig.setToMap(toMap);
            httpCallConfig.setHeaders(headers);
            httpCallConfig.setQueryParams(queryParams);
            httpCallConfig.setAccept(accept);
            httpCallConfig.setType(type);
            httpCallConfig.setExpectedStatus(expectedStatus);
            httpCallConfig.setConnectionTimeout(connectionTimeout);
            httpCallConfig.setReadTimeOut(readTimeOut);
            httpCallConfig.setProxyURL(proxyURL);
            httpCallConfig.setProxyPort(proxyPort);
            httpCallConfig.setUseConnectionPooling(useConnectionPooling);
            httpCallConfig.setMaxConnectionPerHost(maxConnectionPerHost);

            return httpCallConfig;
        }
    }
}
