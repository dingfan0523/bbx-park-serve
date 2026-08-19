package com.cgnpc.bbxpark.localauth.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local-auth")
public class ShiroLoginGateway implements LocalLoginGateway {

    @Override
    public void login(String username, String password) {
        SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
    }
}
