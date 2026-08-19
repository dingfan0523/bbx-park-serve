package com.cgnpc.bbxpark.localauth.web;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalI18nBootstrapFilterTest {

    @Test
    void returnsLocalI18nVersionWithoutInvokingJarController() throws Exception {
        LocalI18nBootstrapFilter filter = new LocalI18nBootstrapFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/sys/i18n/item/last-time");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean continued = new AtomicBoolean(false);
        FilterChain chain = (req, res) -> continued.set(true);

        filter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
        assertEquals("application/json;charset=UTF-8", response.getContentType());
        assertTrue(response.getContentAsString().contains("\"category\":\"system\""));
        assertTrue(response.getContentAsString().contains("\"time\":\"2022-03-09 14:37:15\""));
        assertFalse(continued.get());
    }

    @Test
    void leavesUnrelatedRequestsUntouched() throws Exception {
        LocalI18nBootstrapFilter filter = new LocalI18nBootstrapFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/user/getCurrentUser");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean continued = new AtomicBoolean(false);

        filter.doFilter(request, response, (req, res) -> continued.set(true));

        assertTrue(continued.get());
    }
}
