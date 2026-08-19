package com.cgnpc.bbxpark.localauth.service;

import com.cgnpc.bbxpark.localauth.config.LocalAuthProperties;
import com.cgnpc.ereport.model.ReportTokenModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalHomepageDependencyServiceTest {

    @Test
    void reportTokenKeepsUserWithoutCallingExternalReportService() {
        LocalAuthProperties properties = new LocalAuthProperties();
        AuthenticatedUserProvider authenticatedUser = () -> "LOCAL001";
        LocalEreportConfigService service = new LocalEreportConfigService(properties, authenticatedUser);

        ReportTokenModel token = service.getAuthToken();

        assertEquals("LOCAL001", token.getUser());
        assertNull(token.getAccessToken());
        assertNull(token.getEreportUrl());
    }

    @Test
    void businessMenuIsEmptyWithoutQueryingFrameworkTables() {
        LocalBusinessMenuService service = new LocalBusinessMenuService();

        assertTrue(service.getFormMenuTree(null).isEmpty());
    }
}
