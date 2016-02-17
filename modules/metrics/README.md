# Autumn : Metrics

#### note: future(banners)

## Module

This module provides the context necessary to readily implement operational java-metrics instrumentation.

Configuration options include:

    metrics.properties
      metrics.csv.enabled=[true|false]
      metrics.csv.directory=[string, file path]
      metrics.csv.interval.seconds=[integer, positive integer]
      metrics.graphite.enabled=[true|false]
      metrics.graphite.service=[uri]
      metrics.graphite.service.prefix=[string]
      metrics.graphite.interval.seconds=[integer, positive integer]
      metrics.hystrix.enabled=[true|false]
      metrics.jmx.enabled=[true|false]