package com.dvk.ct250backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class Ct250BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Ct250BackendApplication.class, args);
    }

}
