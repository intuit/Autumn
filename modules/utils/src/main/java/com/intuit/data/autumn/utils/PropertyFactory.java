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

import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.google.common.base.Strings.emptyToNull;
import static java.lang.System.getenv;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A property value retriever which provides for operational overrides, namely in order of precedence:
 * <p/>
 * System.getEnv()
 * System.getProperty()
 * provided property default value
 */

public class PropertyFactory {

    private static final Logger LOGGER = getLogger(PropertyFactory.class);

    private PropertyFactory() {
        throw new UnsupportedOperationException();
    }

    /**
     * Properties creator given the provided resource name.
     *
     * @param propertyResourceName to be instantiated property resource name
     * @return instantiated Properties instance
     */

    public static Properties create(final String propertyResourceName) {
        return create(propertyResourceName, null);
    }

    /**
     * Properties creator given the provided resource name and package local reference class
     *
     * @param propertyResourceName to be instantiated property resource name
     * @param base                 package local reference class
     * @return instantiated Properties instance
     */

    public static Properties create(final String propertyResourceName, @Nullable final Class base) {
        LOGGER.debug("creating property: {}", propertyResourceName);
        Properties properties = new Properties();

        if (propertyResourceName != null) {
            LOGGER.debug("reading property: {}", propertyResourceName);

            try (InputStream is = PropertyFactory.class.getResourceAsStream(propertyResourceName)) {
                LOGGER.debug("loading property: {}", propertyResourceName);

                properties.load(is);
            } catch (IOException e) {
                String msg = "unable to start service: " + propertyResourceName + " properties, cause: " +
                        e.getMessage();

                LOGGER.warn(msg, e);
            }

            fromJar(propertyResourceName, base, properties);
        }

        return properties;
    }

    private static void fromJar(final String propertyResourceName, @Nullable final Class base,
                                final Properties properties) {
        if (base == null) {
            return;
        }

        CodeSource src = base.getProtectionDomain().getCodeSource();

        if (src == null) {
            return;
        }

        Properties propertiesFromJar = getPropertyFromJar(base, propertyResourceName.substring(1), src);

        for (Entry<Object, Object> entry : propertiesFromJar.entrySet()) {
            if (!properties.containsKey(entry.getKey())) {
                LOGGER.debug("overriding key: {}, newValue: {}, originalValue: {}",
                        new Object[]{entry.getKey(), propertiesFromJar, entry.getValue()});

                properties.setProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }
    }

    private static Properties getPropertyFromJar(final Class base, final String property, final CodeSource src) {
        Properties properties = new Properties();
        URL jar = src.getLocation();

        return jar != null ? readZip(base, jar, property, properties) : properties;
    }

    private static Properties readZip(final Class base, final URL jar, final String property,
                                      final Properties properties) {
        try (ZipInputStream zip = new ZipInputStream(jar.openStream())) {
            for (ZipEntry ze = zip.getNextEntry(); ze != null; ze = zip.getNextEntry()) {
                if (ze.getName().equals(property)) {
                    properties.load(zip);

                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.warn("unable to read jar: {}, property: {}, class: {}, cause: {}",
                    new Object[]{jar, property, base.getSimpleName(), e.getMessage()}, e);
        }

        return properties;
    }

    /**
     * Property value getter.
     *
     * @param key        property key
     * @param properties properties object
     * @return return named property value
     */

    public static String getProperty(final String key, final Properties properties) {
        return getProperty(key, properties, null);
    }

    /**
     * Property resolve order
     * 1. Environment Variable set at System level
     * 2. Java System Property set by the -D flag at runtime
     * 3. The property File it self
     *
     * @param key          property key
     * @param properties   property collection
     * @param defaultValue default value
     * @return resolved value
     */

    public static String getProperty(final String key, final Properties properties, final String defaultValue) {
        String value = getenv(key);

        if (!isEmpty(value)) {
            return value;
        }

        value = System.getProperty(key, properties.getProperty(key));

        return !isEmpty(value) ? value : defaultValue;
    }
}
