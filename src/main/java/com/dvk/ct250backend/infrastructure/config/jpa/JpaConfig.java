package com.dvk.ct250backend.infrastructure.config.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.dvk.ct250backend.domain.flight.repository",
        "com.dvk.ct250backend.domain.auth.repository",
        "com.dvk.ct250backend.domain.country.repository"})
public class JpaConfig {
}
