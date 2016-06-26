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

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * A filter that allows for processing of CONTENT-TYPE free messages.
 */

public class WebFilter implements Filter {

    private FilterConfig filterConfig;

    /**
     * Initialization implementation.
     *
     * @param filterConfig filter configuration
     * @throws ServletException unintended exception
     */

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    /**
     * Filter that injects a request wrapper in the event the CONTENT-TYPE was not provided.
     *
     * @param request request
     * @param response response
     * @param chain filter chain
     * @throws IOException unintended exception
     * @throws ServletException unintended exception
     */

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;

        // if content-type not set pass on InputStreamHttpServletRequestWrapper object default
        // content-type = application/x-www-form-urlencoded

        if ("application/x-www-form-urlencoded".equals(httpRequest.getContentType())) {
            httpRequest = new InputStreamHttpServletRequestWrapper(httpRequest);
        }

        chain.doFilter(httpRequest, response);
    }

    /**
     * Destroy implementation
     */

    @Override
    public void destroy() {
        filterConfig = null;
    }

    /**
     * Retrieve the associated filter configuration.
     *
     * @return filter configuration
     */

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }
}
