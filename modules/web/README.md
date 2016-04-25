# Autumn : Web

#### note: future(banners)

## Module

This module provides the operational implementations for readily instantiatable HTTP and HTTPS providers. 

Configuration options include:

    logback-access.xml
    web.properties
      application.http.enabled=[true|false]
      application.http.port=[integer, positive]
      application.http.context.path=[string, endpoint URI root path]
      application.jersey.provider.paths=[string, comman delimited package names]
      application.jersey.pojo.enabled=[true|false]
      application.jersey.request.filters=[string, request filters]
      application.jersey.response.filters=[string, response filters]
      application.jersey.wadl.enabled=[true|false]
      application.httpconfig.output.buffersize=[integer, positive]
      application.https.enabled=[true|false]
      application.https.port=[integer, positive]
      application.https.idletimeout=[integer, positive]
      application.ssl.keystore.path=[string, file path]
    web-secrets.properties
      application.ssl.keystore.password=[string]
      application.ssl.keymanager.password=[string]