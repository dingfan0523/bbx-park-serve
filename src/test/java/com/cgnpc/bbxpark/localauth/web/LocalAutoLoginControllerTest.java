package com.cgnpc.bbxpark.localauth.web;

import com.cgnpc.bbxpark.localauth.config.LocalAuthProperties;
import com.cgnpc.bbxpark.localauth.service.LocalLoginGateway;
import com.cgnpc.bbxpark.localauth.service.LocalLoginService;
import com.cgnpc.bbxpark.localauth.service.LocalSessionWriter;
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
        AtomicReference<String> sessionUserId = new AtomicReference<>();
        LocalLoginGateway gateway = (username, password) -> credentials.set(username + ":" + password);
        LocalSessionWriter sessionWriter = sessionUserId::set;
        LocalLoginService loginService = new LocalLoginService(properties, gateway, sessionWriter);
        LocalAutoLoginController controller = new LocalAutoLoginController(properties, loginService);

        RedirectView redirect = controller.loginCas();

        assertEquals("LOCAL001:local123", credentials.get());
        assertEquals("LOCAL001", sessionUserId.get());
        assertEquals("http://127.0.0.1:8010", redirect.getUrl());
    }
}
