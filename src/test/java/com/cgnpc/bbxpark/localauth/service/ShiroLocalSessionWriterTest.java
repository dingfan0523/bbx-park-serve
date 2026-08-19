package com.cgnpc.bbxpark.localauth.service;

import com.cgnpc.bbxpark.localauth.config.LocalAuthProperties;
import com.cgnpc.cud.boot.autoconfigure.shiro.ShiroProperties;
import com.cgnpc.cud.shiro.domain.Account;
import com.cgnpc.cud.shiro.service.ShiroCryptoService;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShiroLocalSessionWriterTest {

    @Test
    void storeMakesLocalAccountAvailableAsCurrentShiroUser() {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        Subject subject = new Subject.Builder(securityManager).buildSubject();
        ThreadContext.bind(securityManager);
        ThreadContext.bind(subject);
        try {
            LocalAuthProperties properties = new LocalAuthProperties();
            properties.setUserId("LOCAL001");
            properties.setPassword("local123");
            LocalAccountProvider accountProvider = new LocalAccountProvider(properties, new ShiroCryptoService());
            ShiroLocalSessionWriter sessionWriter = new ShiroLocalSessionWriter(accountProvider);

            sessionWriter.store("LOCAL001");

            Account currentUser = (Account) subject.getSession(false)
                    .getAttribute(ShiroProperties.ATTRIBUTE_SESSION_CURRENT_USER);
            assertEquals("LOCAL001", currentUser.getAccount());
        } finally {
            ThreadContext.unbindSubject();
            ThreadContext.unbindSecurityManager();
        }
    }
}
