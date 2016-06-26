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

package com.intuit.autumn.utils;

import java.util.concurrent.ThreadFactory;

import static java.lang.System.getSecurityManager;
import static java.lang.Thread.currentThread;

/**
 * A thread factory that provides a means to name/label instantiated threads.
 */

public class NameableThreadFactory implements ThreadFactory {

    private final String name;

    public NameableThreadFactory(final String name) {
        this.name = name;
    }

    /**
     * A nameable thread instance factory
     *
     * @param runnable to be named runnable
     * @return runnable wrapped thread
     */

    @Override
    public Thread newThread(Runnable runnable) {
        SecurityManager securityManager = getSecurityManager();
        ThreadGroup group = securityManager != null ?
                securityManager.getThreadGroup() : currentThread().getThreadGroup();

        return new Thread(group, runnable, name, 0);
    }
}
