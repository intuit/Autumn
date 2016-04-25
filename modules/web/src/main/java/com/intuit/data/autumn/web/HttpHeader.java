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

import com.google.inject.Inject;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

import static com.google.common.net.HttpHeaders.*;
import static java.lang.Boolean.TRUE;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.status;

public class HttpHeader {

    private final CacheControl cacheControl;

    @Inject
    public HttpHeader() {
        cacheControl = new CacheControl();

        cacheControl.setPrivate(TRUE);
        cacheControl.setNoCache(TRUE);
    }

    public Response.ResponseBuilder headers() {
        return headers(OK);
    }

    public Response.ResponseBuilder headers(Response.Status status) {
        return headers(status.getStatusCode());
    }

    public Response.ResponseBuilder headers(int status) {
        return status(status)
                .cacheControl(cacheControl)
                .header(ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                .header(ACCESS_CONTROL_ALLOW_HEADERS, "Authorization,X-Forwarded-For,Accept-Language,Content-Type")
                .header(ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,OPTIONS")
                .header(ACCESS_CONTROL_REQUEST_METHOD, "GET,POST,OPTIONS");
//                .header("X-Application-Id", applicationName);
    }
}