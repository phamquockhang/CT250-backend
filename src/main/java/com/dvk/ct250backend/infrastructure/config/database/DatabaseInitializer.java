package com.dvk.ct250backend.infrastructure.config.database;

import com.dvk.ct250backend.domain.auth.repository.PermissionRepository;
import com.dvk.ct250backend.domain.auth.repository.RoleRepository;
import com.dvk.ct250backend.domain.auth.repository.UserRepository;
import com.dvk.ct250backend.domain.country.repository.CountryRepository;
import com.dvk.ct250backend.domain.flight.repository.AirplaneRepository;
import com.dvk.ct250backend.domain.flight.repository.AirportRepository;
import com.dvk.ct250backend.domain.flight.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;


@Service
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final AirportRepository airportRepository;
    private final AirplaneRepository airplaneRepository;
    private final ModelRepository modelRepository;
    private final DataSource dataSource;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");
        ResourceDatabasePopulator resourceDatabasePopulator =
                new ResourceDatabasePopulator(false,
                        false,
                        "UTF-8",
                        new ClassPathResource("/sql/db-init.sql"));
        resourceDatabasePopulator.execute(dataSource);

    }
}
