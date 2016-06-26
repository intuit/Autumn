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

import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.powermock.api.easymock.PowerMock.*;
import static org.powermock.reflect.Whitebox.setInternalState;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpService.class, Server.class})
public class HttpServiceTest {

    @Mock
    private Server server;
    @Mock
    private GuiceFilter guiceFilter;

    @After
    public void after() throws Exception {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void instantiatable() throws Exception {
        new HttpService(8080, "/", null, guiceFilter);
    }

    @Test
    public void startable() throws Exception {
        int port = 8080;
        Server server = createMockAndExpectNew(Server.class, port);

        server.setHandler(isA(HandlerCollection.class));
        server.start();
        replay(server, Server.class);

        HttpService httpService = new HttpService(port, "/", null, guiceFilter);

        httpService.startUp();

        verify(server, Server.class);
    }

    @Test
    public void startableAndStopableWithEmptyContextPath() throws Exception {
        int port = 8080;
        Server server = createMockAndExpectNew(Server.class, port);

        server.setHandler(isA(HandlerCollection.class));
        server.start();
        replay(server, Server.class);

        HttpService httpService = new HttpService(port, "", null, guiceFilter);

        httpService.startUp();

        verify(server, Server.class);
    }

    @Test
    public void notStartableAlreadyStarted() throws Exception {
        int port = 8080;
        Server server = createMock(Server.class, port);

        HttpService httpService = new HttpService(port, null, null, guiceFilter);

        setInternalState(httpService, "server", server);
        replay(server, Server.class);

        httpService.startUp();

        verify(server, Server.class);
    }

    @Test
    public void stopable() throws Exception {
        int port = 8080;
        Server server = createMockAndExpectNew(Server.class, port);

        HttpService httpService = new HttpService(port, "", null, guiceFilter);

        setInternalState(server, "_lock", new Object());
        setInternalState(httpService, "server", server);
        expect(server.getConnectors()).andReturn(new Connector[]{});
        setInternalState(server, "_state", 3);
        server.stop();
        replay(server, Server.class);

        httpService.shutDown();
    }

    @Test
    public void notStopableAlreadyStarted() throws Exception {
        int port = 8080;
        Server server = createMock(Server.class, port);

        HttpService httpService = new HttpService(port, "", null, guiceFilter);

        replay(server, Server.class);

        httpService.shutDown();
    }
}

