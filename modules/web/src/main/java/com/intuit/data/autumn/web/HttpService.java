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

import ch.qos.logback.access.jetty.RequestLogImpl;
import ch.qos.logback.access.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import javax.servlet.DispatcherType;
import java.io.IOException;
import java.io.InputStream;

import static java.util.EnumSet.allOf;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A lifecycle provider for a Jetty http service.
 */

public class HttpService extends AbstractIdleService {

    private static final Logger LOGGER = getLogger(HttpService.class);
    private final int httpPort;
    private final String contextPath;
    private final GuiceFilter guiceFilter;
    @Nullable
    private final String contentDirectory;
    private Server server;

    /**
     * @param port
     * @param contextPath
     * @param guiceFilter
     */

    @Inject
    public HttpService(
            @Named("application.http.port") int port,
            @Named("application.http.context.path") String contextPath,
            @Named("application.http.content.directory") @Nullable String contentDirectory,
            GuiceFilter guiceFilter) {
        LOGGER.debug("instantiating {} with httpPort: {}, contextPath: {}, contentDirectory: {}",
                new Object[]{serviceName(), port, contextPath, contentDirectory});

        this.httpPort = port;
        this.contextPath = isEmpty(contextPath) ? "/" : contextPath;
        this.contentDirectory = contentDirectory;
        this.guiceFilter = guiceFilter;

        LOGGER.debug("instantiated {} with httpPort: {}", new Object[]{serviceName(), port});
    }

    private static ResourceHandler makeContentHandler(String contentDirectory) {
        ResourceHandler resourceHandler = new ResourceHandler();

        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setResourceBase(contentDirectory);

        return resourceHandler;
    }

    /**
     * @throws Exception
     */

    @Override
    protected void startUp() throws Exception {
        LOGGER.info("starting {}", serviceName());

        if (server != null) {
            LOGGER.error("Server is already stopped");
            return;
        } else {
            server = new Server(httpPort);
        }

        HandlerCollection handlers = new HandlerCollection(false);

        handlers.addHandler(makeMonitorHandler());

        if (contentDirectory != null) {
            handlers.addHandler(makeContentHandler(contentDirectory));
        }

        handlers.addHandler(makeContextHandler(guiceFilter));
        handlers.addHandler(makeRequestLogHandler());
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

    private RequestLogHandler makeRequestLogHandler() {
        RequestLogImpl requestLogImpl = new RequestLogImpl();
        JoranConfigurator configurator = new JoranConfigurator();

        try (InputStream in = getClass().getResourceAsStream("/logback-access.xml")) {
            configurator.setContext(requestLogImpl);
            configurator.doConfigure(in);
        } catch (JoranException | IOException je) {
            throw new HttpServiceException("unable to start request log hander", je);
        }

        RequestLogHandler requestLogHandler = new RequestLogHandler();

        requestLogHandler.setRequestLog(requestLogImpl);

        return requestLogHandler;
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
            server.stop();
            server = null;
            LOGGER.debug("stopped {}", serviceName());
        }
    }
}
