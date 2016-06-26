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

package com.intuit.autumn.view;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.google.common.cache.CacheBuilder.newBuilder;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.*;
import static javax.ws.rs.core.Response.status;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.slf4j.LoggerFactory.getLogger;


/**
 * A content cache provider.
 */

// todo: allow prefix to vary
@Path("/view")
public class Resource {

    private static final Map<String, String> MIME = new ImmutableMap.Builder<String, String>()
            .put("css", "text/css")
            .put("gif", "image/gif")
            .put("html", TEXT_HTML)
            .put("js", "application/javascript")
            .put("png", "image/png")
            .put("txt", TEXT_PLAIN)
            .build();
    private static final Logger LOGGER = getLogger(Resource.class);
    private final LoadingCache<String, byte[]> cache;

    /**
     * Constructor with configurable state.
     *
     * @param expiryTimeInSeconds time with which resources will be expunged from cache
     */

    @Inject
    public Resource(@Named("resource.expiryTimeInSeconds") final int expiryTimeInSeconds) {
        LOGGER.debug("resource.expiryTimeInSeconds: {}", expiryTimeInSeconds);

        cache = newBuilder()
                .expireAfterWrite(expiryTimeInSeconds, SECONDS)
                .build(new CacheLoader<String, byte[]>() {
                    @Override
                    public byte[] load(String key) throws IOException {
                        return getResource(key);
                    }
                });
    }

    /**
     * Return the index resource.
     *
     * @param resource resource name
     * @param subtype  resource type
     * @return associated response
     */

    @GET
    @Path("/{resource}.{subtype}")
    public Response index(@PathParam("resource") final String resource, @PathParam("subtype") final String subtype) {
        return resource(subtype, resource, subtype);
    }

    /**
     * Return a typed resource.
     *
     * @param type     resource type
     * @param resource resource name
     * @param subtype  resource subtype
     * @return associated response
     */

    @GET
    @Path("/{type}/{resource}.{subtype}")
    public Response resource(@PathParam("type") final String type, @PathParam("resource") final String resource,
                             @PathParam("subtype") final String subtype) {
        LOGGER.debug("loading type: {}, resource: {}, subtype: {}", new Object[]{type, resource, subtype});

        try {
            requireNonNull(type);
            requireNonNull(resource);
            requireNonNull(subtype);
        } catch (NullPointerException e) {
            String message = "invalid resource request";

            LOGGER.debug("{} type: {}, resource: {}, subtype: {}",
                    asList(message, type, resource, subtype).toArray(), e);

            return status(BAD_REQUEST).entity("incomplete resource request").build();
        }

        StringBuilder derivedResource = new StringBuilder(resource);
        String derivedSubtype = subtype;

        if (derivedSubtype.contains(".")) {
            int i = derivedSubtype.lastIndexOf(".");

            derivedResource.append(".").append(derivedSubtype.substring(0, i));

            derivedSubtype = subtype.substring(i + 1);
        }

        String resourcePath = format("/%s/%s.%s", type, derivedResource, derivedSubtype);

        return toResponse(resourcePath, MIME.get(derivedSubtype));
    }

    private Response toResponse(final String resource, final String type) {
        byte[] content = null;
        String cause = null;

        try {
            content = cache.get(resource);
        } catch (ExecutionException | UncheckedExecutionException | CacheLoader.InvalidCacheLoadException e) {
            cause = e.getMessage();

            LOGGER.debug("unable to load resource: {}, type: {}, cause: {}",
                    asList(resource, type, e.getMessage()).toArray(), e);
        }
        return content != null ?
                status(OK).type(type).entity(content).build() :
                status(INTERNAL_SERVER_ERROR)
                        .entity("unable to load content, cause: " + cause)
                        .type(TEXT_PLAIN)
                        .build();
    }

    private byte[] getResource(final String resource) throws IOException {
        byte[] content;

        try (InputStream is = getClass().getResourceAsStream(resource)) {
            content = toByteArray(is);
        } catch (IOException e) {
            LOGGER.error("get resource: {}, cause: {}", resource, e.getMessage());

            throw e;
        }

        return content;
    }
}
