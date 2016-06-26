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

package com.intuit.autumn.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

/**
 * Trivial Ping response.
 */

public class PingServlet extends HttpServlet {

    /**
     * Ping service.
     *
     * @param httpServletRequest request
     * @param httpServletResponse response
     * @throws ServletException unintended exception
     * @throws IOException unintended exception
     */

    @Override
    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ServletException, IOException {
        httpServletResponse.setStatus(SC_OK);
        httpServletResponse.setContentType(TEXT_PLAIN);
        httpServletResponse.getWriter().append(Integer.toString(SC_OK));
    }
}
