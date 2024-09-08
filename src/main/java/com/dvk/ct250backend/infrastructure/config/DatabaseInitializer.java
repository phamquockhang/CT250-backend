package com.dvk.ct250backend.infrastructure.config;

import com.dvk.ct250backend.domain.auth.entity.Role;
import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.auth.enums.GenderEnum;
import com.dvk.ct250backend.domain.auth.repository.RoleRepository;
import com.dvk.ct250backend.domain.auth.repository.UserRepository;
import com.dvk.ct250backend.domain.country.repository.CountryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

   // private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");
       // long countPermissions = this.permissionRepository.count();
        long countRoles = this.roleRepository.count();
        long countUsers = this.userRepository.count();

//        if (countPermissions == 0) {
//            ArrayList<Permission> arr = new ArrayList<>();

//
//            arr.add(new Permission("Create a user", "/api/v1/users", "POST", "USERS"));
//            arr.add(new Permission("Update a user", "/api/v1/users", "PUT", "USERS"));
//            arr.add(new Permission("Delete a user", "/api/v1/users/{id}", "DELETE", "USERS"));
//            arr.add(new Permission("Get a user by id", "/api/v1/users/{id}", "GET", "USERS"));
//            arr.add(new Permission("Get users with pagination", "/api/v1/users", "GET", "USERS"));
//            this.permissionRepository.saveAll(arr);
//        }

        if (countRoles == 0) {
           // List<Permission> allPermissions = this.permissionRepository.findAll();

            Role adminRole = new Role();
            adminRole.setRoleName("ADMIN");
            adminRole.setDescription("Admin thì full permissions");
            adminRole.setIsActive(true);
            //adminRole.setPermissions(allPermissions);

            this.roleRepository.save(adminRole);
        }

        if (countUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setGender(GenderEnum.MALE);
            adminUser.setPassword(this.passwordEncoder.encode("123456"));
            adminUser.setFirstName("I am");
            adminUser.setLastName("ADMIN");
            adminUser.setPhoneNumber("0123456789");
            adminUser.setCountry(null);
            adminUser.setIdentityNumber(null);
            adminUser.setDateOfBirth(null);
            Role adminRole = this.roleRepository.findByRoleName("ADMIN");
            if (adminRole != null) {
                adminUser.setRole(adminRole);
            }

            this.userRepository.save(adminUser);
        }

        if (/*countPermissions > 0 && */ countRoles > 0 && countUsers > 0) {
            System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
        } else
            System.out.println(">>> END INIT DATABASE");
    }
}
