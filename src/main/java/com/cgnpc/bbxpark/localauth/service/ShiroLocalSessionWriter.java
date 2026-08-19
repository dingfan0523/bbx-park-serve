package com.cgnpc.bbxpark.localauth.service;

import com.cgnpc.cud.boot.autoconfigure.shiro.ShiroProperties;
import com.cgnpc.cud.shiro.domain.Account;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local-auth")
public class ShiroLocalSessionWriter implements LocalSessionWriter {

    private final LocalAccountProvider accountProvider;

    public ShiroLocalSessionWriter(LocalAccountProvider accountProvider) {
        this.accountProvider = accountProvider;
    }

    @Override
    public void store(String userId) {
        Account account = accountProvider.loadAccount(userId);
        SecurityUtils.getSubject().getSession()
                .setAttribute(ShiroProperties.ATTRIBUTE_SESSION_CURRENT_USER, account);
    }
}
