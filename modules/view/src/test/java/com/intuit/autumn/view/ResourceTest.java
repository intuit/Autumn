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

import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ResourceTest {

    @Test
    public void textIndex() throws Exception {
        Resource resource = new Resource(5);
        Response response = resource.index("foo", "txt");

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        assertThat(new String((byte[]) response.getEntity()), is("ello ello"));
        assertThat(response.getMetadata().getFirst("Content-Type").toString(), is(TEXT_PLAIN));
    }

    @Test
    public void missingIndex() throws Exception {
        Resource resource = new Resource(5);
        Response response = resource.index("foo", "bar");

        assertThat(response.getStatus(), is(INTERNAL_SERVER_ERROR.getStatusCode()));
        assertThat((String) response.getEntity(), startsWith("unable to load content, cause: "));
        assertThat(response.getMetadata().getFirst("Content-Type").toString(), is(TEXT_PLAIN));
    }

    @Test
    public void cssIndex() throws Exception {
        Resource resource = new Resource(5);
        Response response = resource.index("jquery-ui", "css");

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        assertThat(response.getEntity(), notNullValue());
        assertThat(response.getMetadata().getFirst("Content-Type").toString(), is("text/css"));
    }

    @Test
    public void imageResource() throws Exception {
        Resource resource = new Resource(5);
        Response response = resource.resource("images", "loading", "gif");

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        assertThat(response.getEntity(), notNullValue());
        assertThat(response.getMetadata().getFirst("Content-Type").toString(), is("image/gif"));
    }

    @Test
    public void jsResource() throws Exception {
        Resource resource = new Resource(5);
        Response response = resource.index("jquery.min", "js");

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        assertThat(response.getEntity(), notNullValue());
        assertThat(response.getMetadata().getFirst("Content-Type").toString(), is("application/javascript"));
    }

    @Test
    public void textResource() throws Exception {
        Resource resource = new Resource(5);
        Response response = resource.resource("txt", "foo", "txt");

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        assertThat(new String((byte[]) response.getEntity()), is("ello ello"));
        assertThat(response.getMetadata().getFirst("Content-Type").toString(), is(TEXT_PLAIN));
    }

    @Test
    public void missingResourceWithSubtype() throws Exception {
        Resource resource = new Resource(5);
        Response response = resource.resource("foo", "bar", "bop");

        assertThat(response.getStatus(), is(INTERNAL_SERVER_ERROR.getStatusCode()));
        assertThat((String) response.getEntity(), startsWith("unable to load content, cause: "));
        assertThat(response.getMetadata().getFirst("Content-Type").toString(), is(TEXT_PLAIN));
    }

    @Test
    public void nullType() throws Exception {
        Resource resource = new Resource(5);
        Response response = resource.resource(null, "bar", "bop");

        assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
        assertThat((String) response.getEntity(), is("incomplete resource request"));
    }

    @Test
    public void subtypeWithDot() throws Exception {
        Resource resource = new Resource(5);
        Response response = resource.resource("txt", "foo", "bar.txt");

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        assertThat(new String((byte[]) response.getEntity()), is("ello ello"));
        assertThat(response.getMetadata().getFirst("Content-Type").toString(), is(TEXT_PLAIN));
    }
}
