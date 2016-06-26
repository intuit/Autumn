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

package com.intuit.autumn.exemplary.client;

import com.intuit.autumn.exemplary.data.Ping;
import com.intuit.autumn.utils.PropertyFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static java.lang.System.out;
import static java.util.UUID.randomUUID;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.UriBuilder.fromUri;

/**
 * Prototypical HTTP/S client implementation.
 */

public class PingClient {

    private static final String context;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final URI pingService = URI.create("http://localhost:8080" + context + "/proto");

    static {
        String applicationContextPath = PropertyFactory.create("/web.properties", PingClient.class)
                .getProperty("application.http.context.path");

        context = applicationContextPath.equals("/") ? "" : applicationContextPath;
    }

    /**
     * Application entry point.
     *
     * @param args application arguments
     * @throws IOException unintended exception
     */

    public static void main(String[] args) throws IOException {
        Client client = Client.create();
        UUID id = randomUUID();
        WebResource resource = client.resource(fromUri(pingService).path("ping").path(id.toString()).build());

        out.println("GET: " + resource);

        ClientResponse response = resource.accept(APPLICATION_JSON).get(ClientResponse.class);
        Ping ping = mapper.readValue(response.getEntity(String.class), Ping.class);

        out.println("GOT: resource: " + resource + ", status: " + response.getStatus() + ", content: " +
                ping.toString());

        resource = client.resource(fromUri(pingService).path("pings").build());

        out.println("GET: " + resource);

        response = resource.accept(APPLICATION_JSON).get(ClientResponse.class);

        List<Ping> pings = mapper.readValue(response.getEntity(String.class), new TypeReference<List<Ping>>() {
        });

        out.println("GOT: resource: " + resource + ", status: " + response.getStatus() + ", content: " + pings);

        resource = client.resource(fromUri(pingService).path("ping").build());
        id = randomUUID();

        String content = pingToString(new Ping(id.toString(), "bar:" + id.toString()));

        out.println("POST: " + resource + ", content: " + content);

        response = resource.type(APPLICATION_JSON).post(ClientResponse.class, content);

        out.println("POSTED: resource: " + resource + ", status: " + response.getStatus() + ", content: " +
                response.getEntity(String.class));
    }

    private static String pingToString(Ping data) throws IOException {
        return mapper.writeValueAsString(data);
    }
}
