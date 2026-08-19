package com.cgnpc.bbxpark.localauth.service;

import com.cgnpc.cud.shiro.domain.Account;
import com.cgnpc.cud.shiro.util.ShiroUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local-auth")
public class ShiroAuthenticatedUserProvider implements AuthenticatedUserProvider {

    @Override
    public String currentUserId() {
        Account account = ShiroUtils.getUser();
        if (account == null || account.getAccount() == null || account.getAccount().trim().isEmpty()) {
            throw new UnauthenticatedException("未登录");
        }
        return account.getAccount();
    }
}
