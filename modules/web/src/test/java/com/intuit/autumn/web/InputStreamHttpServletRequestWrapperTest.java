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

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IOUtils.class, HttpServletRequest.class, ServletInputStream.class, InputStreamHttpServletRequestWrapper.class})
public class InputStreamHttpServletRequestWrapperTest {

    private InputStreamHttpServletRequestWrapper inputStreamHttpServletRequestWrapper;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private ServletInputStream servletInputStream;
    @Mock
    private ByteArrayInputStream byteArrayInputStream;
    @Mock
    private ReadListener readListener;
    private byte[] body = "test".getBytes();

    @Before
    public void initialize() throws IOException {
        when(httpServletRequest.getInputStream()).thenReturn(servletInputStream);
        stub(method(IOUtils.class, "toByteArray", InputStream.class)).toReturn(body);

        inputStreamHttpServletRequestWrapper = new InputStreamHttpServletRequestWrapper(httpServletRequest);
    }

    @Test
    public void testGetInputStreamContent() throws Exception {
        assertThat(IOUtils.toString(inputStreamHttpServletRequestWrapper.getInputStream()), is("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetInputStreamNullBody() throws IOException {
        when(httpServletRequest.getInputStream()).thenReturn(servletInputStream);
        stub(method(IOUtils.class, "toByteArray", InputStream.class)).toReturn(null);

        inputStreamHttpServletRequestWrapper = new InputStreamHttpServletRequestWrapper(httpServletRequest);

        inputStreamHttpServletRequestWrapper.getInputStream();
    }

    @Test(expected = IOException.class)
    public void testInputStreamIOException() throws IOException {
        when(httpServletRequest.getInputStream()).thenReturn(servletInputStream);
        stub(method(IOUtils.class, "toByteArray", InputStream.class)).toThrow(new IOException("some io exception"));

        inputStreamHttpServletRequestWrapper = new InputStreamHttpServletRequestWrapper(httpServletRequest);
    }

    @Test
    public void testInputStreamIOExceptionDesc() throws Exception {
        when(httpServletRequest.getInputStream()).thenReturn(servletInputStream);
        stub(method(IOUtils.class, "toByteArray", InputStream.class)).toThrow(new IOException("some io exception"));

        try {
            inputStreamHttpServletRequestWrapper = new InputStreamHttpServletRequestWrapper(httpServletRequest);
        } catch (IOException e) {
            assertThat(e.getMessage(), is("some io exception"));
        }
    }

    @Test
    public void testGetInputStreamIsReady() throws IOException {
        ServletInputStream is = inputStreamHttpServletRequestWrapper.getInputStream();

        assertThat(is.isReady(), is(true));
    }

    @Test(expected = RuntimeException.class)
    public void testGetInputStreamSetReadListener() throws IOException {
        ServletInputStream is = inputStreamHttpServletRequestWrapper.getInputStream();

        is.setReadListener(readListener);
    }

    @Test
    public void testGetInputStreamSetReadListenerDetail() throws Exception {
        ServletInputStream is = inputStreamHttpServletRequestWrapper.getInputStream();

        try {
            is.setReadListener(readListener);
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("Not implemented"));
        }
    }

    @Test
    public void testGetInputStreamIsFinishedTrue() throws Exception {
        PowerMockito.whenNew(ByteArrayInputStream.class).withArguments(any()).thenReturn(byteArrayInputStream);
        when(byteArrayInputStream.available()).thenReturn(0);

        ServletInputStream is = inputStreamHttpServletRequestWrapper.getInputStream();

        assertThat(is.isFinished(), is(true));
    }

    @Test
    public void testGetInputStreamIsFinishedFalse() throws Exception {
        PowerMockito.whenNew(ByteArrayInputStream.class).withArguments(any()).thenReturn(byteArrayInputStream);
        when(byteArrayInputStream.available()).thenReturn(-1);

        ServletInputStream is = inputStreamHttpServletRequestWrapper.getInputStream();

        assertThat(is.isFinished(), is(false));
    }

    @Test
    public void testGetInputStreamRead() throws Exception {
        PowerMockito.whenNew(ByteArrayInputStream.class).withArguments(any()).thenReturn(byteArrayInputStream);
        when(byteArrayInputStream.read()).thenReturn(5);

        ServletInputStream is = inputStreamHttpServletRequestWrapper.getInputStream();
        assertThat(is.read(), is(5));
    }
}
