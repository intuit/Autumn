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

package com.intuit.data.autumn.manage;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.management.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MBeanRegistryTest {

    @Mock
    private MBeanServer mbServer;
    @Mock
    private Object object;

    @Test
    public void register() throws Exception {
        MBeanRegistry mBeanRegistry = new MBeanRegistry(mbServer);

        mBeanRegistry.register(object, "name");
    }

    @Ignore("powermock:Objectname ctor")
    @Test
    public void registerMalformedObjectNameException() throws Exception {
        MalformedObjectNameException mone = mock(MalformedObjectNameException.class);
        when(mbServer.registerMBean(any(Object.class), any(ObjectName.class))).thenThrow(mone);

        MBeanRegistry mBeanRegistry = new MBeanRegistry(mbServer);

        mBeanRegistry.register(object, "name");

        verify(mone, times(2)).getMessage();
    }

    @Test
    public void registerNotCompliantMBeanException() throws Exception {
        NotCompliantMBeanException ncmbe = mock(NotCompliantMBeanException.class);
        when(mbServer.registerMBean(any(Object.class), any(ObjectName.class))).thenThrow(ncmbe);

        MBeanRegistry mBeanRegistry = new MBeanRegistry(mbServer);

        mBeanRegistry.register(object, "name");

        verify(ncmbe, times(2)).getMessage();
    }

    @Test
    public void registerInstanceAlreadyExistsException() throws Exception {
        InstanceAlreadyExistsException iae = mock(InstanceAlreadyExistsException.class);
        when(mbServer.registerMBean(any(Object.class), any(ObjectName.class))).thenThrow(iae);

        MBeanRegistry mBeanRegistry = new MBeanRegistry(mbServer);

        mBeanRegistry.register(object, "name");

        verify(iae, times(2)).getMessage();
    }

    @Test
    public void registerMBeanRegistrationExcpetion() throws Exception {
        MBeanRegistrationException mre = mock(MBeanRegistrationException.class);
        when(mbServer.registerMBean(any(Object.class), any(ObjectName.class))).thenThrow(mre);

        MBeanRegistry mBeanRegistry = new MBeanRegistry(mbServer);

        mBeanRegistry.register(object, "name");

        verify(mre, times(2)).getMessage();
    }
}
