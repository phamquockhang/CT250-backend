package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserDTO>> getAllUsers() {
        return ApiResponse.<List<UserDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUserById(@PathVariable UUID id) {
        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.OK.value())
                .data(userService.getUserById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.CREATED.value())
                .data(userService.createUser(userDTO))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserDTO> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.OK.value())
                .data(userService.updateUser(id, userDTO))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .build();
    }
}