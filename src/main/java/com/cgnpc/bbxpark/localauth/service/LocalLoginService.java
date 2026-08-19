package com.cgnpc.bbxpark.localauth.service;

import com.cgnpc.bbxpark.localauth.config.LocalAuthProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local-auth")
public class LocalLoginService {

    private final LocalAuthProperties properties;
    private final LocalLoginGateway loginGateway;
    private final LocalSessionWriter sessionWriter;

    public LocalLoginService(LocalAuthProperties properties, LocalLoginGateway loginGateway,
                             LocalSessionWriter sessionWriter) {
        this.properties = properties;
        this.loginGateway = loginGateway;
        this.sessionWriter = sessionWriter;
    }

    public void login() {
        loginGateway.login(properties.getUserId(), properties.getPassword());
        sessionWriter.store(properties.getUserId());
    }
}
