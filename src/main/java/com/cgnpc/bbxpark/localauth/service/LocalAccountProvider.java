package com.cgnpc.bbxpark.localauth.service;

import com.cgnpc.bbxpark.localauth.config.LocalAuthProperties;
import com.cgnpc.bbxpark.localauth.model.LocalAccount;
import com.cgnpc.cud.shiro.domain.Account;
import com.cgnpc.cud.shiro.service.AccountProvider;
import com.cgnpc.cud.shiro.service.ShiroCryptoService;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@Primary
@Profile("local-auth")
public class LocalAccountProvider implements AccountProvider {

    private final LocalAuthProperties properties;
    private final ShiroCryptoService cryptoService;

    public LocalAccountProvider(LocalAuthProperties properties, ShiroCryptoService cryptoService) {
        this.properties = properties;
        this.cryptoService = cryptoService;
    }

    @Override
    public Account loadAccount(String account) throws AuthenticationException {
        if (!properties.getUserId().equals(account)) {
            throw new AuthenticationException("账号或密码错误");
        }
        return new LocalAccount(account, cryptoService.password(properties.getPassword()));
    }

    @Override
    public Set<String> loadRoles(String account) {
        return Collections.singleton(properties.getRoleCode());
    }

    @Override
    public Set<String> loadPermissions(String account) {
        return LocalPermissionService.permissionCodes();
    }
}
