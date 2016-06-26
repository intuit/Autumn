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

package com.intuit.autumn.client.impl;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.intuit.autumn.client.HttpCallConfig;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING;
import static java.lang.Boolean.TRUE;
import static javax.ws.rs.HttpMethod.*;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mortbay.jetty.HttpHeaders.ACCEPT;
import static org.mortbay.jetty.HttpHeaders.CONTENT_TYPE;
import static org.mortbay.jetty.HttpStatus.*;

// FIXME: merge with HttpCallImplTest
@RunWith(MockitoJUnitRunner.class)
public class HttpCallImplWithConnectionPoolingTest extends TestCase {

    private int port = 9876;
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(port);
    private HttpCallImplWithConnectionPooling<ClientResponse> httpCallsImpl;
    private Client client;
    private Map<String, String> map;
    private String accept;
    private String type;
    private String url = "http://localhost:" + port + "/test";

    @Before
    public void setup() {
        httpCallsImpl = new HttpCallImplWithConnectionPooling<>();
        ClientConfig clientConfig = new DefaultClientConfig();

        clientConfig.getFeatures().put(FEATURE_POJO_MAPPING, TRUE);

        this.client = Client.create(clientConfig);
        map = new HashMap<>();

        map.put("foo", "bar");
        map.put("dead", "beef");

        accept = APPLICATION_JSON;
        type = APPLICATION_JSON;
    }

