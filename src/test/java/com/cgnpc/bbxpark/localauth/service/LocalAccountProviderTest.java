package com.cgnpc.bbxpark.localauth.service;

import com.cgnpc.bbxpark.localauth.config.LocalAuthProperties;
import com.cgnpc.cud.shiro.domain.Account;
import com.cgnpc.cud.shiro.service.ShiroCryptoService;
import org.apache.shiro.authc.AuthenticationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalAccountProviderTest {

    @Test
    void authenticatesOnlyConfiguredLocalUserWithConfiguredPassword() {
        LocalAuthProperties properties = new LocalAuthProperties();
        properties.setUserId("LOCAL001");
        properties.setPassword("local123");
        ShiroCryptoService cryptoService = new ShiroCryptoService();
        LocalAccountProvider provider = new LocalAccountProvider(properties, cryptoService);

        Account account = provider.loadAccount("LOCAL001");

        assertEquals("LOCAL001", account.getAccount());
        assertEquals(cryptoService.password("local123"), account.getPassword());
        assertThrows(AuthenticationException.class, () -> provider.loadAccount("OTHER"));
    }
}
