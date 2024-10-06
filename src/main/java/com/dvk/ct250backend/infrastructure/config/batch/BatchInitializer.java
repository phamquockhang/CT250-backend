package com.dvk.ct250backend.infrastructure.config.batch;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class BatchInitializer {

    private final DataSource dataSource;

    @PostConstruct
    public void initialize() {
        ResourceDatabasePopulator dropPopulator = new ResourceDatabasePopulator();
        dropPopulator.addScript(new ClassPathResource("sql/drop-batch-schema.sql"));
        dropPopulator.execute(dataSource);

        ResourceDatabasePopulator createPopulator = new ResourceDatabasePopulator();
        createPopulator.addScript(new ClassPathResource("sql/batch-schema.sql"));
        createPopulator.execute(dataSource);
    }
}