    @Test
    public void testMakeRequest() throws Exception {
        try {
            httpCallsImpl.makeRequest(OPTIONS, url, null, ClientResponse.class, map, map, accept, type, ORDINAL_200_OK);
            fail();
        } catch (UnsupportedOperationException ignored) {
        }

        try {
            httpCallsImpl.makeRequest(HEAD, url, null, ClientResponse.class, map, map, accept, type, ORDINAL_200_OK);
            fail();
        } catch (UnsupportedOperationException ignored) {
        }

        stubFor(get(urlEqualTo("/test"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader(CONTENT_TYPE, equalTo(type))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        ClientResponse response = httpCallsImpl.makeRequest(GET, url, null, ClientResponse.class, map, null, accept, type,
                ORDINAL_200_OK);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));

        try {
            httpCallsImpl.makeRequest(GET, url, null, ClientResponse.class, map, null, accept, type,
                    ORDINAL_201_Created);
            fail();
        } catch (RuntimeException ignored) {
        }

        response = httpCallsImpl.makeRequest(GET, url, null, null, null, null, accept, type, ORDINAL_404_Not_Found);

        assertThat(response.getStatus(), is(ORDINAL_404_Not_Found));
    }

    @Test
    public void testMakeRequestGet() throws Exception {
        stubFor(get(urlEqualTo("/test"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader(CONTENT_TYPE, equalTo(type))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        ClientResponse response = httpCallsImpl.makeRequest(GET, url, null, ClientResponse.class, map, null, accept, type,
                ORDINAL_200_OK);
        assertThat(response.getStatus(), is(ORDINAL_200_OK));

        try {
            httpCallsImpl.makeRequest(GET, url, null, null, map, null, accept, type, ORDINAL_201_Created);
            fail();
        } catch (RuntimeException ignored) {
        }

        response = httpCallsImpl.makeRequest(GET, url, null, ClientResponse.class, null, null, accept, type,
                ORDINAL_404_Not_Found);

        assertThat(response.getStatus(), is(ORDINAL_404_Not_Found));
    }

    @Test
    public void testMakeRequestPost() throws Exception {
        stubFor(post(urlEqualTo("/test"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader(CONTENT_TYPE, equalTo(type))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        ClientResponse response = httpCallsImpl.makeRequest(POST, url, null, ClientResponse.class, map, null, accept, type,
                ORDINAL_200_OK);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));
    }

    @Test
    public void testMakeRequestPut() throws Exception {
        stubFor(put(urlEqualTo("/test"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader(CONTENT_TYPE, equalTo(type))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        ClientResponse response = httpCallsImpl.makeRequest(PUT, url, null, ClientResponse.class, map, null, accept, type,
                ORDINAL_200_OK);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));
    }

    @Test
    public void testMakeRequestDelete() throws Exception {
        stubFor(delete(urlEqualTo("/test"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader(CONTENT_TYPE, equalTo(type))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        ClientResponse response = httpCallsImpl.makeRequest(DELETE, url, null, ClientResponse.class, map, null, accept, type,
                ORDINAL_200_OK);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));
    }

    @Test
    public void testAddHeaders() throws Exception {
        WebResource webResource = client.resource(url);

        WebResource.Builder webResourceFromMethod = httpCallsImpl.addHeaders(webResource, map, accept, type);

        stubFor(get(urlEqualTo("/test"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader(CONTENT_TYPE, equalTo(type))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        ClientResponse x = webResourceFromMethod.get(ClientResponse.class);

        assertThat(x.getStatus(), is(ORDINAL_200_OK));
    }

    @Test
    public void testAddQueryParams() throws Exception {
        WebResource webResource = client.resource(url);
        WebResource response = httpCallsImpl.addQueryParams(webResource, map);

        webResource = webResource.queryParam("foo", map.get("foo"));
        webResource = webResource.queryParam("dead", map.get("dead"));

        assertThat(webResource, is(response));
    }

    @Test
    public void testDoGet() throws Exception {
        stubFor(get(urlEqualTo("/test"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        ClientResponse response = httpCallsImpl.doGet(url, ClientResponse.class, map, null, accept, ORDINAL_200_OK);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));

//        try {
//            response = httpCallsImpl.doGet(url, null, map, null, accept, ORDINAL_200_OK);
//            fail();
//        } catch (RuntimeException ignored) {
//        }

        response = httpCallsImpl.doGet(url, ClientResponse.class, null, null, accept, ORDINAL_404_Not_Found);

        assertThat(response.getStatus(), is(ORDINAL_404_Not_Found));
    }

    @Test
    public void testDoGetWithHttpConfig() throws Exception {
        stubFor(get(urlEqualTo("/test"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        HttpCallConfig httpCallConfig = HttpCallConfig.Builder.aHttpCallConfig()
                .withUrl(url)
                .withToMap(ClientResponse.class)
                .withHeaders(map)
                .withAccept(accept)
                .withExpectedStatus(ORDINAL_200_OK).build();
        ClientResponse response = httpCallsImpl.doGet(httpCallConfig);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));
    }

    @Test
    public void testDoPost() throws Exception {
        stubFor(post(urlEqualTo("/test"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader(CONTENT_TYPE, equalTo(type))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        ClientResponse response = httpCallsImpl.doPost(url, null, ClientResponse.class, map, null, accept, type,
                ORDINAL_200_OK);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));
    }

    @Test
    public void testDoPostWithEntity() throws Exception {
        stubFor(post(urlEqualTo("/test"))
                .withRequestBody(matching("entity"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader(CONTENT_TYPE, equalTo(type))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        ClientResponse response = httpCallsImpl.doPost(url, "entity", ClientResponse.class, map, null, accept, type,
                ORDINAL_200_OK);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));
    }

    @Ignore("???")
    @Test
    public void testDoPostWithHttpConfig() throws Exception {
        stubFor(put(urlEqualTo("/test"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader(CONTENT_TYPE, equalTo(type))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        HttpCallConfig httpCallConfig = HttpCallConfig.Builder.aHttpCallConfig()
                .withUrl(url)
                .withData(null)
                .withToMap(ClientResponse.class)
                .withHeaders(map)
                .withQueryParams(null)
                .withAccept(accept)
                .withType(type)
                .withExpectedStatus(ORDINAL_200_OK).build();
        ClientResponse response = httpCallsImpl.doPost(httpCallConfig);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));
    }

    @Ignore("???")
    @Test
    public void testDoPostWithEntityWithHttpConfig() throws Exception {
        stubFor(put(urlEqualTo("/test"))
                .withRequestBody(matching("entity"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader(CONTENT_TYPE, equalTo(type))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        HttpCallConfig httpCallConfig = HttpCallConfig.Builder.aHttpCallConfig()
                .withUrl(url)
                .withData("entity")
                .withToMap(ClientResponse.class)
                .withHeaders(map)
                .withQueryParams(null)
                .withAccept(accept)
                .withType(type)
                .withExpectedStatus(ORDINAL_200_OK).build();
        ClientResponse response = httpCallsImpl.doPost(httpCallConfig);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));
    }

    @Test
    public void testDoPut() throws Exception {
        stubFor(put(urlEqualTo("/test"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader(CONTENT_TYPE, equalTo(type))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        ClientResponse response = httpCallsImpl.doPut(url, null, ClientResponse.class, map, null, accept, type,
                ORDINAL_200_OK);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));
    }

    @Test
    public void testDoPutWithEntity() throws Exception {
        stubFor(put(urlEqualTo("/test"))
                .withRequestBody(matching("entity"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader(CONTENT_TYPE, equalTo(type))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        ClientResponse response = httpCallsImpl.doPut(url, "entity", ClientResponse.class, map, null, accept, type,
                ORDINAL_200_OK);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));
    }

    @Test
    public void testDoPutWithHttpConfig() throws Exception {
        stubFor(put(urlEqualTo("/test"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader(CONTENT_TYPE, equalTo(type))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        HttpCallConfig httpCallConfig = HttpCallConfig.Builder.aHttpCallConfig()
                .withUrl(url)
                .withToMap(ClientResponse.class)
                .withHeaders(map)
                .withAccept(accept)
                .withType(type)
                .withExpectedStatus(ORDINAL_200_OK).build();
        ClientResponse response = httpCallsImpl.doPut(httpCallConfig);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));
    }

    @Test
    public void testDoPutWithEntityWithHttpConfig() throws Exception {
        stubFor(put(urlEqualTo("/test"))
                .withRequestBody(matching("entity"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader(CONTENT_TYPE, equalTo(type))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        String entity = "entity";
        HttpCallConfig httpCallConfig = HttpCallConfig.Builder.aHttpCallConfig()
                .withUrl(url)
                .withData(entity)
                .withToMap(ClientResponse.class)
                .withHeaders(map)
                .withAccept(accept)
                .withType(type)
                .withExpectedStatus(ORDINAL_200_OK).build();
        ClientResponse response = httpCallsImpl.doPut(httpCallConfig);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));
    }

    @Test
    public void testDoDelete() throws Exception {
        stubFor(delete(urlEqualTo("/test"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        ClientResponse response = httpCallsImpl.doDelete(url, ClientResponse.class, map, null, accept, ORDINAL_200_OK);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));
    }

    @Test
    public void testDoDeleteWithHttpConfig() throws Exception {
        stubFor(delete(urlEqualTo("/test"))
                .withHeader(ACCEPT, equalTo(accept))
                .withHeader("foo", equalTo(map.get("foo")))
                .withHeader("dead", equalTo(map.get("dead")))
                .willReturn(aResponse().withStatus(ORDINAL_200_OK)));

        HttpCallConfig httpCallConfig = HttpCallConfig.Builder.aHttpCallConfig()
                .withUrl(url)
                .withToMap(ClientResponse.class)
                .withHeaders(map)
                .withAccept(accept)
                .withExpectedStatus(ORDINAL_200_OK).build();
        ClientResponse response = httpCallsImpl.doDelete(httpCallConfig);

        assertThat(response.getStatus(), is(ORDINAL_200_OK));
    }

}
