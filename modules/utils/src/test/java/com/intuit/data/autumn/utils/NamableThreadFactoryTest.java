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

package com.intuit.data.autumn.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.easymock.EasyMock.expect;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NameableThreadFactory.class, System.class})
public class NamableThreadFactoryTest {

    @Mock
    private SecurityManager securityManager;
    @Mock
    private ThreadGroup threadGroup;

    @Test
    public void namedThread() throws Exception {
        String name = "name";
        NameableThreadFactory nameableThreadFactory = new NameableThreadFactory(name);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
            }
        };

        Thread thread = nameableThreadFactory.newThread(runnable);

        assertThat(thread.getName(), is(name));
    }

    @Test
    public void namedThreadWithSecurityGroup() throws Exception {
        mockStatic(System.class);
        expect(System.getSecurityManager()).andReturn(securityManager);
        when(securityManager.getThreadGroup()).thenReturn(threadGroup);
        replay(System.class);

        String name = "name";
        NameableThreadFactory nameableThreadFactory = new NameableThreadFactory(name);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
            }
        };

        Thread thread = nameableThreadFactory.newThread(runnable);

        assertThat(thread.getName(), is(name));
    }
}