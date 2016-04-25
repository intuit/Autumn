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

/**
 * This module provides the operational implementations for readily instantiatable HTTP and HTTPS providers.
 *
 * Configuration options include:
 *
 *   logback.xml
 *   web.properties
 *     application.http.enabled=[true|false]
 *     application.http.port=[integer, positive]
 *     application.http.context.path=[string, endpoint URI root path]
 *     application.jersey.provider.paths=[string, comman delimited package names]
 *     application.jersey.pojo.enabled=[true|false]
 *     application.jersey.request.filters=[string, request filters]
 *     application.jersey.response.filters=[string, response filters]
 *     application.jersey.wadl.enabled=[true|false]
 *     application.httpconfig.output.buffersize=[integer, positive]
 *     application.https.enabled=[true|false]
 *     application.https.port=[integer, positive]
 *     application.https.idletimeout=[integer, positive]
 *     application.ssl.keystore.path=[string, file path]
 *   web-secrets.properties
 *     application.ssl.keystore.password=[string]
 *     application.ssl.keymanager.password=[string]
 */

package com.intuit.data.autumn.web;