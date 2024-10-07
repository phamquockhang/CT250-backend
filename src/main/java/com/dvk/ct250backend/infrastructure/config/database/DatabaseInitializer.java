package com.dvk.ct250backend.infrastructure.config.database;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DatabaseInitializer {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    final String PERMISSIONS_AFTER_INSERT_TRIGGER = """
                DROP FUNCTION IF EXISTS insert_permission_role CASCADE;
                CREATE OR REPLACE FUNCTION insert_permission_role() RETURNS TRIGGER
                    AS $$
                BEGIN
                    INSERT INTO permission_role (permission_id, role_id)
                    VALUES (NEW.permission_id, 1);
                    RETURN NEW;
                END;
                $$ LANGUAGE plpgsql;
                CREATE TRIGGER insert_permission_role
                AFTER INSERT ON permissions
                FOR EACH ROW
                EXECUTE FUNCTION insert_permission_role();
                """;

    @PostConstruct
    public void initialize() {
        ResourceDatabasePopulator schemaPopulator = new ResourceDatabasePopulator();
        schemaPopulator.addScript(new ClassPathResource("sql/batch-schema.sql"));
        schemaPopulator.addScript(new ClassPathResource("sql/schema.sql"));
        schemaPopulator.execute(dataSource);

        jdbcTemplate.execute(PERMISSIONS_AFTER_INSERT_TRIGGER);

        ResourceDatabasePopulator dataPopulator = new ResourceDatabasePopulator();
        dataPopulator.addScript(new ClassPathResource("sql/data.sql"));
        dataPopulator.execute(dataSource);
    }
}
