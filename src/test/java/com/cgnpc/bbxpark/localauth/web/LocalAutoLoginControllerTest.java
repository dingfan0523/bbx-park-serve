package com.cgnpc.bbxpark.localauth.web;

import com.cgnpc.bbxpark.localauth.config.LocalAuthProperties;
import com.cgnpc.bbxpark.localauth.service.LocalLoginGateway;
import com.cgnpc.bbxpark.localauth.service.LocalLoginService;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.view.RedirectView;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalAutoLoginControllerTest {

    @Test
    void loginCasCreatesLocalSessionAndRedirectsToFrontend() {
        LocalAuthProperties properties = new LocalAuthProperties();
        properties.setUserId("LOCAL001");
        properties.setPassword("local123");
        properties.setFrontendUrl("http://127.0.0.1:8010");
        AtomicReference<String> credentials = new AtomicReference<>();
        LocalLoginGateway gateway = (username, password) -> credentials.set(username + ":" + password);
        LocalLoginService loginService = new LocalLoginService(properties, gateway);
        LocalAutoLoginController controller = new LocalAutoLoginController(properties, loginService);

        RedirectView redirect = controller.loginCas();

        assertEquals("LOCAL001:local123", credentials.get());
        assertEquals("http://127.0.0.1:8010", redirect.getUrl());
    }
}
