package com.cgnpc.bbxpark.localauth.config;

import com.cgnpc.bbxpark.localauth.web.LocalSessionGuardInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("local-auth")
public class LocalAuthWebConfiguration implements WebMvcConfigurer {

    private final LocalSessionGuardInterceptor sessionGuardInterceptor;

    public LocalAuthWebConfiguration(LocalSessionGuardInterceptor sessionGuardInterceptor) {
        this.sessionGuardInterceptor = sessionGuardInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionGuardInterceptor)
                .addPathPatterns("/user/getCurrentUser", "/uau/perm/**", "/rpt/getToken")
                .order(0);
    }
}
