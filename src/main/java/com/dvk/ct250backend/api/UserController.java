package com.dvk.ct250backend.api;

import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    final UserService userService;
    final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) {
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        UserDTO newUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}