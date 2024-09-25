package com.dvk.ct250backend;

import com.dvk.ct250backend.infrastructure.config.security.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableConfigurationProperties(RSAKeyRecord.class)
public class Ct250BackendApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Ct250BackendApplication.class, args);
    }

}
