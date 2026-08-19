package com.cgnpc.bbxpark.localauth.web;

import com.cgnpc.bbxpark.localauth.config.LocalAuthProperties;
import com.cgnpc.bbxpark.localauth.service.LocalLoginService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@Profile("local-auth")
public class LocalAutoLoginController {

    private final LocalAuthProperties properties;
    private final LocalLoginService loginService;

    public LocalAutoLoginController(LocalAuthProperties properties, LocalLoginService loginService) {
        this.properties = properties;
        this.loginService = loginService;
    }

    @GetMapping("/login/cas")
    public RedirectView loginCas() {
        loginService.login();
        return new RedirectView(properties.getFrontendUrl());
    }
}
