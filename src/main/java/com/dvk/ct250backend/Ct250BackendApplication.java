package com.dvk.ct250backend;

import com.dvk.ct250backend.infrastructure.config.context.DotEnvApplicationContextInitializer;
import com.dvk.ct250backend.infrastructure.config.security.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableConfigurationProperties(RSAKeyRecord.class)
public class Ct250BackendApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Ct250BackendApplication.class);
        springApplication.addInitializers(new DotEnvApplicationContextInitializer());
        springApplication.run(args);
    }

}
