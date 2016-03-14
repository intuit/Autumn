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

import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.Scheduler;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileNotFoundException;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.powermock.api.easymock.PowerMock.*;
import static org.powermock.reflect.Whitebox.setInternalState;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpsService.class)
public class HttpsServiceTest {

    @Mock
    private GuiceFilter guiceFilter;
    @Mock
    private ThreadPool threadPool;
    @Mock
    private Scheduler scheduler;
    @Mock
    private ByteBufferPool byteBufferPool;

    @Test
    public void instantiatable() throws Exception {
        new HttpsService(8443, 500000, 32768, "./autumn-keystore", "ThisIsATest", "ThisIsAnotherTest", "/", guiceFilter);
    }

    @Test
    public void startable() throws Exception {
        Server server = createMockAndExpectNew(Server.class);

        setInternalState(server, "_lock", new Object());
        setInternalState(server, "_state", 2);
        expect(server.getThreadPool()).andReturn(threadPool);
        expect(server.getBean(Scheduler.class)).andReturn(scheduler);
        expect(server.getBean(ByteBufferPool.class)).andReturn(byteBufferPool);
        server.setConnectors(isA(Connector[].class));
        server.setHandler(isA(Handler.class));
        server.start();
        replay(server, Server.class);

        String path = "/foo/bar/bop.txt";
        File file = createMockAndExpectNew(File.class, path);

        expect(file.exists()).andReturn(true);
        expect(file.getAbsolutePath()).andReturn(path);
        replay(file, File.class);

        HttpsService httpsService = new HttpsService(8443, 500000, 32768, path, "ThisIsATest", "ThisIsAnotherTest",
                "/", guiceFilter);

        httpsService.startUp();

        verify(server, Server.class);
        verify(file, File.class);
    }

    @Test
    public void startableWithEmptyContextPath() throws Exception {
        Server server = createMockAndExpectNew(Server.class);

        setInternalState(server, "_lock", new Object());
        setInternalState(server, "_state", 2);
        expect(server.getThreadPool()).andReturn(threadPool);
        expect(server.getBean(Scheduler.class)).andReturn(scheduler);
        expect(server.getBean(ByteBufferPool.class)).andReturn(byteBufferPool);
        server.setConnectors(isA(Connector[].class));
        server.setHandler(isA(Handler.class));
        server.start();
        replay(server, Server.class);

        String path = "/foo/bar/bop.txt";
        File file = createMockAndExpectNew(File.class, path);

        expect(file.exists()).andReturn(true);
        expect(file.getAbsolutePath()).andReturn(path);
        replay(file, File.class);

        HttpsService httpsService = new HttpsService(8443, 500000, 32768, path, "ThisIsATest", "ThisIsAnotherTest",
                "", guiceFilter);

        httpsService.startUp();

        verify(server, Server.class);
        verify(file, File.class);
    }

    @Test
    public void notStartableAlreadyStarted() throws Exception {
        Server server = createMock(Server.class);
        String path = "/foo/bar/bop.txt";

        HttpsService httpsService = new HttpsService(8443, 500000, 32768, path, "ThisIsATest", "ThisIsAnotherTest",
                "/", guiceFilter);

        setInternalState(httpsService, "server", server);
        replay(server, Server.class);

        httpsService.startUp();

        verify(server, Server.class);
    }

    @Test(expected = FileNotFoundException.class)
    public void notStartableKeyStoreDoesNotExist() throws Exception {
        Server server = createMockAndExpectNew(Server.class);

        setInternalState(server, "_lock", new Object());
        setInternalState(server, "_state", 2);
        expect(server.getThreadPool()).andReturn(threadPool);
        expect(server.getBean(Scheduler.class)).andReturn(scheduler);
        expect(server.getBean(ByteBufferPool.class)).andReturn(byteBufferPool);
        server.setConnectors(isA(Connector[].class));
        server.setHandler(isA(Handler.class));
        server.start();
        replay(server, Server.class);

        String path = "/foo/bar/bop.txt";
        File file = createMockAndExpectNew(File.class, path);

        expect(file.exists()).andReturn(false);
        expect(file.getAbsolutePath()).andReturn(path);
        replay(file, File.class);

        HttpsService httpsService = new HttpsService(8443, 500000, 32768, path, "ThisIsATest", "ThisIsAnotherTest",
                "/", guiceFilter);

        httpsService.startUp();

        verify(server, Server.class);
        verify(file, File.class);
    }

    @Test
    public void stopable() throws Exception {
        Server server = createMockAndExpectNew(Server.class);
        String path = "/foo/bar/bop.txt";

        HttpsService httpsService = new HttpsService(8443, 500000, 32768, path, "ThisIsATest", "ThisIsAnotherTest",
                "/", guiceFilter);

        setInternalState(server, "_lock", new Object());
        setInternalState(httpsService, "server", server);
        expect(server.getConnectors()).andReturn(new Connector[]{});
        setInternalState(server, "_state", 3);
        server.stop();
        replay(server, Server.class);

        httpsService.shutDown();
    }

    @Test
    public void stopableWithConnectors() throws Exception {
        Server server = createMockAndExpectNew(Server.class);
        Connector connector = createMock(Connector.class);
        String path = "/foo/bar/bop.txt";

        HttpsService httpsService = new HttpsService(8443, 500000, 32768, path, "ThisIsATest", "ThisIsAnotherTest",
                "/", guiceFilter);

        setInternalState(server, "_lock", new Object());
        setInternalState(httpsService, "server", server);
        expect(server.getConnectors()).andReturn(new Connector[]{connector});
        connector.stop();
        setInternalState(server, "_state", 3);
        server.stop();
        replay(server, Server.class);
        replay(connector, Connector.class);

        httpsService.shutDown();
    }

    @Test
    public void notStopableAlreadyStarted() throws Exception {
        Server server = createMock(Server.class);
        String path = "/foo/bar/bop.txt";

        HttpsService httpsService = new HttpsService(8443, 500000, 32768, path, "ThisIsATest", "ThisIsAnotherTest",
                "/", guiceFilter);

        replay(server, Server.class);

        httpsService.shutDown();

        verify(server, Server.class);
    }
}

