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

package com.intuit.data.autumn.web;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;

import javax.servlet.DispatcherType;
import java.io.File;
import java.io.FileNotFoundException;

import static java.util.EnumSet.allOf;
import static org.eclipse.jetty.http.HttpVersion.HTTP_1_1;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A lifecycle provider for a Jetty https service.
 */

public class HttpsService extends AbstractIdleService {

    private static final Logger LOGGER = getLogger(HttpsService.class);
    private final int httpsPort;
    private final int httpsIdleTimeout;
    private final int httpConfigOutputBufferSize;
    private final String keyStorePath;
    private final String sslKeyStorePassword;
    private final String sslKeyManagerPassword;
    private final String contextPath;
    private final GuiceFilter guiceFilter;
    private Server server;

    /**
     * @param httpsPort
     * @param httpsIdleTimeout
     * @param httpConfigOutputBufferSize
     * @param keyStorePath
     * @param sslKeyStorePassword
     * @param sslKeyManagerPassword
     * @param contextPath
     * @param guiceFilter
     */

    @Inject
    public HttpsService(
            @Named("application.https.port") int httpsPort,
            @Named("application.https.idletimeout") int httpsIdleTimeout,
            @Named("application.httpconfig.output.buffersize") int httpConfigOutputBufferSize,
            @Named("application.ssl.keystore.path") String keyStorePath,
            @Named("application.ssl.keystore.password") String sslKeyStorePassword,
            @Named("application.ssl.keymanager.password") String sslKeyManagerPassword,
            @Named("application.http.context.path") String contextPath,
            GuiceFilter guiceFilter) {
        LOGGER.debug("instantiating {} with httpsPort: {}", new Object[]{serviceName(), httpsPort});

        this.httpsIdleTimeout = httpsIdleTimeout;
        this.httpConfigOutputBufferSize = httpConfigOutputBufferSize;
        this.keyStorePath = keyStorePath;
        this.sslKeyStorePassword = sslKeyStorePassword;
        this.sslKeyManagerPassword = sslKeyManagerPassword;
        this.httpsPort = httpsPort;
        this.contextPath = contextPath.isEmpty() ? "/" : contextPath;
        this.guiceFilter = guiceFilter;

        LOGGER.debug("instantiated {} with httpsPort: {}", new Object[]{serviceName(), httpsPort});
    }

    /**
     * @throws Exception
     */

    @Override
    protected void startUp() throws Exception {
        LOGGER.info("starting {}", serviceName());

        // Create a basic jetty server object without declaring the port.
        // Since we are configuring connectors directly we'll be setting ports on those connectors.

        if (server != null) {
            LOGGER.error("Server is already stopped");

            return;
        } else {
            server = new Server();
        }

        File keystoreFile = new File(keyStorePath);

        if (!keystoreFile.exists()) {
            throw new FileNotFoundException(keystoreFile.getAbsolutePath());
        }

        HttpConfiguration httpConfiguration = new HttpConfiguration();

        httpConfiguration.setSecureScheme("https");
        httpConfiguration.setSecurePort(httpsPort);
        httpConfiguration.setOutputBufferSize(httpConfigOutputBufferSize);

        // SSL Context Factory for HTTPS
        // SSL requires a certificate so we configure a factory for ssl contents
        // with information pointing to what keystore the ssl connection needs
        // to know about. Much more configuration is available the ssl context,
        // including things like choosing the particular certificate out of
        // a keystore to be used.
        SslContextFactory sslContextFactory = new SslContextFactory();

        sslContextFactory.setKeyStorePath(keystoreFile.getAbsolutePath());
        sslContextFactory.setKeyStorePassword(sslKeyStorePassword);
        sslContextFactory.setKeyManagerPassword(sslKeyManagerPassword);

        // HTTPS Configuration
        // A new HttpConfiguration object is needed for the next connector
        // and you can pass the old one as an argument to effectively clone the
        // contents. On this HttpConfiguration object we add a
        // SecureRequestCustomizer which is how a new connector is able to
        // resolve the https connection before handing control over to the Jetty Server.
        HttpConfiguration httpsConfiguration = new HttpConfiguration(httpConfiguration);

        httpsConfiguration.addCustomizer(new SecureRequestCustomizer());

        // Configure HTTPS connector
        ServerConnector https = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, HTTP_1_1.asString()),
                new HttpConnectionFactory(httpsConfiguration));

        https.setPort(httpsPort);
        https.setIdleTimeout(httpsIdleTimeout);

        // The server have multiple connectors registered with it,
        // now requests can flow into the server from both http and https
        // urls to their respective ports and be processed accordingly by jetty.
        // Set the connectors
        server.setConnectors(new Connector[]{https});

        HandlerCollection handlers = new HandlerCollection(false);

        handlers.addHandler(makeMonitorHandler());
        handlers.addHandler(makeContextHandler(guiceFilter));

        server.setHandler(handlers);
        server.start();

        LOGGER.info("started {}", serviceName());
    }

    private ContextHandlerCollection makeMonitorHandler() {
        ServletContextHandler handler = new ServletContextHandler();

        handler.setContextPath("/monitor");
        handler.addServlet(new ServletHolder(new PingServlet()), "/ping");
        // note: registering a servlet for '/*' appears to be required, fails otherwise
        handler.addServlet(new ServletHolder(new InvalidRequestServlet()), "/*");

        ContextHandlerCollection context = new ContextHandlerCollection();

        context.setHandlers(new Handler[]{handler});

        return context;
    }

    private ContextHandlerCollection makeContextHandler(GuiceFilter guiceFilter) {
        ServletContextHandler handler = new ServletContextHandler();

        handler.setContextPath(contextPath);
        handler.addFilter(new FilterHolder(guiceFilter), "/*", allOf(DispatcherType.class));
        // note: registering a servlet for '/*' appears to be required, fails otherwise
        handler.addServlet(new ServletHolder(new InvalidRequestServlet()), "/*");

        ContextHandlerCollection context = new ContextHandlerCollection();

        context.setHandlers(new Handler[]{handler});

        return context;
    }

    /**
     * @throws Exception
     */

    @Override
    protected void shutDown() throws Exception {
        if (server == null) {
            LOGGER.error("Server is already stopped");
        } else {
            LOGGER.debug("stopping {}", serviceName());

            try {
                for (Connector c : server.getConnectors()) {
                    c.stop();
                }
            } finally {
                server.stop();
                server = null;
            }

            LOGGER.debug("stopped {}", serviceName());
        }
    }
}
