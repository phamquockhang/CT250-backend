package com.dvk.ct250backend.infrastructure.config.database;

import com.dvk.ct250backend.domain.auth.entity.Permission;
import com.dvk.ct250backend.domain.auth.entity.Role;
import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.auth.enums.GenderEnum;
import com.dvk.ct250backend.domain.auth.repository.PermissionRepository;
import com.dvk.ct250backend.domain.auth.repository.RoleRepository;
import com.dvk.ct250backend.domain.auth.repository.UserRepository;
import com.dvk.ct250backend.domain.country.entity.Country;
import com.dvk.ct250backend.domain.country.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CountryRepository countryRepository;


    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");
        long countPermissions = this.permissionRepository.count();
        long countRoles = this.roleRepository.count();
        long countUsers = this.userRepository.count();
        long countCountries = this.countryRepository.count();

        if (countCountries == 0){
            ArrayList<Country> arr = new ArrayList<>();
            arr.add(Country.builder().countryId(1).countryName("Vietnam").countryCode(84).build());
            countryRepository.saveAll(arr);
        }

        if (countPermissions == 0) {
            ArrayList<Permission> arr = new ArrayList<>();

            arr.add(new Permission("Create a user", "/api/v1/users", "POST", "USERS"));
            arr.add(new Permission("Update a user", "/api/v1/users/{id}", "PUT", "USERS"));
            arr.add(new Permission("Delete a user", "/api/v1/users/{id}", "DELETE", "USERS"));
            arr.add(new Permission("Get a user by id", "/api/v1/users/{id}", "GET", "USERS"));
            arr.add(new Permission("Get logged in user", "/api/v1/users/logged-in", "GET", "USERS"));
            arr.add(new Permission("Get users with pagination", "/api/v1/users", "GET", "USERS"));
            arr.add(new Permission("Create a role", "/api/v1/roles", "POST", "ROLES"));
            arr.add(new Permission("Update a role", "/api/v1/roles/{id}", "PUT", "ROLES"));
            arr.add(new Permission("Delete a role", "/api/v1/roles/{id}", "DELETE", "ROLES"));
            arr.add(new Permission("Get a role by id", "/api/v1/roles/{id}", "GET", "ROLES"));
            arr.add(new Permission("Get roles with pagination", "/api/v1/roles", "GET", "ROLES"));
            arr.add(new Permission("Create a permission", "/api/v1/permissions", "POST", "PERMISSIONS"));
            arr.add(new Permission("Update a permission", "/api/v1/permissions/{id}", "PUT", "PERMISSIONS"));
            arr.add(new Permission("Delete a permission", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
            arr.add(new Permission("Get a permission by id", "/api/v1/permissions/{id}", "GET", "PERMISSIONS"));
            arr.add(new Permission("Get permissions with pagination", "/api/v1/permissions", "GET", "PERMISSIONS"));
            arr.add(new Permission("Get all permissions", "/api/v1/permissions/all", "GET", "PERMISSIONS"));

            this.permissionRepository.saveAll(arr);
        }

        if (countRoles == 0) {
           List<Permission> allPermissions = this.permissionRepository.findAll();

            Role adminRole = Role.builder()
                    .roleName("ADMIN")
                    .description("Admin thì full permissions")
                    .active(true)
                    .permissions(allPermissions)
                    .build();

            this.roleRepository.save(adminRole);
        }

        if (countUsers == 0) {
            LocalDate dateOfBirth = LocalDate.of(1999, 5, 6);

            User adminUser = User.builder()
                    .email("admin@gmail.com")
                    .gender(GenderEnum.MALE)
                    .password(this.passwordEncoder.encode("123456"))
                    .firstName("I am")
                    .lastName("ADMIN")
                    .phoneNumber("0123456789")
                    .identityNumber("123456789000")
                    .dateOfBirth(LocalDate.of(2000, 5, 3))
                    .active(true)
                    .build();

            Optional<Role> adminRole = this.roleRepository.findByRoleName("ADMIN");
            adminRole.ifPresent(adminUser::setRole);
            Optional<Country> country = this.countryRepository.findByCountryName("Vietnam");
            country.ifPresent(adminUser::setCountry);

            this.userRepository.save(adminUser);
        }

        if (countPermissions > 0 &&  countRoles > 0 && countUsers > 0) {
            System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
        } else
            System.out.println(">>> END INIT DATABASE");
    }
}
