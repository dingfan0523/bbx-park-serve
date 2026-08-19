package com.cgnpc.bbxpark.localauth.service;

import com.cgnpc.bbxpark.localauth.config.LocalAuthProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local-auth")
public class LocalLoginService {

    private final LocalAuthProperties properties;
    private final LocalLoginGateway loginGateway;

    public LocalLoginService(LocalAuthProperties properties, LocalLoginGateway loginGateway) {
        this.properties = properties;
        this.loginGateway = loginGateway;
    }

    public void login() {
        loginGateway.login(properties.getUserId(), properties.getPassword());
    }
}
