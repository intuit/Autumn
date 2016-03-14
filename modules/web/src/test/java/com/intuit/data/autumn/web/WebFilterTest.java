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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WebFilter.class, InputStreamHttpServletRequestWrapper.class, HttpServletRequestWrapper.class})
public class WebFilterTest {

    private WebFilter webFilter;
    @Mock
    private ServletResponse servletResponse;
    @Mock
    private FilterChain filterChain;
    @Mock
    private InputStreamHttpServletRequestWrapper inputStreamHttpServletRequestWrapper;
    @Mock
    private FilterConfig filterConfig;

    @Before
    public void initialize() {
        webFilter = new WebFilter();
    }

    @Test
    public void testInit() throws ServletException {
        webFilter.init(filterConfig);

        assertThat(webFilter.getFilterConfig(), is(filterConfig));
    }

    @Test
    public void testDestroy() {
        webFilter.destroy();

        assertThat(webFilter.getFilterConfig(), is(nullValue()));
    }

    @Test
    public void testRequestContentTypeNull() throws Exception {
        ServletRequest servletRequest = mock(ServletRequest.class, withSettings().extraInterfaces(HttpServletRequest.class));
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        when(httpServletRequest.getContentType()).thenReturn(null);

        webFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(filterChain).doFilter(servletRequest, servletResponse);
    }

    @Test
    public void testDoFilter() throws Exception {
        ServletRequest servletRequest = mock(ServletRequest.class, withSettings().extraInterfaces(HttpServletRequest.class));
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        when(httpServletRequest.getContentType()).thenReturn("application/json");

        webFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(filterChain).doFilter(servletRequest, servletResponse);
    }

    @Test
    public void testDoFilterWrapper() throws Exception {
        ServletRequest servletRequest = mock(ServletRequest.class, withSettings().extraInterfaces(HttpServletRequest.class));
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        when(httpServletRequest.getContentType()).thenReturn("application/x-www-form-urlencoded");
        whenNew(InputStreamHttpServletRequestWrapper.class).withArguments(any(HttpServletRequest.class)).
                thenReturn(inputStreamHttpServletRequestWrapper);

        webFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(filterChain).doFilter(inputStreamHttpServletRequestWrapper, servletResponse);
    }
}
