package com.dvk.ct250backend.infrastructure.config.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class PermissionInterceptorConfig implements WebMvcConfigurer {

    private final CustomPermissionInterceptor permissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/", "/api/v1/auth/**",
                "/api/v1/countries"
        };
        registry.addInterceptor(permissionInterceptor)
                .excludePathPatterns(whiteList);
    }
}
