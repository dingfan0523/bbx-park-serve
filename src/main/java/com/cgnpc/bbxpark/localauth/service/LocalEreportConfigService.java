package com.cgnpc.bbxpark.localauth.service;

import com.cgnpc.bbxpark.localauth.config.LocalAuthProperties;
import com.cgnpc.ereport.model.ReportTokenModel;
import com.cgnpc.ereport.service.IEreportConfigService;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Primary
@Profile("local-auth")
public class LocalEreportConfigService implements IEreportConfigService {

    private final LocalAuthProperties properties;
    private final AuthenticatedUserProvider authenticatedUser;

    public LocalEreportConfigService(LocalAuthProperties properties, AuthenticatedUserProvider authenticatedUser) {
        this.properties = properties;
        this.authenticatedUser = authenticatedUser;
    }

    @Override
    public ReportTokenModel getAuthToken() {
        ReportTokenModel token = new ReportTokenModel();
        token.setSyscode(properties.getEreportCode());
        token.setUser(authenticatedUser.currentUserId());
        return token;
    }

    @Override
    public String getEreportCode() {
        return properties.getEreportCode();
    }
}
