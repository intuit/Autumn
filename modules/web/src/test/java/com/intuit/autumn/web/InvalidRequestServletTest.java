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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.PrintWriter;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvalidRequestServletTest {

    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private PrintWriter printWriter;
    private InvalidRequestServlet invalidRequestServlet;

    @Before
    public void before() {
        invalidRequestServlet = new InvalidRequestServlet();
    }

    @Test
    public void testService() throws Exception {
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        invalidRequestServlet.service(httpServletRequest, httpServletResponse);

        verify(httpServletResponse).setStatus(SC_NOT_FOUND);
        verify(httpServletResponse).setContentType(MediaType.TEXT_PLAIN);
        verify(httpServletResponse).setContentType(UTF_8.name());
        verify(printWriter).append(Integer.toString(SC_NOT_FOUND));
    }
}
