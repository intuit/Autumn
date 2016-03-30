package com.intuit.data.autumn.crypto;

import com.google.inject.Injector;
import com.intuit.data.autumn.utils.PropertyFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Properties;

import static com.google.inject.Guice.createInjector;
import static com.intuit.data.autumn.crypto.CryptoModule.*;
import static java.lang.Boolean.TRUE;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PropertyFactory.class)
public class CryptoModuleTest {

    @Test
    public void injectNoopEncryptor() throws Exception {
        Injector injector = createInjector(new CryptoModule());

        assertThat(injector.getInstance(Encryptor.class), instanceOf(NoopEncryptor.class));
    }

    @Test
    public void injectAlgorithmEncryptor() throws Exception {
        Properties testProperties = new Properties();

        mockStatic(PropertyFactory.class);
        expect(PropertyFactory.create(PROPERTY_NAME, CryptoModule.class)).andReturn(testProperties);
        expect(PropertyFactory.create(SECRETS_PROPERTY_NAME, CryptoModule.class)).andReturn(testProperties);
        expect(PropertyFactory.create(DECRYPTION_KEYS_PROPERTY_NAME, CryptoModule.class)).andReturn(testProperties);
        expect(PropertyFactory.getProperty("crypto.enabled", testProperties, TRUE.toString())).andReturn(TRUE.toString());
        expect(PropertyFactory.getProperty("crypto.key", testProperties)).andReturn("hack-key");
        expect(PropertyFactory.getProperty("crypto.algorithm", testProperties)).andReturn("PBEWITHSHA256AND256BITAES");
        expect(PropertyFactory.getProperty("crypto.poolsize", testProperties)).andReturn(Integer.valueOf(5).toString());
        replay(PropertyFactory.class);

        Injector injector = createInjector(new CryptoModule());

        PowerMock.verify(PropertyFactory.class);

        assertThat(injector.getInstance(Encryptor.class), instanceOf(AlgorithmEncryptor.class));
        assertThat(injector.getInstance(Decryptor.class), instanceOf(Decryptor.class));
    }
}
