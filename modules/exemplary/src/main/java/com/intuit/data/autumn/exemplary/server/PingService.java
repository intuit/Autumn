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

package com.intuit.data.autumn.exemplary.server;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.intuit.data.autumn.exemplary.data.Ping;
import com.intuit.data.autumn.web.HttpHeader;
import org.slf4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Joiner.on;
import static java.util.Arrays.asList;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.status;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Prototypical application endpoint implementation.
 */

@Provider
@Path("/proto")
public class PingService {

    private static final Joiner JOINER = on(",");
    private final HttpHeader httpHeader;
    private final Counter getPingCounter;
    private final Timer getPingTimer;
    private final Counter postPingCounter;
    private final Counter getPingsCounter;
    private final Timer getPingsTimer;
    private static final Logger LOGGER = getLogger(PingService.class);

    /**
     * Instantiation method.
     *
     * @param metricRegistry operational metrics registry
     */

    @Inject
    public PingService(final HttpHeader httpHeader, final MetricRegistry metricRegistry) {
        this.httpHeader = httpHeader;
        getPingCounter = metricRegistry.counter("get-ping-counter");
        getPingTimer = metricRegistry.timer("get-ping-timer");
        postPingCounter = metricRegistry.counter("post-ping-counter");
        getPingsCounter = metricRegistry.counter("get-pings-counter");
        getPingsTimer = metricRegistry.timer("get-pings-timer");
    }

    /**
     * Object reader, retrieving the object associated with the provided ID.
     *
     * @param id object ID
     * @return representative object
     * @throws Exception
     */

    @GET
    @Path("/ping/{id}")
    @Produces(APPLICATION_JSON)
    public Response ping(@PathParam("id") String id) throws Exception {
        getPingCounter.inc();
        LOGGER.debug("getting ping id: {}, count: {}", id, getPingCounter.getCount());

        Ping ping;
        Timer.Context timer = getPingTimer.time();

        try {
            UUID pid = fromString(id);

            ping = new Ping(pid.toString(), "message:" + pid);
        } finally {
            timer.stop();
        }

        LOGGER.debug("got ping id: {}, count: {}, ping: {}", new Object[]{id, getPingCounter.getCount(), ping});

        return httpHeader.headers().entity(ping).build();
    }

    /**
     * Object writer, synthesizing persisting of a materialized object.
     *
     * @param ping object to be persisted
     * @return instance JSON representation
     */

    @POST
    @Path("/ping")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response ping(Ping ping) {
        postPingCounter.inc();
        LOGGER.debug("posting ping: {}, count: {}", ping, postPingCounter.getCount());

        Ping pong = ping;

        LOGGER.debug("posted ping: {}, count: {}, pong: {}", new Object[]{ping, postPingCounter.getCount(), pong});

        return httpHeader.headers().entity(pong).build();
    }

    /**
     * Object collection reader, retrieving all the available objects.
     *
     * @return a collection of objects
     * @throws Exception
     */

    @GET
    @Path("/pings")
    @Produces(APPLICATION_JSON)
    public Response pings() throws Exception {
        getPingsCounter.inc();
        LOGGER.debug("getting pings count: {}", getPingsCounter.getCount());

        List<Ping> pings;
        Timer.Context timer = getPingsTimer.time();

        try {
            UUID id1 = randomUUID();
            UUID id2 = randomUUID();

            pings = asList(new Ping(id1.toString(), "message:" + id1), new Ping(id2.toString(), "message:" + id2));
        } finally {
            timer.stop();
        }

        LOGGER.debug("got pings count: {}, pong: {}", getPingsCounter.getCount(), JOINER.join(pings));

        return httpHeader.headers().entity(pings).build();
    }
}
