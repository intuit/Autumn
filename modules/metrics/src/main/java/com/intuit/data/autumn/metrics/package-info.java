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
 * This module provides the context necessary to readily implement operational java-metrics instrumentation.
 *
 * Configuration options include:
 *
 *   metrics.properties
 *     metrics.csv.enabled=[true|false]
 *     metrics.csv.directory=[string, file path]
 *     metrics.csv.interval.seconds=[integer, positive integer]
 *     metrics.graphite.enabled=[true|false]
 *     metrics.graphite.service=[uri]
 *     metrics.graphite.service.prefix=[string]
 *     metrics.graphite.interval.seconds=[integer, positive integer]
 *     metrics.hystrix.enabled=[true|false]
 *     metrics.jmx.enabled=[true|false]
 */

package com.intuit.data.autumn.metrics;